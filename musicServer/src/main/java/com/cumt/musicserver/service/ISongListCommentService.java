package com.cumt.musicserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cumt.musicserver.domain.SongListComment;
import com.cumt.musicserver.util.Result;
import com.github.yulichang.base.MPJBaseService;

public interface ISongListCommentService extends MPJBaseService<SongListComment> {
    Result subComments(SongListComment s);

    Result getCommentsBySongListId(Integer currentPage, Integer pageSize, Integer songListId,String type);

    Result consumerLikeComment(Integer commentId, Boolean liked);

    Result consumerDeleteComment(Integer commentId,Integer songListId);
}
