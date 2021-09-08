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
     * @param active 初始状态：0失活 1激活
     * @return
     * @throws Exception
     */
    List<UploadFile> upload(Long uid,MultipartFile[] files, String dir, String[] allowedExtension, String name,int active) throws Exception;

    /**
     * 更新数据库文件上传的记录为失活(文件不会删除，文件的删除依赖定时任务)
     * @param ids upload的id集合
     * @return
     */
    void delete(Long[] ids);

    /**
     * 更新数据库文件上传的记录为失活(文件不会删除，文件的删除依赖定时任务)
     * @param names upload的文件名集合
     */
    void deleteByFileNames(String[] names);

    /**
     * 激活文件
     * @param ids
     */
    void active(Long[] ids);
    /**
     * 激活文件
     * @param names
     */
    void activeByFileNames(String[] names);
}
