package cn.lookup.sanye.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_tags")
public class Tags implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 标签名
     */
    @NotBlank(message = "标签名不能为空")
    @Length(message = "标签名需要在1-10之间",max = 10,min = 1)
    private String tag_name;

    /**
     * 创建者id
     */
    private Long create_user;

    /**
     * 排序权重
     */
    @NotBlank(message = "排序权重不能为空")
    private Long sort;


}
