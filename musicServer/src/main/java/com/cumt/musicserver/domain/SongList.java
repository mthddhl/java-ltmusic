package com.cumt.musicserver.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SongList implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private String pic;

    private String introduction;

    private String style;

    @TableLogic(value = "1",delval = "0")
    private Boolean logicDelete;

    @TableField(exist = false)
    private Integer likes;

    @TableField(exist = false)
    private Integer count;

    private LocalDate createdTime;

    private Integer createdConId;
    @TableField(exist = false)
    private String isLike;

    @TableField(exist = false)
    private Integer commentCount;

    @TableField(exist = false)
    private boolean isPlay;

    @TableField(exist = false)
    private String createdName;
    @TableField(exist = false)
    private String createdAvator;

}
