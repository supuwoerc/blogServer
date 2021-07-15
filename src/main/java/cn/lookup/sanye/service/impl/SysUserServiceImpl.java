package cn.lookup.sanye.service.impl;

import cn.lookup.sanye.mapper.SysUserMapper;
import cn.lookup.sanye.pojo.Auth;
import cn.lookup.sanye.pojo.Role;
import cn.lookup.sanye.pojo.User;
import cn.lookup.sanye.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/15 23:18
 * @Desc: 系统用户接口实现
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, User> implements SysUserService {
    /**
     * 根据用户名称查询用户信息
     * @param username 用户名称
     * @return
     */
    @Override
    public User findUserByUserName(String username) {
        return this.baseMapper.selectOne(
                new QueryWrapper<User>().lambda().eq(User::getUsername,username).ne(User::getStatus,-1));
    }

    /**
     * 根据用户ID查询角色
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<Role> findRolesByUserId(Long userId) {
        return this.baseMapper.findRolesByUserId(userId);
    }
    /**
     * 根据用户ID查询权限
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<Auth> findAuthsByUserId(Long userId) {
        return this.baseMapper.findAuthsByUserId(userId);
    }
}
