package cn.lookup.sanye.service.impl;

import cn.lookup.sanye.exception.BadRequestException;
import cn.lookup.sanye.pojo.*;
import cn.lookup.sanye.mapper.ArticleMapper;
import cn.lookup.sanye.service.IArticleService;
import cn.lookup.sanye.service.IUploadService;
import cn.lookup.sanye.utils.FileUploadAndDownloadUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-08-10
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Autowired
    private IUploadService uploadService;

    /**
     * 保存文章
     *
     * @param article
     * @param tagIds
     */
    @Override
    @Transactional
    public void saveArticle(Article article, Long[] tagIds) {
        article.setUpdate_time(LocalDateTime.now());
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (article.getId() == null) {
            article.setCreate_time(LocalDateTime.now());
            article.setCreate_user(sysUserDetails.getId());
            article.setLike_num(0L);
            article.setView_num(0L);
            //验证封面文件是否存在
            if (article.getCover_url() != null && !"".equals(article.getCover_url()) && !FileUploadAndDownloadUtils.fileExists(article.getCover_url())) {
                throw new BadRequestException(500, "封面图片已过期,请重新上传");
            }
            //激活封面
            if (article.getCover_url() != null && !"".equals(article.getCover_url())) {
                String fileName = FileUploadAndDownloadUtils.getFileName(article.getCover_url());
                uploadService.activeByFileNames(new String[]{fileName});
            }
            //保存文章
            this.save(article);
            //关联分类
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("article_id", article.getId());
            stringObjectHashMap.put("tagIds", tagIds);
            this.saveArticleTags(stringObjectHashMap);
        } else {
            Article one = this.getOne(new QueryWrapper<Article>().eq("id", article.getId()).eq("create_user", sysUserDetails.getId()));
            if (one == null) {
                throw new BadRequestException(500, "未找到用户的该文章");
            }
            //判断更新封面,需要将旧封面失活
            if (one.getCover_url() != null && !("".equals(one.getCover_url())) && !article.getCover_url().equals(one.getCover_url())) {
                uploadService.deleteByFileNames(new String[]{one.getCover_url()});
            }
            //激活新封面
            if (article.getCover_url() != null && !("".equals(article.getCover_url())) && !article.getCover_url().equals(one.getCover_url())) {
                uploadService.activeByFileNames(new String[]{article.getCover_url()});
            }
            this.updateById(article);
            //关联分类
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("article_id", article.getId());
            stringObjectHashMap.put("tagIds", tagIds);
            this.saveArticleTags(stringObjectHashMap);
        }
    }

    /**
     * 删除封面
     *
     * @param id
     */
    @Override
    public void deleteCover(Long id) {
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Upload one = uploadService.getOne(new QueryWrapper<Upload>().eq("id", id).eq("uid", sysUserDetails.getId()));
        if (one == null) {
            throw new BadRequestException(500, "用户未上传该文件");
        }
        if (one.getActive() == 1) {
            uploadService.delete(new Long[]{one.getId()});
        }
    }

    /**
     * 删除文章
     *
     * @param ids
     */
    @Override
    @Transactional
    public void deleteArticle(Long[] ids) {
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ArrayList<String> covers = new ArrayList<>();
        for (Long id : ids) {
            Article userArticleItem = this.getOne(new QueryWrapper<Article>().eq("id", id).eq("create_user", sysUserDetails.getId()));
            covers.add(FileUploadAndDownloadUtils.getFileName(userArticleItem.getCover_url()));
        }
        String[] arr = new String[covers.size()];
        String[] names = covers.toArray(arr);
        uploadService.deleteByFileNames(names); //失活文章关联的封面图片
        this.removeByIds(Arrays.asList(ids)); //删除文章
        this.baseMapper.deleteArticleTags(ids); //删除全部关联关系
    }

    /**
     * 关联文章分类
     */
    @Override
    @Transactional
    public void saveArticleTags(Map<String, Object> map) {
        Long[] tagIds = (Long[]) map.get("tagIds");
        Long article_id = (Long) map.get("article_id");
        if (tagIds.length <= 0) {
            throw new BadRequestException(500, "至少选择一个标签");
        }
        this.baseMapper.deleteArticleTags(new Long[]{article_id});
        this.baseMapper.saveArticleTags(map);
    }

    /**
     * 查询文章列表
     *
     * @param articlePage
     * @param keyWord
     * @param uid
     * @return
     */
    @Override
    public IPage<Article> getArticleList(Page<Article> articlePage, String keyWord, Long uid) {
        IPage<Article> pageList = this.baseMapper.getArticleList(articlePage, keyWord,uid);
        return pageList;
    }
}
