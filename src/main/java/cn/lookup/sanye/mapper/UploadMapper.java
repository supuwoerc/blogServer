package cn.lookup.sanye.mapper;

import cn.lookup.sanye.pojo.Upload;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-08-05
 */
public interface UploadMapper extends BaseMapper<Upload> {
    /**
     * 更新数据库文件上传的记录为失活(文件不会删除，文件的删除依赖定时任务)
     * @param ids upload的id集合
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
