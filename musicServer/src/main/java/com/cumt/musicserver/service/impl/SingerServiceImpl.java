package com.cumt.musicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cumt.musicserver.domain.Consumer;
import com.cumt.musicserver.domain.Singer;
import com.cumt.musicserver.dao.SingerDao;
import com.cumt.musicserver.domain.SongList;
import com.cumt.musicserver.service.IConsumerService;
import com.cumt.musicserver.service.ISingerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.musicserver.util.FileUtil;
import com.cumt.musicserver.util.Result;
import com.cumt.musicserver.util.StaticString;
import jodd.util.StringUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

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
@Transactional
public class SingerServiceImpl extends ServiceImpl<SingerDao, Singer> implements ISingerService {
    @Resource
    private SingerDao singerDao;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IConsumerService consumerService;
    @Override
    public Result insertSinger(Singer singer,MultipartFile file) {
        if(com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(file)){
            singer.setPic(SINGER_DEFAULT_PIC);
        }else {
            String s = FileUtil.uploadFile(file, "img", "singerPic");
            singer.setPic(s);
        }
        singer.setPic(SINGER_DEFAULT_PIC);
        if(save(singer)){
            return Result.ok("添加成功");
        }
        return Result.fail("添加失败");

    }
    @Override
    public Result updateSinger(Singer singer,MultipartFile file) {
        String picUrl=lambdaQuery().select(Singer::getPic).eq(Singer::getId,singer.getId()).one().getPic();
        if(ObjectUtils.isNotEmpty(file)){
            if(ObjectUtils.isNotEmpty(file)){
                String s = FileUtil.uploadFile(file, "img", "singerPic");
                singer.setPic(s);
            }
        }
        if(updateById(singer) && ObjectUtils.isNotEmpty(file)){
            if(!SINGER_DEFAULT_PIC.equals(picUrl)) FileUtil.deleteFile(Collections.singletonList(picUrl));
            return Result.ok("修改成功");
        }
        return Result.fail("修改失败");
    }
    @Override
    public Result deleteSinger(Integer id) {
//        boolean update = update().eq("id", id).update();
        String pic = lambdaQuery().select(Singer::getPic).eq(Singer::getId, id).one().getPic();
        boolean b = removeById(id);
        if(b){
            deleteCount(Collections.singletonList(id));
            if(!SINGER_DEFAULT_PIC.equals(pic) && ObjectUtils.isNotEmpty(pic)) {
                FileUtil.deleteFile(Collections.singletonList(pic));
            }
            return Result.ok("删除成功");
        }
        return Result.fail("删除失败");
    }
    @Override
    public Result getList() {
        Integer count = query().count();
        return Result.ok(count);
    }
    @Override
    public Result getLikeName(String name) {
        List<Singer> name1 = lambdaQuery().likeRight(Singer::getName, name).list();
        return Result.ok(name1);
    }
    @Override
    public Result getBySex(Integer sex) {
        List<Singer> sex1 =lambdaQuery().eq(Singer::getSex, sex).list();
        return Result.ok(sex1);
    }


    @Override
    public Result getLikeNames(String name) {
        Set<String> set=new HashSet<>();
        query().select("name").likeRight("name",name).list().forEach(e->{
            set.add(e.getName());
        });
        return Result.ok(set);
    }

    @Override
    public Result getSingerByName(String name) {
        List<Singer> name1 = query().eq("name", name).list();
        return Result.ok(name1);
    }

    @Override
    public Result getSingerPage(String likeName, Integer currentPage,Integer pageSize) {
        if(likeName==null || "".equals(likeName)){
            List<Singer> records = query().page(new Page<>(currentPage, pageSize)).getRecords();
            setCount(records);
            return Result.ok(records);
        }else {
            QueryChainWrapper<Singer> logic_delete = query();
            Integer count=logic_delete.count();
            List<Singer> records = logic_delete.page(new Page<>(currentPage, pageSize)).getRecords();
            Map<String,Object> map = new HashMap<>();
            map.put("count",count);
            map.put("singers",records);
            setCount(records);
            return Result.ok(map);
        }
    }

    @Override
    public Result deleteSingerByIds(List<Integer> ids) {
        List<Singer> list = lambdaQuery().select(Singer::getPic).in(Singer::getId, ids).list();
        boolean update = update().in("id", ids).update();
//        stringRedisTemplate.opsForZSet().remove()
        if(update){
            deleteCount(ids);
            List<String> delete=new ArrayList<>();
            list.forEach(a-> {
                if(ObjectUtils.isNotEmpty(a))delete.add(a.getPic());
            });
            FileUtil.deleteFile(delete);
            return Result.ok("删除成功");
        }
        return Result.fail("删除失败");
    }
    @Override
    public Result restore() {
        if (update().set("logic_delete", true).update()) {
            return Result.ok("恢复成功");
        }else {
            return Result.fail("恢复失败");
        }

    }

