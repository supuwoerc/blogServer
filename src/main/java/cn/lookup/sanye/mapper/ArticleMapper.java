package cn.lookup.sanye.mapper;

import cn.lookup.sanye.pojo.Article;
import cn.lookup.sanye.pojo.Tags;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-08-10
 */
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 保存文章关联的分类标签
     * @param map
     */
    void saveArticleTags(Map<String,Object> map);

    /**
     * 删除文章的关联标签
     * @param article_ids
     */
    void deleteArticleTags(Long[] article_ids);

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
     * 根据文章ID查询文章信息的方法
     * @param id
     * @return
     */
    Article getArticleDetailById(Long id);

}
