package com.cumt.musicserver.service.impl;

import com.cumt.musicserver.domain.Comment;
import com.cumt.musicserver.dao.CommentDao;
import com.cumt.musicserver.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements ICommentService {

}
