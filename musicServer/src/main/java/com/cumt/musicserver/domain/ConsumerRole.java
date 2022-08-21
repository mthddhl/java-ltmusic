package com.cumt.musicserver.domain;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2022-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConsumerRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer consumerId;

    private Integer roleId;

    public ConsumerRole(Integer consumerId, Integer roleId) {
        this.consumerId = consumerId;
        this.roleId = roleId;
    }
}
