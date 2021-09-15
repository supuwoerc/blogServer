package cn.lookup.sanye.controller;


import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.common.vo.UploadFile;
import cn.lookup.sanye.pojo.Article;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.service.IArticleService;
import cn.lookup.sanye.service.IUploadService;
import cn.lookup.sanye.utils.MimeTypeEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        articleService.deleteCover(map.get("id"));
        return Result.success("删除成功");
    }

    /**
     * 保存文章
     *
     * @param map
     * @return
     */
    @PutMapping("/save")
    public Result saveArticle(@RequestBody Map<String,Object> map) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Object tags = map.get("tags");
        String tagsJson = objectMapper.writeValueAsString(tags);
        Object article = map.get("article");
        String articleJson = objectMapper.writeValueAsString(article);
        Article articlePojo = objectMapper.readValue(articleJson, Article.class);
        Long[] tagsIds = objectMapper.readValue(tagsJson,new TypeReference<Long[]>() {});
        articleService.saveArticle(articlePojo,tagsIds);
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
        articleService.deleteArticle(ids);
        return Result.success("删除成功");
    }

    /**
     * 修改文章关联的分类标签
     * @param map
     * @return
     */
    @PostMapping("/update/tags")
    public Result saveArticleTags(@RequestBody Map<String,Object> map){
        articleService.saveArticleTags(map);
        return Result.success("保存成功");
    }
}
