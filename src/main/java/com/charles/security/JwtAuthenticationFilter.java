package com.charles.security;

import cn.hutool.core.util.StrUtil;
import com.charles.entity.User;
import com.charles.service.UserService;
import com.charles.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description :JWT认证过滤器
 *
 * @author : Charles
 * @created : 2021/6/4
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    UserService userService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    /**
     * 认证逻辑
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain chain) throws ServletException, IOException {
        //获取请求头中的jwt
        String jwt = request.getHeader(jwtUtil.getHeader());
        //若为空，则交给后续的过滤器处理
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }
        //获取jwt中的载荷信息
        Claims claim = jwtUtil.getClaimByToken(jwt);
        //若为空，抛出异常
        if (claim == null) {
            throw new JwtException("token 异常");
        }
        //若jwt国企，抛出异常处理
        if (jwtUtil.isTokenExpired(claim)) {
            throw new JwtException("token已过期");
        }
        //获取用户名
        String username = claim.getSubject();
        // 获取用户的权限等信息
        User user = userService.getByUsername(username);
        //放入认证token中
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(username, null, userDetailsServiceImpl.getUserAuthority(user.getId()));
        SecurityContextHolder.getContext().setAuthentication(token);
        //放行
        chain.doFilter(request, response);
    }
}
