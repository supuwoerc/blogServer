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
@TableName("sys_comment_reply")
public class CommentReply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 被评论的内容ID
     */
    private Long comment_id;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数量
     */
    private Long like_num;

    /**
     * 回复人的ID
     */
    private Long to_user;

    /**
     * 创建者ID
     */
    private Long create_user;

    /**
     * 创建时间
     */
    private LocalDateTime create_time;


}
