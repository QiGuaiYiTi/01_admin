package com.charles.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.charles.commons.Const;
import com.charles.commons.Result;
import com.charles.commons.dto.PasswordDto;
import com.charles.entity.Role;
import com.charles.entity.User;
import com.charles.entity.UserRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Charles
 * @since 2021-06-04
 */
@Api(tags = "用户模块相关接口")
@RestController
@RequestMapping("sys/user")
public class UserController extends BaseController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:user:list')")
    @ApiOperation(value="获取用户详情信息")
    public Result info(@PathVariable("id") Long id) {

        User user = userService.getById(id);
        Assert.notNull(user, "找不到该管理员");

        List<Role> roles = roleService.listRolesByUserId(id);

        user.setRoles(roles);
        return Result.success(user);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    @ApiOperation("获取用户列表")
    public Result list(String username) {

        Page pageData = userService.page(getPage(), new QueryWrapper<User>()
                .like(StrUtil.isNotBlank(username), "username", username));

//        pageData.getRecords().forEach(user -> {
//
//            user.setRoles(roleService.listRolesByUserId(u.getId()));
//        });
        List<User> users = pageData.getRecords();
        users.stream().forEach(user ->{
            user.setRoles(roleService.listRolesByUserId(user.getId()));
        });

        return Result.success(pageData);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:user:save')")
    @ApiOperation("新增用户")
    public Result save(@Validated @RequestBody User user) {

        user.setCreated(LocalDateTime.now());
        user.setStatu(Const.STATUS_ON);

        // 默认密码
        String password = passwordEncoder.encode(Const.DEFAULT_PASSWORD);
        user.setPassword(password);

        // 默认头像
        user.setAvatar(Const.DEFAULT_AVATAR);

        userService.save(user);
        return Result.success(user);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:user:update')")
    @ApiOperation(value = "修改用户信息")
    public Result update(@Validated @RequestBody User user) {

        user.setUpdated(LocalDateTime.now());

        userService.updateById(user);
        return Result.success(user);
    }

    @Transactional
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @ApiOperation(value="批量删除用户")
    public Result delete(@RequestBody Long[] ids) {

        userService.removeByIds(Arrays.asList(ids));
        userRoleService.remove(new QueryWrapper<UserRole>().in("user_id", ids));

        return Result.success("");
    }

    @Transactional
    @PostMapping("/role/{userId}")
    @PreAuthorize("hasAuthority('sys:user:role')")
    @ApiOperation(value = "分配角色")
    public Result rolePerm(@PathVariable("userId") Long userId, @RequestBody Long[] roleIds) {

        List<UserRole> userRoles = new ArrayList<>();

        Arrays.stream(roleIds).forEach(r -> {
            UserRole userRole = new UserRole();
            userRole.setRoleId(r);
            userRole.setUserId(userId);

            userRoles.add(userRole);
        });

        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", userId));
        userRoleService.saveBatch(userRoles);

        // 删除缓存
        User user = userService.getById(userId);
        userService.clearUserAuthorityInfo(user.getUsername());

        return Result.success("");
    }

    @PostMapping("/repass")
    @PreAuthorize("hasAuthority('sys:user:repass')")
    @ApiOperation(value="重置密码")
    public Result repass(@RequestBody Long userId) {

        User user = userService.getById(userId);

        user.setPassword(passwordEncoder.encode(Const.DEFAULT_PASSWORD));
        user.setUpdated(LocalDateTime.now());

        userService.updateById(user);
        return Result.success("");
    }

    @PostMapping("/updatePass")
    @ApiOperation(value="修改密码")
    public Result updatePass(@Validated @RequestBody PasswordDto passwordDto, Principal principal) {

        User user = userService.getByUsername(principal.getName());

        boolean matches = passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPassword());
        if (!matches) {
            return Result.fail("旧密码不正确");
        }

        user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        user.setUpdated(LocalDateTime.now());

        userService.updateById(user);
        return Result.success("");
    }

}

