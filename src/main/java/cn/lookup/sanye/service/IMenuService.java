package cn.lookup.sanye.service;

import cn.lookup.sanye.pojo.Menu;
import cn.lookup.sanye.pojo.Role;
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
     * 根据角色查询当前菜单(包括匿名用户)
     * @param roles 角色集合
     * @return
     */
    List<Menu> findMenuByUserRoles(List<Role> roles);
}
