package cn.lookup.sanye.service.impl;

import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.config.ProjectInfoBean;
import cn.lookup.sanye.mapper.SysUserMapper;
import cn.lookup.sanye.pojo.Auth;
import cn.lookup.sanye.pojo.Role;
import cn.lookup.sanye.pojo.User;
import cn.lookup.sanye.pojo.UserRole;
import cn.lookup.sanye.service.IUserRoleService;
import cn.lookup.sanye.service.IUserService;
import cn.lookup.sanye.service.MailService;
import cn.lookup.sanye.service.SysUserService;
import cn.lookup.sanye.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Email;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @Autowired
    private MailService mailService;

    /**
     * 根据用户名称查询用户信息
     *
     * @param username 用户名称
     * @return
     */
    @Override
    public User findUserByUserName(String username) {

        return this.baseMapper.selectOne(
                new QueryWrapper<User>().lambda().eq(User::getUsername, username));
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<Role> findRolesByUserId(Long userId) {
        return this.baseMapper.findRolesByUserId(userId);
    }

    /**
     * 根据用户名查询角色
     *
     * @param userName 用户ID
     * @return
     */
    @Override
    public List<Role> findRolesByUserName(String userName) {
        return this.baseMapper.findRolesByUserName(userName);
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<Auth> findAuthsByUserId(Long userId) {
        return this.baseMapper.findAuthsByUserId(userId);
    }

    /**
     * 用户注册接口
     *
     * @param username
     * @param password
     * @param codeKey  验证码key
     * @param code     验证码
     * @return
     */
    @Override
    public Result register(String username, String password, String codeKey, String code) {
        User one = userService.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
        if (redisUtil.hasKey(codeKey)) {
            String realCode = (String) redisUtil.get(codeKey);
            if (StringUtils.isEmpty(realCode) || !realCode.equalsIgnoreCase(code)) {
                return Result.fail(500, "验证码错误", null);
            } else if (one != null) {
                return Result.fail(500, "账户已存在", null);
            } else {
                //清除本次的验证码
                redisUtil.del(codeKey);
                //新增用户数据
                User user = new User();
                user.setPassword(passwordEncoder.encode(password));
                user.setUsername(username);
                user.setStatus(-1);  //锁定状态,需要邮件激活
                userService.save(user);
                //设置用户角色
                UserRole userRole = new UserRole();
                userRole.setRole_id(2L);
                userRole.setUser_id(user.getId());
                userRoleService.save(userRole);
                HashMap<String, Object> dataMap = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                redisUtil.hset("active-mapper", username, uuid, 10 * 60);  //有效期1小时
                dataMap.put("activeLink", ProjectInfoBean.getServerUrl() + "/user/activeUser/" + username + "/" + uuid);
                mailService.sendTemplateMail(username, "激活账户", "activeUserTemplate.html", dataMap);
                return Result.success(200, "注册成功", null);
            }
        }
        return Result.fail(500, "验证码过期,请点击获取", null);
    }

    /**
     * 邮件激活账户
     *
     * @param username
     * @return
     */
    @Override
    public Map<String, Object> activeUser(String username, String code) {
        HashMap<String, Object> result = new HashMap<>();
        User user = userService.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
        if (user != null) {
            if (user.getStatus().equals(-1)) {
                if (!redisUtil.hHasKey("active-mapper", username) || !redisUtil.hget("active-mapper", username).equals(code)) {
                    result.put("msg", "激活邮件已过期");
                    result.put("btn", "请重新获取激活邮件");
                    return result;
                }
                User temp = new User();
                temp.setStatus(1);
                this.baseMapper.update(temp,new UpdateWrapper<User>().eq("username",username));
                redisUtil.hdel("active-mapper",username);
                result.put("msg", "激活成功");
                result.put("btn", "可以登录啦");
            } else {
                result.put("msg", "账户已被激活");
                result.put("btn", "可以登录啦");
            }
        } else {
            result.put("msg", "账户不存在");
            result.put("btn", "请去注册");
        }
        return result;
    }

    /**
     * 重新发送激活邮件
     * @param username
     * @return
     */
    @Override
    public Result reSendActiveMail(@Email(message = "邮箱格式错误") String username) {
        User user = userService.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username).eq(User::getStatus,-1));
        if(user!=null){
            if(redisUtil.hHasKey("active-mapper",username)){
                return Result.fail("请求太过频繁",null);
            }
            HashMap<String, Object> dataMap = new HashMap<>();
            String uuid = UUID.randomUUID().toString();
            redisUtil.hset("active-mapper", username, uuid, 10 * 60);  //有效期
            dataMap.put("activeLink", ProjectInfoBean.getServerUrl() + "/user/activeUser/" + username + "/" + uuid);
            mailService.sendTemplateMail(username, "激活账户", "activeUserTemplate.html", dataMap);
            return Result.success("邮件发送成功",null);
        }else{
            return Result.fail("账户不存在或已激活",null);
        }
    }
}
