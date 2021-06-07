package com.charles.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.charles.service.*;
import com.charles.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Description :
 *
 * @author : Charles
 * @created : 2021/6/4
 */
public class BaseController {

    @Autowired
    HttpServletRequest req;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleMenuService roleMenuService;
    /**
     * 获取页面
     * @return
     */
    public Page getPage() {
        int current = ServletRequestUtils.getIntParameter(req, "cuurent", 1);
        int size = ServletRequestUtils.getIntParameter(req, "size", 10);

        return new Page(current, size);
    }

}
