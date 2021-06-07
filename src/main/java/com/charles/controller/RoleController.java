package com.charles.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.charles.commons.Const;
import com.charles.commons.Result;
import com.charles.entity.Role;
import com.charles.entity.RoleMenu;
import com.charles.entity.UserRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Charles
 * @since 2021-06-04
 */
@RestController
@RequestMapping("sys/role")
@Api(tags = "角色模块相关接口")
public class RoleController extends BaseController {

    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/info/{id}")
    @ApiOperation(value = "查询角色详情" )
    public Result info(@PathVariable("id") Long id) {

        Role role = roleService.getById(id);

        // 获取角色相关联的菜单id
        List<RoleMenu> roleMenus = roleMenuService.list(new QueryWrapper<RoleMenu>().eq("role_id", id));
        List<Long> menuIds = roleMenus.stream().map(p -> p.getMenuId()).collect(Collectors.toList());

        role.setMenuIds(menuIds);
        return Result.success(role);
    }

    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/list")
    @ApiOperation(value = "分页查询角色列表")
    public Result list(String name) {

        Page pageData = roleService.page(getPage(),
                new QueryWrapper<Role>()
                        .like(StrUtil.isNotBlank(name), "name", name)
        );

        return Result.success(pageData);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:role:save')")
    @ApiOperation(value="新增角色")
    public Result save(@Validated @RequestBody Role role) {

        role.setCreated(LocalDateTime.now());
        role.setStatu(Const.STATUS_ON);

        roleService.save(role);
        return Result.success(role);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    @ApiOperation(value="修改角色")
    public Result update(@Validated @RequestBody Role role) {

        role.setUpdated(LocalDateTime.now());

        roleService.updateById(role);

        // 更新缓存
        userService.clearUserAuthorityInfoByRoleId(role.getId());

        return Result.success(role);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @Transactional
    @ApiOperation(value="批量删除")
    public Result info(@RequestBody Long[] ids) {

        roleService.removeByIds(Arrays.asList(ids));

        // 删除中间表
        userRoleService.remove(new QueryWrapper<UserRole>().in("role_id", ids));
        roleMenuService.remove(new QueryWrapper<RoleMenu>().in("role_id", ids));

        // 缓存同步删除
        Arrays.stream(ids).forEach(id -> {
            // 更新缓存
            userService.clearUserAuthorityInfoByRoleId(id);
        });

        return Result.success("");
    }

    @Transactional
    @PostMapping("/perm/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:perm')")
    @ApiOperation(value="分配权限")
    public Result info(@PathVariable("roleId") Long roleId, @RequestBody Long[] menuIds) {

        List<RoleMenu> sysRoleMenus = new ArrayList<>();

        Arrays.stream(menuIds).forEach(menuId -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);

            sysRoleMenus.add(roleMenu);
        });

        // 先删除原来的记录，再保存新的
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id", roleId));
        roleMenuService.saveBatch(sysRoleMenus);

        // 删除缓存
        userService.clearUserAuthorityInfoByRoleId(roleId);

        return Result.success(menuIds);
    }

}

