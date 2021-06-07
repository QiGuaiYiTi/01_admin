package com.charles.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author Charles
 * @since 2021-06-04
 */
@TableName("sys_role_menu")
@Data
public class RoleMenu {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long roleId;

    private Long menuId;

    @Override
    public String toString() {
        return "RoleMenu{" +
        "roleId=" + roleId +
        ", menuId=" + menuId +
        "}";
    }
}
