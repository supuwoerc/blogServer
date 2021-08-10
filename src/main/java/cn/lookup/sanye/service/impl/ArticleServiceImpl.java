package cn.lookup.sanye.service.impl;

import cn.lookup.sanye.pojo.Article;
import cn.lookup.sanye.mapper.ArticleMapper;
import cn.lookup.sanye.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-08-10
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
