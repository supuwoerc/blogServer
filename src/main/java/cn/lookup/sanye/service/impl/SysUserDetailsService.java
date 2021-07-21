package cn.lookup.sanye.service.impl;

import cn.lookup.sanye.pojo.Role;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.pojo.User;
import cn.lookup.sanye.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/15 23:53
 * @Desc: 用户登录service
 */
@Service
@Slf4j
public class SysUserDetailsService implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 根据用户名查用户信息
     *
     * @param username 用户名
     * @return 用户详细信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User sysUser = sysUserService.findUserByUserName(username);
        if (sysUser != null) {
            //设置用户
            SysUserDetails sysUserDetails = new SysUserDetails();
            BeanUtils.copyProperties(sysUser, sysUserDetails);
            HashSet<GrantedAuthority> authorities = new HashSet<>();
            List<Role> roleList = sysUserService.findRolesByUserId(sysUser.getId());
            roleList.forEach(role -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole_name()));
            });
            sysUserDetails.setAuthorities(authorities);
            return sysUserDetails;
        } else {
            log.info("账户不存在");
            throw new UsernameNotFoundException("账户不存在");
        }
    }
}
