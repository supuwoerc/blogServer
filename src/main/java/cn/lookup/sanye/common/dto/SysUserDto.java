package cn.lookup.sanye.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/16 15:10
 * @Desc:desc
 **/
@Data
public class SysUserDto {
    /**
     * 账户名
     */
    @NotBlank(message = "用户名不能为空")
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
