package cn.lookup.sanye.service;

import cn.lookup.sanye.pojo.Auth;
import cn.lookup.sanye.pojo.Role;
import cn.lookup.sanye.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/15 23:11
 * @Desc: 自定义系统用户相关接口
 */
public interface SysUserService extends IService<User> {
    /**
     * 根据账户名查询账户
     * @param username
     * @return
     */
    User findUserByUserName(String username);

    /**
     * 根据账户id查询角色
     * @param userId
     * @return
     */
    List<Role> findRolesByUserId(Long userId);
    /**
     * 根据账户id查询权限
     * @param userId
     * @return
     */
    List<Auth> findAuthsByUserId(Long userId);
}
