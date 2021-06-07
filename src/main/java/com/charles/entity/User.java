package com.charles.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 
 * </p>
 *
 * @author Charles
 * @since 2021-06-04
 */
@TableName("sys_user")
@Data
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @NotBlank(message = "用户名不能为空")
    private String username;

    private String password;

    private String avatar;
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    private String city;

    private LocalDateTime lastLogin;
    @TableField(exist = false)
    private List<Role> roles = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
        "username=" + username +
        ", password=" + password +
        ", avatar=" + avatar +
        ", email=" + email +
        ", city=" + city +
        ", lastLogin=" + lastLogin +
        "}";
    }
}
