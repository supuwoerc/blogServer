package cn.lookup.sanye.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-08-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_article")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(message = "标题长度需要在1-20之间",min = 1,max = 20)
    private String title;

    /**
     * 文章详情
     */
    private String content;
    /**
     * 文章封面
     */
    private String cover_url;
    /**
     * 点赞数(redis同步)
     */
    private Long like_num;

    /**
     * 浏览量(redis同步)
     */
    private Long view_num;

    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime update_time;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime create_time;

    /**
     * 创建者id
     */
    private Long create_user;
    /**
     * 标签列表
     */
    @TableField(exist = false)
    private List<Tags> tagsList;
}
