package cn.lookup.sanye.controller;


import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.pojo.Menu;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
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
    /**
     * 查询当前用户的菜单
     * @return
     */
    @GetMapping("/currentUserMenu")
    public Result getCurrentUserMenu(){
        SysUserDetails  sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Menu> menus = menuService.findMenuByUserName(sysUserDetails.getUsername());
        return Result.success(menus);
    }
}
