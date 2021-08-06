package cn.lookup.sanye.service.impl;

import cn.lookup.sanye.common.vo.UploadFile;
import cn.lookup.sanye.pojo.Upload;
import cn.lookup.sanye.mapper.UploadMapper;
import cn.lookup.sanye.service.IUploadService;
import cn.lookup.sanye.utils.FileUploadAndDownloadUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-08-05
 */
@Service
public class UploadServiceImpl extends ServiceImpl<UploadMapper, Upload> implements IUploadService {
    /**
     * 上传文件的方法
     *
     * @param files            文件集合
     * @param dir              目录
     * @param allowedExtension 文件类型
     * @param name             业务名
     * @return
     * @throws Exception
     */
    @Override
    public List<UploadFile> upload(MultipartFile[] files, String dir, String[] allowedExtension, String name) throws Exception {
        for (MultipartFile file : files) {
            if (file==null||file.getOriginalFilename() == null || "".equals(file.getOriginalFilename())) {
                throw new Exception("上传文件为空");
            }
        }
        List<UploadFile> uploadResult = null;
        if (dir == null) {
            uploadResult = FileUploadAndDownloadUtils.upload(files, allowedExtension);
        } else {
            uploadResult = FileUploadAndDownloadUtils.upload(dir, files, allowedExtension);
        }
        for (int i = 0; i < files.length; i++) {
            Upload uploadFile = new Upload();
            uploadFile.setDescription(name);
            uploadFile.setLocation(uploadResult.get(i).getLocation());
            uploadFile.setOld_name(files[i].getOriginalFilename());
            uploadFile.setCreate_time(LocalDateTime.now());
            uploadFile.setUpdate_time(LocalDateTime.now());
            uploadFile.setSize(files[i].getSize());
            this.baseMapper.insert(uploadFile);
        }
        return uploadResult;
    }
}
