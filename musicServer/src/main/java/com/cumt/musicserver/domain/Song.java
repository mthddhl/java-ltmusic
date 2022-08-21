package com.cumt.musicserver.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
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
public class Song implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer singerId;

    @TableField(exist = false)
    private String singerName;

    private String name;

    private String introduction;

    /**
     * 发行时间
     */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String pic;

    private String lyric;

    private String url;

    private Boolean isVip;

    @TableLogic(value = "1",delval = "0")
    private Boolean deleteLogic;

    @TableField(exist = false)
    private Boolean liked=false;
    @TableField(exist = false)
    private Boolean isDownload=false;

    @TableField(exist = false)
    private Double isDownloadNum=0.0;

    private String songTime;

}
