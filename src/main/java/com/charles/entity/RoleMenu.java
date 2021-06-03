package com.charles.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * <p>
 * 
 * </p>
 *
 * @author Charles
 * @since 2021-06-04
 */
@TableName("sys_role_menu")
public class RoleMenu {

    private static final long serialVersionUID = 1L;

    private Long roleId;

    private Long menuId;


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "RoleMenu{" +
        "roleId=" + roleId +
        ", menuId=" + menuId +
        "}";
    }
}
