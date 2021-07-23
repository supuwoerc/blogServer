package cn.lookup.sanye.controller;


import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.pojo.User;
import cn.lookup.sanye.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-07-15
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SysUserService sysUserService;
    @GetMapping("/info")
    public Result getUserInfo(){
        //获取当前上下文中的用户
        try {
            SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = sysUserService.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername,sysUserDetails.getUsername()));
            user.setPassword(null);
            return Result.success(user);
        } catch (Exception e) {
            return Result.fail("登录过期",null);
        }
    }
}
