package com.cumt.musicserver.service;

import com.cumt.musicserver.domain.SlChComment;
import com.cumt.musicserver.util.Result;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

public interface SlChCommentService extends MPJBaseService<SlChComment> {

    Result replyComment(SlChComment slChComment);

    Result consumerChildrenLikeComment(Integer commentId, Boolean liked);

    void setLikeCount(List<SlChComment> list);

    Result consumerDeleteReplyComment(Integer replyId,Integer pid);
}
