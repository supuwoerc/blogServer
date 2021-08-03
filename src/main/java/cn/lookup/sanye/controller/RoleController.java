package cn.lookup.sanye.controller;


import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.pojo.Role;
import cn.lookup.sanye.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * @since 2021-07-15
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    /**
     * 需要管理员权限
     * 查询全部角色列表(不包括匿名角色：匿名角色仅仅在默认菜单处可用)
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public Result getRoleList(){
        List<Role> list = roleService.list(new QueryWrapper<Role>().ne("role_name","ANONYMOUS"));
        return Result.success(list);
    }
}
