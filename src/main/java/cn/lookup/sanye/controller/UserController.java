package cn.lookup.sanye.controller;


import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.pojo.Role;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.pojo.User;
import cn.lookup.sanye.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
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

    /**
     * 获取当前登录人信息
     * @return
     */
    @GetMapping("/info")
    public Result getUserInfo() {
        //获取当前上下文中的用户
        try {
            SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = sysUserService.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, sysUserDetails.getUsername()));
            user.setPassword(null);
            return Result.success(user);
        } catch (Exception e) {
            return Result.fail("登录过期", null);
        }
    }

    /**
     * 获取当前登录人角色
     * @return
     */
    @GetMapping("/role")
    public Result getUserRole() {
        //获取当前上下文中的用户
        try {
            SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Role> roles = sysUserService.findRolesByUserName(sysUserDetails.getUsername());
            return Result.success(roles);
        } catch (Exception e) {
            return Result.fail("登录过期", null);
        }
    }

    /**
     * 激活账户
     * @param username
     * @param code
     * @param modelAndView
     * @return
     */
    @GetMapping("/activeUser/{username}/{activeCode}")
    public ModelAndView activeUser(@PathVariable("username") String username, @PathVariable("activeCode") String code, ModelAndView modelAndView) {
        System.out.println(username);
        System.out.println(code);
        Map<String, Object> result = sysUserService.activeUser(username, code);
        modelAndView.setViewName("activeUserResult");
        modelAndView.addObject("msg", result.get("msg"));
        modelAndView.addObject("link", result.get("link"));
        modelAndView.addObject("btn", result.get("btn"));
        return modelAndView;
    }
    @PostMapping("/reActiveUser/{username}")
    public Result reSendMail(@Email(message = "邮箱格式错误") @PathVariable("username") String username) {
        return sysUserService.reSendActiveMail(username);
    }
}
