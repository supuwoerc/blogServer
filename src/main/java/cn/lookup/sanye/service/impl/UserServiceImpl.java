package cn.lookup.sanye.service.impl;

import cn.lookup.sanye.pojo.User;
import cn.lookup.sanye.mapper.UserMapper;
import cn.lookup.sanye.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-07-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
