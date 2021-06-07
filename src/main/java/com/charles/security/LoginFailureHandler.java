package com.charles.security;

import cn.hutool.json.JSONUtil;
import com.charles.commons.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description :登录失败处理器
 *
 * @author : Charles
 * @created : 2021/6/4
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
        //登录失败，返回异常信息
        Result failInfo = Result.fail(e.getMessage());
        response.setContentType("application/json;charset=utf-8");
        response.getOutputStream().write(JSONUtil.toJsonStr(failInfo).getBytes("utf-8"));
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
