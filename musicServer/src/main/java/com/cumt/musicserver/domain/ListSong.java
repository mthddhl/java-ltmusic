package com.cumt.musicserver.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class ListSong implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer songId;

    private Integer songListId;

    @TableField(exist = false)
    private String pic;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private String introduction;

    @TableField(exist = false)
    private String url;

    @TableField(exist = false)
    private String singer;

    @TableField(exist = false)
    private Integer singerId;

    @TableField(exist = false)
    private String lyric;

    @TableField(exist = false)
    private Boolean liked=false;

    @TableField(exist = false)
    private Boolean isVip;

    public ListSong() {
    }

    public ListSong(Integer id, Integer songId, Integer songListId) {
        this.id=id;
        this.songId = songId;
        this.songListId = songListId;
    }
}
