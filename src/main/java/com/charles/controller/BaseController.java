package com.charles.controller;

import com.charles.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Description :
 *
 * @author : Charles
 * @created : 2021/6/4
 */
public class BaseController {

    @Autowired
    HttpServletRequest req;
    @Autowired
    RedisUtil redisUtil;

}
