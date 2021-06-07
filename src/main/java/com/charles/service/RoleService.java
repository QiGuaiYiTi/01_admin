package com.charles.service;

import com.charles.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Charles
 * @since 2021-06-04
 */
public interface RoleService extends IService<Role> {

    List<Role> listRolesByUserId(Long id);
}
