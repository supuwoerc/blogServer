package cn.lookup.sanye.controller;

import cn.lookup.sanye.common.dto.LoginAndRegisterUserDto;
import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/16 15:04
 * @Desc:注册
 **/
@RestController
public class RegisterController {
    @Autowired
    SysUserService sysUserService;

    @PutMapping("/register")
    public Result register(@RequestBody @Validated LoginAndRegisterUserDto registerUserDto) throws Exception {
        sysUserService.register(registerUserDto.getUsername(), registerUserDto.getPassword(), registerUserDto.getCodeKey(), registerUserDto.getCode());
        return Result.success("注册成功");
    }
}