    @Override
    public Result getSingerSex() {
        Integer man=query().eq("sex",1).count();
        Integer woman=query().eq("sex",0).count();
        Integer queue=query().eq("sex",2).count();
        Map<String,Integer> map=new HashMap<>();
        map.put("man",man);
        map.put("woman",woman);
        map.put("queue",queue);
        return Result.ok(map);
    }

    @Override
    public Result getSingerCountry() {
        QueryWrapper<Singer> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("location as name", "count(*) as value").groupBy("location");
        List<Map<String, Object>> maps = listMaps(queryWrapper);
         return Result.ok(maps);
    }

    @Override
    public Result getPartSinger() {
        List<Singer> list=null;
//        List<Singer> list = query().orderByDesc("likes").last("limit 0,10").list();
        Random r=new Random();
        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(SINGER_LIKES_COUNT))
                && Objects.requireNonNull(stringRedisTemplate.opsForZSet().size(SINGER_LIKES_COUNT)).intValue()>10){
            Set<String> strings = stringRedisTemplate.opsForZSet().reverseRange(SINGER_LIKES_COUNT, 0, 9);
             list= lambdaQuery().in(Singer::getId, strings).list();
        }else {
            list=lambdaQuery().page(new Page<>(r.nextInt(9),10)).getRecords();
        }
        setCount(list);
        Collections.shuffle(list);
        return Result.ok(list);
    }

    @Override
    public Result getSingerById(Integer id) {
        Singer one = lambdaQuery().eq(Singer::getId, id).one();
        Object o= SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        if(o instanceof Consumer){
            Boolean member = stringRedisTemplate.opsForSet().isMember(SINGER_LIKES + id,
                    ((Consumer) o).getId().toString());
            if(BooleanUtils.isTrue(member)){
                one.setIsLike("success");
            }
        }
        setCount(Collections.singletonList(one));
        return Result.ok(one);
    }

    @Override
    public Result consumerLikeSinger(Integer id) {
        Object o= SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        Consumer c= (Consumer) o;
//        boolean update = lambdaUpdate().eq(Singer::getId, id).setSql("likes=likes+1").update();
        Double aDouble = stringRedisTemplate.opsForZSet().incrementScore(SINGER_LIKES_COUNT, id.toString(), 1);
        if(ObjectUtils.isNotEmpty(aDouble) && aDouble.intValue()>=0){
            Long add = stringRedisTemplate.opsForSet().add(SINGER_LIKES + id, String.valueOf(c.getId()));
            Long add1 = stringRedisTemplate.opsForSet().add(CONSUMER_LIKE_SINGER + c.getId(), id.toString());
            if(add!=null && add!=0 && add1!=null && add1!=0){
                return Result.ok();
            }
        }
        return Result.fail("关注失败");
    }

    @Override
    public Result consumerGetLikeSinger(Integer id) {
        Set<String> members = stringRedisTemplate.opsForSet().members(CONSUMER_LIKE_SINGER + id);
        if(Objects.isNull(members)) return Result.ok();
        List<String> list = new ArrayList<>(members);
        if(ObjectUtils.isNotEmpty(list)) {
            List<Singer> list1 = lambdaQuery().in(Singer::getId, list).list();
            Set<String> set = new HashSet<>(list1.size());
            list1.forEach(a -> set.add(a.getId().toString()));
            members.removeAll(set);
            if (!members.isEmpty())
                stringRedisTemplate.opsForSet().remove(CONSUMER_LIKE_SINGER + id, members.toArray());
            setCount(list1);
            return Result.ok(list1);
        }
        return Result.ok(new ArrayList<>());
    }

    @Override
    public Result consumerDeleteLikeSinger(Integer id) {
        Object o= SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        Consumer c= (Consumer) o;
        Long remove = stringRedisTemplate.opsForSet().remove(SINGER_LIKES + id.toString(), c.getId().toString());
        Long remove1 = stringRedisTemplate.opsForSet().remove(CONSUMER_LIKE_SINGER + c.getId(), id.toString());
        if(!Objects.isNull(remove) && !Objects.isNull(remove1)){
//            boolean update = lambdaUpdate().eq(Singer::getId, id).setSql("likes=likes-1").update();
            Double aDouble = stringRedisTemplate.opsForZSet().incrementScore(SINGER_LIKES_COUNT, id.toString(), -1);
            if(ObjectUtils.isNotEmpty(aDouble) && aDouble.intValue()>=0) return Result.ok();
        }
        return Result.fail("取消关注失败");
    }

    @Override
    public Result singerGetCountByLetter(String letter,Integer sex,String location) {
        if(ObjectUtils.isEmpty(letter) || ObjectUtils.isEmpty(sex) || ObjectUtils.isEmpty(location)){
            return Result.fail("查询失败");
        }
        Integer count;
        if("全部".equals(letter) && sex==3 && "全部".equals(location)){
            count=count();
        }else {
            count = singerDao.singerGetCountByLetter(letter, sex, location);
        }
//        if("全部".equals(letter) && sex==3){
//            count = lambdaQuery().count();
//        }else if("全部".equals(letter)){
//            count=lambdaQuery().eq(Singer::getSex,sex).count();
//        } else if (sex==3) {
//            count=singerDao.singerGetCountByLetter(letter,null);
//        }else {
//            count=singerDao.singerGetCountByLetter(letter,sex);
//        }
        return Result.ok(count);
    }

    @Override
    public Result singerGetByLetter(String letter, Integer currentPage, Integer pageSize,Integer sex,String location) {
        if(ObjectUtils.isEmpty(letter)){
            return Result.fail("查询失败");
        }
        List<Singer> list;
        if("全部".equals(letter) && sex==3 && "全部".equals(location)){
            list=query().page(new Page<>(currentPage,pageSize)).getRecords();
        }else {
            currentPage = (currentPage - 1) * pageSize;
            list = singerDao.singerGetByLetter(letter, currentPage, pageSize, sex, location);
        }
        setCount(list);
        return Result.ok(list);
    }

    @Override
    public Result getLikeSinger(Integer id) {
        List<String> list=null;
        if(ObjectUtils.isNotEmpty(id) && BooleanUtils.isTrue(stringRedisTemplate.hasKey(SINGER_LIKES + id.toString()))) {
            list = stringRedisTemplate.opsForSet().randomMembers(SINGER_LIKES + id, 8);
        }
        List<Consumer> res=new ArrayList<>();
        if(ObjectUtils.isNotEmpty(list)) {
            res=consumerService.lambdaQuery().select(Consumer::getId,Consumer::getAvator).in(Consumer::getId, list).list();
        }
        return Result.ok(res);
    }

    @Override
    @Transactional
    public Result changeImg(MultipartFile multipartFile, Integer id) {
        Singer one = lambdaQuery().select(Singer::getPic).eq(Singer::getId, id).one();
        String temp=one.getPic();
        String s = FileUtil.uploadFile(multipartFile, "img", "singerPic");
        if(ObjectUtils.isNotEmpty(s)){
            boolean update=lambdaUpdate().eq(Singer::getId,id).set(Singer::getPic,s).update();
            if (update) {
                if (!SINGER_DEFAULT_PIC.equals(temp) && FileUtil.deleteFile(Collections.singletonList(temp))) {
                    return Result.ok("更新成功");
                }
            }
        }
        return Result.fail("更新失败");
    }

    @Override
    public Result getTogetLikedSingers(Integer id) {
        Object o= SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        Consumer c= (Consumer) o;
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(CONSUMER_LIKE_SINGER + c.getId(), CONSUMER_LIKE_SINGER + id);
        List<Singer> list = lambdaQuery().in(Singer::getId, intersect).list();
        setCount(list);
        return Result.ok(list);
    }

    @Override
    public void setCount(List<Singer> list1) {
        List<String> list=new ArrayList<>();
        list1.forEach(a-> {
            if(ObjectUtils.isNotEmpty(a)) {
                list.add(a.getId().toString());
            }
        });
        if(ObjectUtils.isNotEmpty(list)) {
            List<Double> score = stringRedisTemplate.opsForZSet().score(SINGER_LIKES_COUNT, list.toArray());
            if (ObjectUtils.isNotEmpty(score)) {
                for (int i = 0; i < score.size(); i++) {
                    if (ObjectUtils.isEmpty(score.get(i))) continue;
                    list1.get(i).setLikes(score.get(i).intValue());
                }
            }
        }
    }

    @Override
    public void deleteCount(List<Integer> list1) {
        List<String> list=new ArrayList<>();
        list1.forEach(a->list.add(a.toString()));
        stringRedisTemplate.opsForZSet().remove(SINGER_LIKES_COUNT,list.toArray());
        List<String> list2=new ArrayList<>();
        list1.forEach(a->list2.add(SINGER_LIKES+a.toString()));
        stringRedisTemplate.delete(list2);
    }
}
