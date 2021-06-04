package com.charles.security;

import cn.hutool.json.JSONUtil;
import com.charles.commons.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
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
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
            //设置响应头
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ServletOutputStream outputStream = response.getOutputStream();
            //返回结果
            Result result = Result.fail("请先登录");
            outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
        }

}
