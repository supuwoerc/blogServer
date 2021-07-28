package cn.lookup.sanye.service.impl;

import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.pojo.Menu;
import cn.lookup.sanye.mapper.MenuMapper;
import cn.lookup.sanye.pojo.Role;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-07-28
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    /**
     * 根据账户名查询菜单
     * @param userName
     * @return
     */
    @Override
    public List<Menu> findMenuByUserName(String userName) {
        List<Menu> menus = this.baseMapper.findMenuByUserName(userName);
        return buildTreeMenu(menus,0L);
    }
    /**
     * 将数据组装成树状数据
     * @param menuList
     * @param pid
     * @return
     */
    private List<Menu> buildTreeMenu(List<Menu> menuList,Long pid){
        List<Menu> resultTree = new ArrayList<>();
        for (Menu menu : menuList) {
            if(menu.getPid().equals(pid)){
                menu.setChildren(buildTreeMenu(menuList,menu.getId()));
                resultTree.add(menu);
            }
        }
        return resultTree;
    }

}
