package com.charles.service.impl;

import com.charles.entity.User;
import com.charles.dao.UserMapper;
import com.charles.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
