package cn.lookup.sanye.controller;


import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.common.vo.UploadFile;
import cn.lookup.sanye.exception.BadRequestException;
import cn.lookup.sanye.pojo.Article;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.pojo.Upload;
import cn.lookup.sanye.service.IArticleService;
import cn.lookup.sanye.service.IUploadService;
import cn.lookup.sanye.utils.FileUploadAndDownloadUtils;
import cn.lookup.sanye.utils.MimeTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    private IUploadService uploadService;
    @Autowired
    private IArticleService articleService;

    /**
     * 文章封面上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload/cover")
    public Result uploadCoverImg(@RequestParam("file") MultipartFile file) throws Exception {
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final MultipartFile[] multipartFiles = new MultipartFile[]{file};
        final List<UploadFile> result = uploadService.upload(sysUserDetails.getId(), multipartFiles, null, MimeTypeEnum.IMAGE_EXTENSION.getTypes(), "文章封面");
        return Result.success(result.get(0));
    }

    /**
     * 删除封面图片
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/cover")
    public Result deleteCoverImg(@RequestParam("id") Long id) {
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Upload one = uploadService.getOne(new QueryWrapper<Upload>().eq("id", id).eq("uid", sysUserDetails.getId()));
        if (one == null) {
            throw new BadRequestException(500, "用户未上传该文件");
        }
        uploadService.delete(new Long[]{one.getId()});
        return Result.success("删除成功");
    }

    /**
     * 保存文章
     *
     * @param article
     * @return
     */
    @PutMapping("/save")
    public Result saveArticle(@Validated @RequestBody Article article) {
        article.setUpdate_time(LocalDateTime.now());
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (article.getId() == null) {
            article.setCreate_time(LocalDateTime.now());
            article.setCreate_user(sysUserDetails.getId());
            articleService.save(article);
        } else {
            Article one = articleService.getOne(new QueryWrapper<Article>().eq("id", article.getId()).eq("create_user", sysUserDetails.getId()));
            if (one == null) {
                throw new BadRequestException(500, "未找到用户的该文章");
            }
            articleService.updateById(article);
        }
        return Result.success("保存成功");
    }

    /**
     * 删除文章
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    public Result deleteArticle(Long[] ids) {
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ArrayList<String> covers = new ArrayList<>();
        for (Long id : ids) {
            Article userArticleItem = articleService.getOne(new QueryWrapper<Article>().eq("id", id).eq("create_user", sysUserDetails.getId()));
            covers.add(FileUploadAndDownloadUtils.getFileName(userArticleItem.getCover_url()));
        }
        String[] arr = new String[covers.size()];
        String[] names = covers.toArray(arr);
        uploadService.delete(names); //失活文章关联的封面图片
        articleService.removeByIds(Arrays.asList(ids)); //删除文章
        return Result.success("删除成功");
    }
}
