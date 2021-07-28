package cn.lookup.sanye.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 路由url
     */
    private String path;
    /**
     * 菜单的小图标
     */
    private String icon;
    /**
     * 排序权重
     */
    private Long sort;
    /**
     * 父菜单id
     */
    private Long pid;

    /**
     * 子菜单(表中并没有这个字段，需要排除)
     */
    @TableField(exist = false)
    private List<Menu> children;

}
