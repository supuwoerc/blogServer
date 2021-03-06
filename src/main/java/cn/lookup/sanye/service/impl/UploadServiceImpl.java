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
import java.util.ArrayList;
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
    public List<UploadFile> upload(Long uid, MultipartFile[] files, String dir, String[] allowedExtension, String name, int active) throws Exception {
        for (MultipartFile file : files) {
            if (file == null || file.getOriginalFilename() == null || "".equals(file.getOriginalFilename())) {
                throw new Exception("上传文件为空");
            }
        }
        List<UploadFile> uploadResult = null;
        if (dir == null) {
            uploadResult = FileUploadAndDownloadUtils.upload(files, allowedExtension);
        } else {
            uploadResult = FileUploadAndDownloadUtils.upload(dir, files, allowedExtension);
        }
        ArrayList<Upload> batch = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            Upload uploadFile = new Upload();
            uploadFile.setUid(uid);
            uploadFile.setDescription(name);
            uploadFile.setLocation(uploadResult.get(i).getLocation());
            uploadFile.setOld_name(files[i].getOriginalFilename());
            uploadFile.setCreate_time(LocalDateTime.now());
            uploadFile.setUpdate_time(LocalDateTime.now());
            uploadFile.setSize(files[i].getSize());
            uploadFile.setActive(active);
            uploadFile.setFile_name(uploadResult.get(i).getName());
            batch.add(uploadFile);
        }
        this.saveBatch(batch); //批量添加数据库记录
        for (int i = 0; i < uploadResult.size(); i++) {
            uploadResult.get(i).setId(batch.get(i).getId());
        }
        return uploadResult;
    }

    /**
     * 更新数据库文件上传的记录为失活(文件不会删除，文件的删除依赖定时任务)
     *
     * @param ids
     * @return
     */
    @Override
    public void delete(Long[] ids) {
        this.baseMapper.deleteByIds(ids);
    }

    /**
     * 更新数据库文件上传的记录为失活(文件不会删除，文件的删除依赖定时任务)
     *
     * @param names upload的文件名集合
     */
    @Override
    public void deleteByFileNames(String[] names) {
        this.baseMapper.deleteByFileNames(names);
    }

    /**
     * 激活文件,根据id集合
     *
     * @param ids
     */
    @Override
    public void active(Long[] ids) {
        this.baseMapper.active(ids);
    }

    /**
     * 激活文件，根据文件名集合
     *
     * @param names
     */
    @Override
    public void activeByFileNames(String[] names) {
        this.baseMapper.activeByFileNames(names);
    }
}
