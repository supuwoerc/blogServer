package cn.lookup.sanye.controller;


import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.common.vo.UploadFile;
import cn.lookup.sanye.service.IUploadService;
import cn.lookup.sanye.utils.MimeTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-08-10
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired IUploadService uploadService;

    /**
     * 文章封面上传
     * @param file
     * @return
     */
    @PostMapping("/upload/cover")
    public Result uploadCoverImg(@RequestParam("file") MultipartFile file) {
        try {
            final MultipartFile[] multipartFiles = new MultipartFile[]{file};
            final List<UploadFile> result = uploadService.upload(multipartFiles, null, MimeTypeEnum.IMAGE_EXTENSION.getTypes(), "文章封面");
            return Result.success(result.get(0));
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }
}
