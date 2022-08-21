package com.cumt.musicserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cumt.musicserver.domain.SongListComment;
import com.github.yulichang.base.MPJBaseMapper;

import java.util.List;

public interface SongListCommentDao extends MPJBaseMapper<SongListComment> {
    List<SongListComment> getCommentsBySongListId(Integer currentPage, Integer pageSize, Integer songListId);
}
