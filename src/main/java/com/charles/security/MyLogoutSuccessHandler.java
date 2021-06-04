package com.charles.security;

import cn.hutool.json.JSONUtil;
import com.charles.commons.Result;
import com.charles.utils.JwtUtil;
import com.charles.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description :
 *
 * @author : Charles
 * @created : 2021/6/4
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        //退出
        if(authentication!=null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        //清空jwt
        response.setContentType("application/json;charset=utf-8");
        response.setHeader(jwtUtil.getHeader(),"");
        //写出到浏览器
        ServletOutputStream outputStream = response.getOutputStream();
        Result result = Result.success("");
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
