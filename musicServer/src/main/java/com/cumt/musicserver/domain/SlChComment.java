package com.cumt.musicserver.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class SlChComment implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String content;

    private Integer userId;

    private LocalDateTime createTime;

    private Integer pid;

    private Integer originId;

    private Integer songListId;


    @TableField(exist = false)
    private String userName;

    private Integer likes;

    @TableField(exist = false)
    private String userAvator;

    @TableField(exist = false)
    private String originName;

    @TableField(exist = false)
    private Boolean liked=false;


}
