package cn.lookup.sanye.controller;

import cn.lookup.sanye.common.dto.SysUserDto;
import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.pojo.User;
import cn.lookup.sanye.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping("/register")
    public Result register(@RequestBody @Validated SysUserDto userDto){
        return sysUserService.register(userDto.getUsername(),userDto.getPassword(),userDto.getCodeKey(),userDto.getCode());
    }
}
