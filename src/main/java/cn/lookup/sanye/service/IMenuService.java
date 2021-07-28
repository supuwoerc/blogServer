package cn.lookup.sanye.service;

import cn.lookup.sanye.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-07-28
 */
public interface IMenuService extends IService<Menu> {
    /**
     * 根据账户名查询菜单
     * @param userName
     * @return
     */
    List<Menu> findMenuByUserName(String userName);
}
