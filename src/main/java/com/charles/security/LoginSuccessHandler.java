package com.charles.security;

import cn.hutool.json.JSONUtil;
import com.charles.commons.Result;
import com.charles.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description :登录成功处理器
 *
 * @author : Charles
 * @created : 2021/6/4
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //获取用户名
        String username = authentication.getName();
        //生成jwt
        String jwt = jwtUtil.generateToken(username);
        //设置响应头将jwt存入头信息中
        response.setContentType("application/json;charset=utf-8");
        response.setHeader(jwtUtil.getHeader(),jwt);
        //写出到浏览器
        ServletOutputStream outputStream = response.getOutputStream();
        Result result = Result.success("");
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
