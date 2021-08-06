package cn.lookup.sanye.common.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/8/6 10:48
 * @Desc:desc
 **/
@Data
public class UploadFile{
    /**
     * 本地存储位置
     */
    private String location;
    /**
     * 访问路径(配合图片服务器)
     */
    private String url;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 文件名
     */
    private String source_name;
    /**
     * 存储文件名
     */
    private String name;
    /**
     * 上传时间 yyyy-MM-dd HH:mm:ss
     */
    private String create_time;
}
