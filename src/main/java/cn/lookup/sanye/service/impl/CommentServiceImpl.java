package cn.lookup.sanye.service.impl;

import cn.lookup.sanye.pojo.Comment;
import cn.lookup.sanye.mapper.CommentMapper;
import cn.lookup.sanye.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-10-05
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
