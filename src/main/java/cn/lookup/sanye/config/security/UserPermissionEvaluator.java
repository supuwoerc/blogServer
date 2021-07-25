package cn.lookup.sanye.config.security;

import cn.lookup.sanye.pojo.Auth;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/16 11:03
 * @Desc:用户访问权限处理（注解）
 **/
@Component
public class UserPermissionEvaluator implements PermissionEvaluator {
    @Autowired
    private SysUserService sysUserService;
    /**
     * 判断是否拥有权限
     * @param authentication 用户身份
     * @param targetUrl      目标路径
     * @param permission     路径权限
     * @return 是否拥有权限
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object permission) {
        SysUserDetails sysUserDetails = (SysUserDetails) authentication.getPrincipal();
        HashSet<String> permissions = new HashSet<>();  //权限集合
        List<Auth> authList = sysUserService.findAuthsByUserId(sysUserDetails.getId());  //查询用户权限
        for (Auth auth : authList) {
            permissions.add(auth.getPermission());
        }
        // 判断是否拥有权限
        if (permissions.contains(permission.toString())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String targetType, Object permission) {
        return false;
    }
}
