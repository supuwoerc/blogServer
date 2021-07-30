package cn.lookup.sanye.common.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/16 15:10
 * @Desc:登录、注册接口的dto
 **/
@Data
public class LoginAndRegisterUserDto {
    /**
     * 账户名
     */
    @NotBlank(message = "用户名不能为空")
    @Email(message = "邮箱格式错误")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
    /**
     * 验证码code
     */
    @NotBlank(message = "验证码不能为空")
    private String code;
    /**
     * 验证码codeKey
     */
    @NotBlank(message = "验证码key不能为空")
    private String codeKey;
}
