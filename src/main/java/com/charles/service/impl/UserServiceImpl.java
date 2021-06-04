package com.charles.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.charles.entity.Menu;
import com.charles.entity.Role;
import com.charles.entity.User;
import com.charles.dao.UserMapper;
import com.charles.service.MenuService;
import com.charles.service.RoleService;
import com.charles.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charles.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Charles
 * @since 2021-06-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;
    @Autowired
    RedisUtil redisUtil;

    /**
     * 根据用户id获取用户权限
     * @param userId
     * @return
     */
    @Override
    public String getUserAuthorityInfo(Long userId) {
        User user = userMapper.selectById(userId);
        String authority = "";
        //先从缓存中读取权限列表，读不到再查询数据库
        if (redisUtil.hasKey("GrantedAuthority:" + user.getUsername())) {
            authority = (String) redisUtil.get("GrantedAuthority:" + user.getUsername());
        }else {
            //获取角色列表
            List<Role> roles = roleService.list(
                    new QueryWrapper<Role>()
                            .inSql("id", "SELECT role_id FROM sys_user_role WHERE user_id = #{user_id}"));
            if (roles.size() > 0) {
                String roleCodes = roles.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }
            // 获取菜单操作编码
            List<Long> menuIds = userMapper.getNavMenuIds(userId);
            if (menuIds.size() > 0) {
                List<Menu> menus = menuService.listByIds(menuIds);
                String menuPerms = menus.stream().map(m -> m.getPerms()).collect(Collectors.joining(","));
                authority = authority.concat(menuPerms);
                //将权限列表放入redis缓存中
                redisUtil.set("GrantedAuthority:" + user.getUsername(), authority, 60 * 60);
            }
        }
        return authority;
    }

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Override
    public User getByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username",username));
    }

    /**
     * 根据用户名清除缓存
     * @param username
     */
    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtil.del("GrantedAuthority:" + username);
    }

    /**
     * 根据角色id清除缓存
     * @param roleId
     */
    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {
        List<User> users = this.list(new QueryWrapper<User>()
                .inSql("id", "select user_id from sys_user_role where role_id = " + roleId));

        users.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }

    /**
     * 根据权限id清除缓存
     * @param menuId
     */
    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        List<User> users = userMapper.listByMenuId(menuId);
        users.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }


}
