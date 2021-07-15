package cn.lookup.sanye.service.impl;

import cn.lookup.sanye.pojo.Auth;
import cn.lookup.sanye.mapper.AuthMapper;
import cn.lookup.sanye.service.IAuthService;
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
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth> implements IAuthService {

}
