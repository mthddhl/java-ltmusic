package com.cumt.musicserver.service;

import com.cumt.musicserver.domain.Consumer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cumt.musicserver.util.Result;
import com.rabbitmq.client.Channel;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
public interface IConsumerService extends IService<Consumer> {

    Result consumerListGetPage(String likeName, Integer currentPage, Integer pageSize);

    Result consumerGetLikeName(String name);

    Result consumerInsert(Consumer consumer);

    Result consumerRestore();

    Result getConsumerInfo(Integer id);

    Result consumerGetInfo(Integer id);

    Result changeImg(MultipartFile multipartFile, Integer id, HttpServletResponse httpServletResponse);

    Result consumerUpdate(Consumer consumer);

    Result consumerDelete(Integer id);

    Result consumerDeleteIds(List<Integer> list);

    void updateConsumerVIP(Map<String,String> map, Channel channel,long tag);
}
