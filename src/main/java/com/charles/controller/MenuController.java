package com.charles.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.charles.commons.Result;
import com.charles.commons.dto.MenuDto;
import com.charles.dao.UserMapper;
import com.charles.entity.Menu;
import com.charles.entity.RoleMenu;
import com.charles.entity.User;
import com.charles.service.RoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Charles
 * @since 2021-06-04
 */
@Controller
@RequestMapping("/sys/menu")
@Api(tags = "菜单模块相关接口")
public class MenuController extends BaseController {

    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMenuService roleMenuService;

    @GetMapping("/nav")
    @ApiOperation(value = "获取用户菜单导航栏")
    public Result nav(Principal principal){
        //获取当前登录用户用户名
        String username = principal.getName();
        //获取当前用户信息
        User currentUser = userService.getByUsername(username);
        //获取用户权限
        String userAuthorityInfo = userService.getUserAuthorityInfo(currentUser.getId());
        String[] authorityInfoArray = StringUtils.tokenizeToStringArray(userAuthorityInfo, ",");
        //获取菜单导航
        List<MenuDto> navs = menuService.getCurrentUserNav();
        return Result.success(MapUtil.builder()
                .put("authoritys", authorityInfoArray)
                .put("nav", navs)
                .map()
        );
    }

    /**
     * 用户当前用户的菜单和权限信息
     * @param principal
     * @return
     */

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    @ApiOperation(value="获取菜单详细信息")
    public Result info(@PathVariable(name = "id") Long id) {
        return Result.success(menuService.getById(id));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    @ApiOperation(value = "获取菜单列表")
    public Result list() {

        List<Menu> menus = menuService.tree();
        return Result.success(menus);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:menu:save')")
    @ApiOperation(value="新增菜单信息")
    public Result save(@Validated @RequestBody Menu menu) {

        menu.setCreated(LocalDateTime.now());

        menuService.save(menu);
        return Result.success(menu);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    @ApiOperation(value="修改菜单信息")
    public Result update(@Validated @RequestBody Menu menu) {

        menu.setUpdated(LocalDateTime.now());

        menuService.updateById(menu);

        // 清除所有与该菜单相关的权限缓存
        userService.clearUserAuthorityInfoByMenuId(menu.getId());
        return Result.success(menu);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @ApiOperation(value="删除菜单")
    public Result delete(@PathVariable("id") Long id) {
        int count = menuService.count(new QueryWrapper<Menu>().eq("parent_id", id));
        if (count > 0) {
            return Result.fail("请先删除子菜单");
        }

        // 清除所有与该菜单相关的权限缓存
        userService.clearUserAuthorityInfoByMenuId(id);

        menuService.removeById(id);

        // 同步删除中间关联表
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("menu_id", id));
        return Result.success("");
    }
}

