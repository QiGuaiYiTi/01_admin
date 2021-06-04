package com.charles.service;

import com.charles.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Charles
 * @since 2021-06-04
 */
public interface UserService extends IService<User> {
    /**
     * 根据用户id获取用户权限
     * @param userId
     * @return
     */
    String getUserAuthorityInfo(Long userId);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User getByUsername(String username);

    /**
     * 根据用户名清除缓存
     * @param username
     */
    void clearUserAuthorityInfo(String username);

    /**
     * 根据角色id清除缓存
     * @param roleId
     */
    void clearUserAuthorityInfoByRoleId(Long roleId);

    /**
     * 根据权限id清除缓存
     * @param menuId
     */
    void clearUserAuthorityInfoByMenuId(Long menuId);
}
