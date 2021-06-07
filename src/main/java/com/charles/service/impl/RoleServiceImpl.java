package com.charles.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.charles.entity.Role;
import com.charles.dao.RoleMapper;
import com.charles.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Charles
 * @since 2021-06-04
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

        @Override
        public List<Role> listRolesByUserId(Long userId) {

            List<Role> roles = this.list(new QueryWrapper<Role>()
                    .inSql("id", "select role_id from sys_user_role where user_id = " + userId));

            return roles;
        }

}
