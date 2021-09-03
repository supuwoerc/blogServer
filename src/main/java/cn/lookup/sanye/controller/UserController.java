package cn.lookup.sanye.controller;

import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.common.vo.UploadFile;
import cn.lookup.sanye.exception.BadRequestException;
import cn.lookup.sanye.pojo.Role;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.pojo.Upload;
import cn.lookup.sanye.pojo.User;
import cn.lookup.sanye.service.IUploadService;
import cn.lookup.sanye.service.SysUserService;
import cn.lookup.sanye.utils.MimeTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    @Autowired
    private IUploadService uploadService;

    /**
     * 获取当前登录人信息
     *
     * @return
     */
    @GetMapping("/info")
    public Result getUserInfo() {
        //获取当前上下文中的用户
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = sysUserService.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, sysUserDetails.getUsername()));
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 获取当前登录人角色
     *
     * @return
     */
    @GetMapping("/role")
    public Result getUserRole() {
        //获取当前上下文中的用户
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Role> roles = sysUserService.findRolesByUserName(sysUserDetails.getUsername());
        return Result.success(roles);
    }

    /**
     * 需要管理员权限
     * 查询用户列表,可选参数用户角色role
     *
     * @param role
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/userList")
    public Result getUserList(@RequestParam(value = "role", required = false) String role, @RequestParam(value = "page", required = false, defaultValue = "1") long page,
                              @RequestParam(value = "size", required = false, defaultValue = "10") long size,
                              @RequestParam(value = "keyWord", required = false, defaultValue = "") String keyWord) {
        Page<User> userPage = new Page<>(page, size);
        IPage<User> userList = sysUserService.getUserList(userPage, role, keyWord);
        return Result.success(userList);
    }

    /**
     * 用户上传头像
     *
     * @param file
     * @return
     */
    @PostMapping("/upload/avatar")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file) throws Exception {
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UploadFile uploadFile = sysUserService.updateAvatar(file, sysUserDetails.getId());
        return Result.success(uploadFile);
    }

    /**
     * 更新当前用户信息
     *
     * @param userDto
     * @return
     */
    @PostMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody User userDto) {
        //获取当前上下文中的用户
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = sysUserService.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, sysUserDetails.getUsername()));
        sysUserService.update(userDto, new UpdateWrapper<User>().lambda().eq(User::getId, user.getId()));
        return Result.success("更新成功");
    }

    /**
     * 激活账户
     *
     * @param username
     * @param code
     * @param modelAndView
     * @return
     */
    @GetMapping("/activeUser/{username}/{activeCode}")
    public ModelAndView activeUser(@PathVariable("username") String username, @PathVariable("activeCode") String code, ModelAndView modelAndView) {
        Map<String, Object> result = sysUserService.activeUser(username, code);
        modelAndView.setViewName("activeUserResult");
        modelAndView.addObject("msg", result.get("msg"));
        modelAndView.addObject("link", result.get("link"));
        modelAndView.addObject("btn", result.get("btn"));
        return modelAndView;
    }

    /**
     * 发送激活邮件到指定账户
     *
     * @param username
     * @return
     */
    @PostMapping("/reActiveUser/{username}")
    public Result reSendMail(@Email(message = "邮箱格式错误") @PathVariable("username") String username) {
        sysUserService.reSendActiveMail(username);
        return Result.success("发送成功");
    }
}
