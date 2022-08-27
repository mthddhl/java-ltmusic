package com.cumt.musicserver.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class SongListComment {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer consumerId;
    @TableField(exist = false)
    private String consumerName;

    private LocalDateTime createdTime;

    private Integer songListId;

    private String content;

    private Integer likes;

    @TableLogic(value = "1",delval = "0")
    private Boolean deleteLogic;
    @TableField(exist = false)
    private Boolean liked=false;
    @TableField(exist = false)
    private String avator;

    @TableField(exist = false)
    private Integer replyCount=0;

    @TableField(exist = false)
    private List<SlChComment> replyList=new ArrayList<>();

    @TableField(exist = false)
    private Integer currentPage=0;
}
