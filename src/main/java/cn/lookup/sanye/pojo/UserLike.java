package cn.lookup.sanye.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-10-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_like")
public class UserLike implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 点赞的数据ID
     */
    private Long like_id;

    /**
     * 描述
     */
    private String like_desc;

    /**
     * 点赞时间
     */
    private LocalDateTime create_time;


}
