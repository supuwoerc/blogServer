package cn.lookup.sanye.service;

import cn.lookup.sanye.pojo.Upload;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

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
     * @param files
     * @param dir
     * @return
     */
    String[] upload(MultipartFile[] files,String dir,String[] allowedExtension,String name) throws Exception;
}
