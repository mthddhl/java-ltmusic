package com.cumt.musicserver.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cumt.musicserver.dao.ConsumerRoleDao;
import com.cumt.musicserver.dao.RoleDao;
import com.cumt.musicserver.domain.Consumer;
import com.cumt.musicserver.dao.ConsumerDao;
import com.cumt.musicserver.domain.ConsumerRole;
import com.cumt.musicserver.service.IConsumerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.musicserver.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.ObjectUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.cumt.musicserver.util.StaticString.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
@Service
//@Transactional
public class ConsumerServiceImpl extends ServiceImpl<ConsumerDao, Consumer> implements IConsumerService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;



    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private ConsumerRoleDao consumerRoleDao;
    @Resource
    private RoleDao roleDao;
    private ObjectMapper objectMapper=new ObjectMapper();
    @Override
    public Result consumerListGetPage(String likeName, Integer currentPage, Integer pageSize) {
        QueryChainWrapper<Consumer> username = query().eq((likeName != null && !"".equals(likeName)), "username", likeName);
        Integer count = username.count();
        List<Consumer> records = username.page(new Page<>(currentPage, pageSize)).getRecords();
        records.forEach(each-> each.setPassword(""));
        Map<String,Object> map=new HashMap<>();
        map.put("count",count);
        map.put("consumers",records);
        return Result.ok(map);
    }

    @Override
    public Result consumerGetLikeName(String name) {
        List<Consumer> list = query().select("username").likeRight("username", name).list();
        return Result.ok(list);
    }

    @Override
    @Transactional
    public Result consumerInsert(Consumer consumer) {
        String encode = passwordEncoder.encode(consumer.getPassword());
        consumer.setPassword(encode);
        consumer.setCreateTime(LocalDateTime.now());
        consumer.setUpdateTime(LocalDateTime.now());
        consumer.setAvator(consumerDefaultPic);
        consumer.setVipExpireTime(LocalDateTime.now());
        if(save(consumer)){
            int insert = consumerRoleDao.insert(new ConsumerRole(consumer.getId(), 2));
            if(insert==1){
                return Result.ok("注册成功");
            }
        }
        return Result.fail("注册失败");
    }

    @Override
    public Result consumerRestore() {
        boolean delete_logic = update().set("delete_logic", true).update();
        return delete_logic? Result.ok() : Result.fail("恢复失败");
    }

    @Override
    public Result getConsumerInfo(Integer id) {
        Consumer one = lambdaQuery().eq(Consumer::getId, id).one();
        ConsumerInfo consumerInfo=new ConsumerInfo();
        consumerInfo.setId(one.getId());
        consumerInfo.setAvator(one.getAvator());
        return Result.ok(consumerInfo);
    }

    @Override
    public Result consumerGetInfo(Integer id) {
        Consumer one = lambdaQuery().eq(Consumer::getId, id).one();
//        return Result.ok(one);
        ConsumerInfo consumerInfo=new ConsumerInfo();
        if(ObjectUtils.isNotEmpty(one)) {
            consumerInfo.setAvator(one.getAvator());
            consumerInfo.setBirth(one.getBirth());
            consumerInfo.setEmail(one.getEmail());
            consumerInfo.setIntroduction(one.getIntroduction());
            consumerInfo.setPhoneNum(one.getPhoneNum());
            consumerInfo.setUsername(one.getUsername());
            consumerInfo.setLocation(one.getLocation());
            consumerInfo.setSex(one.getSex());
        }
        return Result.ok(consumerInfo);
    }

    @Override
    public Result changeImg(MultipartFile multipartFile, Integer id, HttpServletResponse httpServletResponse) {
        String s = FileUtil.uploadFile(multipartFile, "img", "consumer");
        String id2 = query().eq("id", id).one().getAvator();
        boolean id1=true;
        if(!consumerDefaultPic.equals(id2)){
            id1 = FileUtil.deleteFile(Collections.singletonList(id2));
        }
        if(!id1) {
           return Result.fail("更新失败");
        }
        lambdaUpdate().eq(Consumer::getId, id).set(Consumer::getAvator, s).update();
        return Result.ok("更新成功");
    }

    @Override
    public Result consumerUpdate(Consumer consumer) {
        consumer.setUpdateTime(LocalDateTime.now());
        if(ObjectUtils.isNotEmpty(consumer.getPassword())){
            String encode = passwordEncoder.encode(consumer.getPassword());
            consumer.setPassword(encode);
        }
        return updateById(consumer)? Result.ok("更新成功") :Result.fail("更新失败");
    }

    @Override
    public Result consumerDelete(Integer id) {
        if(removeById(id)){
            deleteRedisConsumer(Collections.singletonList(id));
            return Result.ok("删除成功");
        }
        return Result.fail("删除失败");
    }

    @Override
    public Result consumerDeleteIds(List<Integer> list) {
        if(removeByIds(list)){
            deleteRedisConsumer(list);
            return Result.ok("删除成功");
        }
        return Result.fail("删除失败");
    }




    @RabbitListener(bindings =@QueueBinding(
            value = @Queue(name="consumerVIP"),
            exchange = @Exchange(name = "consumer",type = ExchangeTypes.DIRECT),
            key = {"consumer.VIP"}
    ))
    @Override
    @Transactional
    public void updateConsumerVIP(Map<String,String> map, Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        String orderNo = map.get("orderNo");
        RLock lock = redissonClient.getLock("lock:mq:consumer:" + orderNo);
        if(lock.tryLock()) {
            try {
                if(Boolean.TRUE.equals(stringRedisTemplate.hasKey("label:mq:consumer:" + orderNo))){
                    return;
                }
                int consumerId = Integer.parseInt(map.get("consumerId"));
                String vipType = map.get("productType");
                Consumer one = lambdaQuery().eq(Consumer::getId, consumerId).one();
                LocalDateTime vipExpireTime = one.getVipExpireTime();
                if (vipExpireTime.isBefore(LocalDateTime.now())) {
                    vipExpireTime = LocalDateTime.now();
                }
                LocalDateTime localDateTime = null;
                if (VIPType.MONTH_VIP.getType().equals(vipType) || VIPType.MONTH_VIP_DISCOUNT.getType().equals(vipType)) {
                    localDateTime = vipExpireTime.plusMonths(1).plusDays(1);
                } else if (VIPType.QUARTER_VIP.getType().equals(vipType) || VIPType.QUARTER_VIP_DISCOUNT.getType().equals(vipType)) {
                    localDateTime = vipExpireTime.plusMonths(3).plusDays(3);
                } else if (VIPType.YEAR_VIP.getType().equals(vipType) || VIPType.YEAR_VIP_DISCOUNT.getType().equals(vipType)) {
                    localDateTime = vipExpireTime.plusYears(1).plusDays(12);
                }
                if (ObjectUtils.isNotEmpty(localDateTime)) {
                    one.setVipExpireTime(localDateTime);
                    if(updateById(one)){
                        stringRedisTemplate.opsForValue().set("label:mq:consumer:"
                                + orderNo,"1",24,TimeUnit.HOURS);
                        channel.basicAck(tag,true);
                    }
                }
            }catch (Exception e){
                try {
                    Thread.sleep(1000);
                    channel.basicNack(tag,true,true);
                } catch (InterruptedException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            finally {
                lock.unlock();
            }
        }
    }

    public void deleteRedisConsumer(List<Integer> ids){
        if(ObjectUtils.isEmpty(ids)) return;
        List<String> list=new ArrayList<>();
        ids.forEach(a-> {
            list.add(CONSUMER_LIKE_SINGER + a);
            list.add(CONSUMER_LIKE_SONGlIST+a);
            list.add(CONSUMER_LIKE_SONGS+a);
            list.add(CONSUMER_LIKE_SONGS_SORT+a);
        });
         stringRedisTemplate.delete(list);
    }


}
