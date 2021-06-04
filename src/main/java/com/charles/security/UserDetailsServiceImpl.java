package com.charles.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.charles.dao.UserMapper;
import com.charles.entity.AccountUser;
import com.charles.entity.User;
import com.charles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description :登录逻辑
 *
 * @author : Charles
 * @created : 2021/6/4
 */
@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //获取用户
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        //为空，抛出异常
        if (user==null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        //获取密码
        String password = user.getPassword();
        //返回认证对象
        UserDetails accountUser = new AccountUser(user.getUsername(),
                 passwordEncoder.encode(user.getPassword()),
                 this.getUserAuthority(user.getId()),
                true,
                true,
                true,
                true);
        return accountUser;
    }
    /**
     * 获取用户权限信息（角色、菜单权限）
     * @param userId
     * @return
     */
    public List<GrantedAuthority> getUserAuthority(Long userId){
        // 角色(ROLE_admin)、菜单操作权限 sys:user:list
        String authority = userService.getUserAuthorityInfo(userId);  // ROLE_admin,ROLE_normal,sys:user:list,....
        //返回权限列表
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
