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

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        final List<UploadFile> result = uploadService.upload(sysUserDetails.getId(), multipartFiles, null, MimeTypeEnum.IMAGE_EXTENSION.getTypes(), "文章封面", 0);
        return Result.success(result.get(0));
    }

    /**
     * 失活封面图片
     *
     * @return
     */
    @DeleteMapping("/delete/cover")
    public Result deleteCoverImg(@RequestBody Map<String, Long> map) {
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Upload one = uploadService.getOne(new QueryWrapper<Upload>().eq("id", map.get("id")).eq("uid", sysUserDetails.getId()));
        if (one == null) {
            throw new BadRequestException(500, "用户未上传该文件");
        }
        if (one.getActive() == 1) {
            uploadService.delete(new Long[]{one.getId()});
        }
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
            //验证封面文件是否存在
            if (!FileUploadAndDownloadUtils.fileExists(article.getCover_url())) {
                throw new BadRequestException(500, "封面图片已过期");
            }
            //激活封面
            uploadService.activeByFileNames(new String[]{article.getCover_url()});
            //保存文章
            articleService.save(article);
        } else {
            Article one = articleService.getOne(new QueryWrapper<Article>().eq("id", article.getId()).eq("create_user", sysUserDetails.getId()));
            if (one == null) {
                throw new BadRequestException(500, "未找到用户的该文章");
            }
            //判断更新封面,需要将旧封面失活
            if (one.getCover_url() != null && !("".equals(one.getCover_url())) && !article.getCover_url().equals(one.getCover_url())) {
                uploadService.deleteByFileNames(new String[]{one.getCover_url()});
            }
            uploadService.deleteByFileNames(new String[]{article.getCover_url()});
            articleService.updateById(article);
        }
        return Result.success("保存成功");
    }

    /**
     * 删除文章
     *
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
        uploadService.deleteByFileNames(names); //失活文章关联的封面图片
        articleService.removeByIds(Arrays.asList(ids)); //删除文章
        return Result.success("删除成功");
    }
}
