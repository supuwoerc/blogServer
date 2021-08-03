package cn.lookup.sanye.service;

import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.pojo.Auth;
import cn.lookup.sanye.pojo.Role;
import cn.lookup.sanye.pojo.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Map;

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
     * 根据账户名查询角色
     * @param userName 账户名
     * @return
     */
    List<Role> findRolesByUserName(String userName);
    /**
     * 根据账户id查询权限
     * @param userId
     * @return
     */
    List<Auth> findAuthsByUserId(Long userId);

    /**
     * 用户注册接口
     * @param username
     * @param password
     * @param codeKey 验证码key
     * @param code  验证码
     * @return
     */
    Result register(String username, String password, String codeKey, String code);

    /**
     * 邮件激活账户
     * @param username
     * @param code
     * @return
     */
    Map<String,Object> activeUser(String username, String code);

    /**
     * 重新发送激活邮件
     * @param username
     * @return
     */
    Result reSendActiveMail(@Email(message = "邮箱格式错误") String username);

    /**
     * 查询用户列表
     * @param userPage
     * @param role
     * @return
     */
    Result getUserList(Page<User> userPage, String role,String keyWord);
}
