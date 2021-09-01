package cn.lookup.sanye.controller;


import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.common.vo.UploadFile;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.pojo.Upload;
import cn.lookup.sanye.service.IUploadService;
import cn.lookup.sanye.utils.MimeTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-08-10
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    IUploadService uploadService;

    /**
     * 文章封面上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload/cover")
    public Result uploadCoverImg(@RequestParam("file") MultipartFile file) {
        try {
            SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            final MultipartFile[] multipartFiles = new MultipartFile[]{file};
            final List<UploadFile> result = uploadService.upload(sysUserDetails.getId(), multipartFiles, null, MimeTypeEnum.IMAGE_EXTENSION.getTypes(), "文章封面");
            return Result.success(result.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 删除封面图片
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/cover/{coverId}")
    public Result deleteCoverImg(@PathVariable("coverId") Long id) {
        try {
            SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Upload one = uploadService.getOne(new QueryWrapper<Upload>().eq("id", id).eq("uid", sysUserDetails.getId()));
            boolean deleteResult = uploadService.delete(new Long[]{one.getId()});
            String s = deleteResult ? "删除成功" : "删除失败";
            return Result.success(s);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e.getMessage());
        }
    }
}
