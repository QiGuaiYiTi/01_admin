package com.charles.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 
 * </p>
 *
 * @author Charles
 * @since 2021-06-04
 */
@TableName("sys_menu")
public class Menu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父菜单ID，一级菜单为0
     */
    private Long parentId;

    private String name;

    /**
     * 菜单URL
     */
    private String path;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;

    private String component;

    /**
     * 类型     0：目录   1：菜单   2：按钮
     */
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    @TableField("orderNum")
    private Integer ordernum;


    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(Integer ordernum) {
        this.ordernum = ordernum;
    }

    @Override
    public String toString() {
        return "Menu{" +
        "parentId=" + parentId +
        ", name=" + name +
        ", path=" + path +
        ", perms=" + perms +
        ", component=" + component +
        ", type=" + type +
        ", icon=" + icon +
        ", ordernum=" + ordernum +
        "}";
    }
}
