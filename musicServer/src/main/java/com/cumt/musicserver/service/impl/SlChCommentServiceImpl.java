package com.cumt.musicserver.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.cumt.musicserver.dao.SlChCommentDao;
import com.cumt.musicserver.domain.Consumer;
import com.cumt.musicserver.domain.SlChComment;
import com.cumt.musicserver.domain.SongListComment;
import com.cumt.musicserver.service.SlChCommentService;
import com.cumt.musicserver.util.Result;
import com.cumt.musicserver.util.StaticString;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SlChCommentServiceImpl extends MPJBaseServiceImpl<SlChCommentDao, SlChComment> implements SlChCommentService {

    public final static String COMMENT_REPLY_COUNT="comment:reply:count";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result replyComment(SlChComment slChComment) {
        slChComment.setCreateTime(LocalDateTime.now());
        Consumer o= (Consumer) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        slChComment.setUserId(o.getId());
        if(save(slChComment)){
            stringRedisTemplate.opsForZSet().incrementScore(COMMENT_REPLY_COUNT,slChComment.getPid().toString(),1);
            return Result.ok("回复成功");
        }
        return Result.fail("回复失败");
    }

    public final static String CHILDREN_COMMENT_LIKES="children:comment:likes:";

    @Override
    public Result consumerChildrenLikeComment(Integer commentId, Boolean liked) {
        Consumer o= (Consumer) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        if(BooleanUtils.isTrue(liked)) {
            boolean update = lambdaUpdate().eq(SlChComment::getId, commentId).setSql("likes=likes-1").update();
            if (update) {
                stringRedisTemplate.opsForSet().remove(CHILDREN_COMMENT_LIKES + o.getId(),commentId.toString());
            }else {
                return Result.fail("点赞失败,尝试重新登陆");
            }
        }else {
            boolean update = lambdaUpdate().eq(SlChComment::getId, commentId).setSql("likes=likes+1").update();
            if(update){
                stringRedisTemplate.opsForSet().add(CHILDREN_COMMENT_LIKES+o.getId(),commentId.toString());
            }else {
                return Result.fail("点赞失败,尝试重新登陆");
            }
        }
        return Result.ok();
    }

    public void setLikeCount(List<SlChComment> list){
        Object o= SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        if(ObjectUtils.isEmpty(o) || !(o instanceof Consumer)){
            return;
        }
        Consumer c=(Consumer) o;
        List<String> list1=new ArrayList<String>();
        list.forEach(a->list1.add(a.getId().toString()));
        if(ObjectUtils.isNotEmpty(list)) {
            Map<Object, Boolean> member = stringRedisTemplate.opsForSet()
                    .isMember(CHILDREN_COMMENT_LIKES + c.getId(), list1.toArray());
            if (ObjectUtils.isNotEmpty(member)) {
                for (SlChComment sl : list) {
                    if (BooleanUtils.isTrue(member.get(sl.getId().toString()))) {
                        sl.setLiked(true);
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public Result consumerDeleteReplyComment(Integer replyId,Integer pid) {
        if(removeById(replyId)){
            stringRedisTemplate.opsForZSet().incrementScore(COMMENT_REPLY_COUNT,pid.toString(),-1);
            return Result.ok("删除成功");
        }
        return Result.fail("删除失败");
    }
}
