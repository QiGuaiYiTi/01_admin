package com.charles.commons;

import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;

/**
 * Description :验证码异常相关
 *
 * @author : Charles
 * @created : 2021/6/4
 */
public class CaptchaException extends AuthenticationException {

    public CaptchaException(String msg) {
        super(msg);
    }
}
