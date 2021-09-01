package cn.lookup.sanye.service;

import cn.lookup.sanye.common.vo.UploadFile;
import cn.lookup.sanye.pojo.Upload;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-08-05
 */
public interface IUploadService extends IService<Upload> {
    /**
     * 上传文件
     * @param uid 用户id
     * @param files 文件集合
     * @param dir 上传目录
     * @param allowedExtension 扩展名枚举
     * @param name 上传动作,如：用户头像
     * @return
     * @throws Exception
     */
    List<UploadFile> upload(Long uid,MultipartFile[] files, String dir, String[] allowedExtension, String name) throws Exception;

    /**
     * 删除数据库文件上传的记录(文件不会删除，文件的删除依赖定时任务)
     * @param ids
     * @return
     */
    boolean delete(Long[] ids);
}
