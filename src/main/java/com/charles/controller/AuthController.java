package com.charles.controller;

import cn.hutool.core.map.MapUtil;
import com.charles.commons.Const;
import com.charles.commons.Result;
import com.charles.entity.User;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

/**
 * Description :获取验证码
 *
 * @author : Charles
 * @created : 2021/6/4
 */
@RestController
@Api(tags = "用户登录认证相关接口")
public class AuthController extends BaseController{

    @Autowired
    Producer producer;

    @GetMapping("/captcha")
    @ApiOperation(value = "获取验证码接口")
    public Result getCaptcha() throws IOException {
        //生成随机码
        String key = UUID.randomUUID().toString();
        //生成验证码
        String code = producer.createText();
        System.out.println(code);
        //生产验证码图片并写出
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str + encoder.encode(outputStream.toByteArray());
        //将随机码和验证码存入redis缓存
        redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120);
        //返回结果到浏览器
        return Result.success(
                MapUtil.builder()
                        .put("token", key)
                        .put("captchaImg", base64Img)
                        .build()

        );
    }

    /**
     * 获取用户信息接口
     * @param principal
     * @return
     */
    @GetMapping("/sys/userInfo")
    @ApiOperation(value = "获取用户名，用户头像")
    public Result userInfo(Principal principal) {

        User user = userService.getByUsername(principal.getName());

        return Result.success(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .put("created", user.getCreated())
                .map()
        );
    }
}
