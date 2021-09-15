package cn.lookup.sanye.service;

import cn.lookup.sanye.pojo.Article;
import cn.lookup.sanye.pojo.Tags;
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
}
