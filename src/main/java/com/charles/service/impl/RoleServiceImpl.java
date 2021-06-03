package com.charles.service.impl;

import com.charles.entity.Role;
import com.charles.dao.RoleMapper;
import com.charles.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
