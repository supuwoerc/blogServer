package cn.lookup.sanye.mapper;

import cn.lookup.sanye.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-07-28
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据账户名查询菜单
     * @param userName
     * @return
     */
    List<Menu> findMenuByUserName(String userName);
}
