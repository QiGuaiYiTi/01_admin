package com.charles.commons.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Description :
 *
 * @author : Charles
 * @created : 2021/6/6
 */
@Data
public class PasswordDto implements Serializable {
    @NotBlank(message = "新密码不能为空")
    private String password;
    @NotBlank(message = "旧密码不能为空")
    private String currentPassword;

}
