package cn.lookup.sanye.service.impl;

import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.mapper.SysUserMapper;
import cn.lookup.sanye.pojo.Auth;
import cn.lookup.sanye.pojo.Role;
import cn.lookup.sanye.pojo.User;
import cn.lookup.sanye.pojo.UserRole;
import cn.lookup.sanye.service.IUserRoleService;
import cn.lookup.sanye.service.IUserService;
import cn.lookup.sanye.service.SysUserService;
import cn.lookup.sanye.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/15 23:18
 * @Desc: 系统用户接口实现
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, User> implements SysUserService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRoleService userRoleService;
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

    /**
     * 用户注册接口
     * @param username
     * @param password
     * @param codeKey 验证码key
     * @param code  验证码
     * @return
     */
    @Override
    public Result register(String username, String password, String codeKey, String code) {
        if(redisUtil.hasKey(codeKey)){
            String realCode = (String) redisUtil.get(codeKey);
            if(StringUtils.isEmpty(realCode)||!realCode.equalsIgnoreCase(code)){
                return Result.fail(500,"验证码错误",null);
            }else{
                //清除本次的验证码
                redisUtil.del(codeKey);
                //新增用户数据
                User user = new User();
                user.setPassword(passwordEncoder.encode(password));
                user.setUsername(username);
                user.setStatus(0);
                userService.save(user);
                //设置用户角色
                UserRole userRole = new UserRole();
                userRole.setRole_id(2L);
                userRole.setUser_id(user.getId());
                userRoleService.save(userRole);
                return Result.success(200,"注册成功",null);
            }
        }
        return Result.fail(500,"验证码过期,请点击获取",null);
    }
}
