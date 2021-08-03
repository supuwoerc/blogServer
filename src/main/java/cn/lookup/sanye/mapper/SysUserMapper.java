package cn.lookup.sanye.mapper;

import cn.lookup.sanye.pojo.Auth;
import cn.lookup.sanye.pojo.Role;
import cn.lookup.sanye.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/15 23:24
 * @Desc: 系统用户dao
 */
public interface SysUserMapper extends BaseMapper<User> {
    /**
     * 根据用户ID查询角色
     * @param userId 用户ID
     * @return
     */
    List<Role> findRolesByUserId(Long userId);
    /**
     * 根据用户名查询角色
     * @param userName 用户
     * @return
     */
    List<Role> findRolesByUserName(String userName);
    /**
     * 根据用户ID查询权限
     * @param userId 用户ID
     * @return
     */
    List<Auth> findAuthsByUserId(Long userId);

    /**
     * 查询用户列表
     * @param role_name
     * @return
     */
    IPage<User> getUserList(Page<User> page, String role_name);
}
