package com.cumt.musicpay.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
public class Consumer implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private Integer sex;

    private String phoneNum;

    private String email;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private String introduction;

    private String location;

    private LocalDateTime vipExpireTime;

    private String avator;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
    @TableLogic(value = "1",delval = "0")
    private Boolean deleteLogic;

    @TableField(exist = false)
    private List<String> roles;
    @TableField(exist = false)
    private Boolean enabled;
    @TableField(exist = false)
    private Boolean credentialsNonExpired;
    @TableField(exist = false)
    private Boolean accountNonExpired;
    @TableField(exist = false)
    private Boolean accountNonLocked;
    @TableField(exist = false)
    private List<Map<String,String>> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list=new ArrayList<>();
        if(roles!=null){
            roles.forEach(each->{
                list.add(new SimpleGrantedAuthority(each));
            });
        }
        return list;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
