package cn.lookup.sanye.controller;


import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.pojo.Menu;
import cn.lookup.sanye.pojo.Role;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.service.IMenuService;
import cn.lookup.sanye.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-07-28
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private IMenuService menuService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 查询登录用户的菜单
     * @return
     */
    @GetMapping("/currentUserMenu")
    @PreAuthorize("isAuthenticated()")  //只允许登录认证过的账户访问
    public Result getCurrentUserMenu() {
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Role> roles = sysUserService.findRolesByUserName(sysUserDetails.getUsername());
        List<Menu> menus = menuService.findMenuByUserRoles(roles);
        return Result.success(menus);
    }

    /**
     * 查询匿名用户的菜单(默认菜单)
     * @return
     */
    @GetMapping("/defaultMenu")
    @PreAuthorize("isAnonymous()") //只允许匿名访问
    public Result getDefaultMenu() {
        ArrayList<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setId(3L);
        role.setRole_name("ANONYMOUS");
        roles.add(role);
        List<Menu> menus = menuService.findMenuByUserRoles(roles);
        return Result.success(menus);
    }
}
