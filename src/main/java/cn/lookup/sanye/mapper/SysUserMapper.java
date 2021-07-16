package cn.lookup.sanye.mapper;

import cn.lookup.sanye.pojo.Auth;
import cn.lookup.sanye.pojo.Role;
import cn.lookup.sanye.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/15 23:24
 * @Desc: 系统用户dao
 */
@Repository
public interface SysUserMapper extends BaseMapper<User> {
    /**
     * 根据用户ID查询角色
     * @param userId 用户ID
     * @return
     */
    List<Role> findRolesByUserId(Long userId);
    /**
     * 根据用户ID查询权限
     * @param userId 用户ID
     * @return
     */
    List<Auth> findAuthsByUserId(Long userId);
}
