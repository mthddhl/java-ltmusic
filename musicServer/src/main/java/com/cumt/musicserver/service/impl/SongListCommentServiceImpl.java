package com.cumt.musicserver.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.musicserver.dao.SongListCommentDao;
import com.cumt.musicserver.domain.Comment;
import com.cumt.musicserver.domain.Consumer;
import com.cumt.musicserver.domain.SongListComment;
import com.cumt.musicserver.service.ISongListCommentService;
import com.cumt.musicserver.util.Result;
import com.cumt.musicserver.util.StaticString;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SongListCommentServiceImpl extends MPJBaseServiceImpl<SongListCommentDao, SongListComment> implements ISongListCommentService {
    @Resource
    private SongListCommentDao songListCommentDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result subComments(SongListComment s) {
        s.setCreatedTime(LocalDateTime.now());
        Consumer o= (Consumer) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        s.setConsumerId(o.getId());
        s.setLikes(0);
        boolean save = save(s);
        if(save){
            if(BooleanUtils.isFalse(stringRedisTemplate.hasKey(StaticString.SONGLIST_COMMENT_COUNT + s.getSongListId().toString()))){
                synchronized (s.getId().toString().intern()){
                    if(BooleanUtils.isFalse(stringRedisTemplate.hasKey(StaticString.SONGLIST_COMMENT_COUNT + s.getSongListId().toString()))){
                        Integer count = lambdaQuery().eq(SongListComment::getSongListId, s.getSongListId()).count();
                        stringRedisTemplate.opsForValue().set(StaticString.SONGLIST_COMMENT_COUNT + s.getSongListId().toString(),count.toString());
                    }else {
                        stringRedisTemplate.opsForValue().increment(StaticString.SONGLIST_COMMENT_COUNT + s.getSongListId().toString());
                    }
                }
            }else {
                stringRedisTemplate.opsForValue().increment(StaticString.SONGLIST_COMMENT_COUNT + s.getSongListId().toString());
            }
            return Result.ok();
        }
        return Result.fail("评论失败");
    }

    @Override
    public Result getCommentsBySongListId(Integer currentPage, Integer pageSize, Integer songListId,String type) {
        Object o= SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        MPJLambdaWrapper<SongListComment> songListCommentMPJLambdaWrapper = new MPJLambdaWrapper<SongListComment>()
                .select(SongListComment::getId, SongListComment::getLikes, SongListComment::getContent,
                        SongListComment::getCreatedTime,SongListComment::getConsumerId,SongListComment::getConsumerId)
                .selectAs(Consumer::getUsername, SongListComment::getConsumerName)
                .selectAs(Consumer::getAvator, SongListComment::getAvator)
                .leftJoin(Consumer.class, Consumer::getId, SongListComment::getConsumerId);
        List<SongListComment> list;
        if("hot".equals(type)){
            list=selectJoinListPage(new Page<>(currentPage, pageSize)
                    , SongListComment.class, songListCommentMPJLambdaWrapper
                            .orderByDesc(SongListComment::getLikes)).getRecords();
        }else {
            list=selectJoinListPage(new Page<>(currentPage, pageSize)
                    , SongListComment.class, songListCommentMPJLambdaWrapper
                            .orderByAsc(SongListComment::getCreatedTime)).getRecords();
        }
        if(!(o instanceof Consumer)){
            return Result.ok(list);
        }
        Consumer c=(Consumer) o;
        list.forEach(each->{
            Boolean member = stringRedisTemplate.opsForSet().isMember(StaticString.COMMENT_LIKES + each.getId(), c.getId().toString());
            if(BooleanUtils.isTrue(member)){
                each.setLiked(true);
            }
        });

        return Result.ok(list);
    }

    @Override
    public Result consumerLikeComment(Integer commentId, Boolean liked) {
        Consumer o= (Consumer) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        if(BooleanUtils.isTrue(liked)) {
            boolean update = lambdaUpdate().eq(SongListComment::getId, commentId).setSql("likes=likes-1").update();
            if (update) {
                stringRedisTemplate.opsForSet().remove(StaticString.COMMENT_LIKES + commentId,o.getId().toString());
            }else {
                return Result.fail("点赞失败,尝试重新登陆");
            }
        }else {
            boolean update = lambdaUpdate().eq(SongListComment::getId, commentId).setSql("likes=likes+1").update();
            if(update){
                stringRedisTemplate.opsForSet().add(StaticString.COMMENT_LIKES + commentId,o.getId().toString());
            }else {
                return Result.fail("点赞失败,尝试重新登陆");
            }
        }
        return Result.ok();
    }

    @Override
    @Transactional
    public Result consumerDeleteComment(Integer commentId,Integer songListId) {
        Consumer o= (Consumer) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        if(removeById(commentId)){
            stringRedisTemplate.opsForValue().decrement(StaticString.SONGLIST_COMMENT_COUNT + songListId);
            stringRedisTemplate.opsForSet().remove(StaticString.COMMENT_LIKES + commentId,o.getId().toString());
            return Result.ok("删除成功");
        }
        return Result.ok("删除失败");
    }

}
