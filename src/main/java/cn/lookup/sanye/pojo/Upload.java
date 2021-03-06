package cn.lookup.sanye.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文件上传实体
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_upload")
public class Upload implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 上传者用户id
     */
    private Long uid;
    /**
     * 可用状态
     */
    private int active;
    /**
     * 本地存储位置
     */
    private String location;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 文件名
     */
    private String old_name;
    /**
     * 文件在磁盘上的名子
     */
    private String file_name;

    /**
     * 描述
     */
    private String description;

    /**
     * 上传时间
     */
    private LocalDateTime create_time;

    /**
     * 更新时间
     */
    private LocalDateTime update_time;


}
