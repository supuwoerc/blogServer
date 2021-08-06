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
     * @param files
     * @param dir
     * @return
     */
    List<UploadFile> upload(MultipartFile[] files, String dir, String[] allowedExtension, String name) throws Exception;
}
