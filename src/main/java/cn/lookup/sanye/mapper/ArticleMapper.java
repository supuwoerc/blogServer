package cn.lookup.sanye.mapper;

import cn.lookup.sanye.pojo.Article;
import cn.lookup.sanye.pojo.Tags;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
}
