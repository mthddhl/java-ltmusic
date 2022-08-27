package com.cumt.musicserver.dao;

import com.cumt.musicserver.domain.SlChComment;
import com.github.yulichang.base.MPJBaseMapper;

import java.util.List;

public interface SlChCommentDao extends MPJBaseMapper<SlChComment> {

    List<SlChComment> getChildrenCommentBySongListId(Integer songListId, Integer currentPage);
}
