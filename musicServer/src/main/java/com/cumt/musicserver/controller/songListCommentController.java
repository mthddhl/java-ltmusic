package com.cumt.musicserver.controller;

import com.cumt.musicserver.domain.SlChComment;
import com.cumt.musicserver.domain.SongListComment;
import com.cumt.musicserver.service.ISongListCommentService;
import com.cumt.musicserver.service.SlChCommentService;
import com.cumt.musicserver.util.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
@RestController
@RequestMapping("/songListComment")
public class songListCommentController {
    @Resource
    private ISongListCommentService songListCommentService;


    @Resource
    private SlChCommentService slChCommentService;

    @PostMapping("/subComments")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result subComments(SongListComment s){
        return songListCommentService.subComments(s);
    }

    @GetMapping("/getCommentsBySongListId")
    public Result getCommentsBySongListId(Integer currentPage,Integer pageSize,Integer songListId,String type){
        if(ObjectUtils.isEmpty(songListId)) return Result.ok(new ArrayList<>());
        return songListCommentService.getCommentsBySongListId(currentPage,pageSize,songListId,type);
    }
    @GetMapping("/getCommentCount")
    public Result getCommentCount(Integer songListId){
        Integer count = songListCommentService.lambdaQuery().eq(SongListComment::getSongListId, songListId).count();
        return Result.ok(count);
    }

    @GetMapping("/likeOfConsumer")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result likeOfConsumer(Integer commentId,Boolean liked){
        return songListCommentService.consumerLikeComment(commentId,liked);
    }
    @GetMapping("/consumerDeleteComment")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerDeleteComment(Integer commentId,Integer songListId){
        return songListCommentService.consumerDeleteComment(commentId,songListId);
    }
    @PostMapping("/replyComment")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result replyComment(SlChComment slChComment) {
        return slChCommentService.replyComment(slChComment);
    }

    @GetMapping("/getChildrenCommentBySongListId")
    public Result getChildrenCommentBySongListId(Integer songListId,Integer currentPage){
        return songListCommentService.getChildrenCommentBySongListId(songListId,currentPage);
    }

    @GetMapping("/consumerChildrenLikeComment")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerChildrenLikeComment(Integer commentId,Boolean liked){
        return slChCommentService.consumerChildrenLikeComment(commentId,liked);
    }
    @GetMapping("/consumerDeleteReplyComment")
    @PreAuthorize("hasAnyAuthority('admin','consumer')")
    public Result consumerDeleteReplyComment(Integer replyId,Integer pid){
        return slChCommentService.consumerDeleteReplyComment(replyId,pid);
    }

}
