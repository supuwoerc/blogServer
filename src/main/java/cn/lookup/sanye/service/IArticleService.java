package cn.lookup.sanye.service;

import cn.lookup.sanye.pojo.Article;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-08-10
 */
public interface IArticleService extends IService<Article> {
    /**
     * 保存文章
     * @param article
     * @param tagIds
     */
    void saveArticle(Article article, Long[] tagIds);

    /**
     * 删除封面
     * @param id
     */
    void deleteCover(Long id);

    /**
     * 删除文章
     * @param ids
     */
    void deleteArticle(Long[] ids);

    /**
     * 保存文章分类标签
     * @param map
     */
    void saveArticleTags(Map<String,Object> map);

    /**
     * 查询文章列表
     * @param articlePage
     * @param keyWord
     * @param isOverview
     * @param uid
     * @return
     */
    IPage<Article> getArticleList(Page<Article> articlePage, String keyWord, int isOverview, Long uid);

    /**
     * 根据文章ID查询文件信息的方法
     * @param id
     * @return
     */
    Article getArticleDetailById(Long id);

    /**
     * 点赞、取消点赞
     * @param articleId
     * @param like
     * @return
     */
    boolean makeLikeStatus(Long articleId, Long like);
}
