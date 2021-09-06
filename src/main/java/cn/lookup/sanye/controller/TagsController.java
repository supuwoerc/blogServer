package cn.lookup.sanye.controller;


import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.exception.BadRequestException;
import cn.lookup.sanye.pojo.SysUserDetails;
import cn.lookup.sanye.pojo.Tags;
import cn.lookup.sanye.pojo.User;
import cn.lookup.sanye.service.ITagsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-08-09
 */
@RestController
@RequestMapping("/tags")
public class TagsController {
    @Autowired
    private ITagsService tagsService;

    /**
     * 查询当前账户所有的全部标签
     *
     * @return
     */
    @GetMapping("/list")
    public Result getTags() {
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Tags> list = tagsService.list(new QueryWrapper<Tags>().eq("create_user", sysUserDetails.getId()).orderByAsc("sort"));
        return Result.success(list);
    }

    /**
     * 保存标签(每个账户最多15个标签)
     *
     * @param tags
     * @return
     */
    @PostMapping("/save")
    public Result saveTags(@Validated @RequestBody Tags[] tags) {
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Tags> list = tagsService.list(new QueryWrapper<Tags>().eq("create_user", sysUserDetails.getId()));
        if (list.size() > 15) {
            throw new BadRequestException(500, "标签数量大于15");
        } else {
            for (int i = 0; i < tags.length; i++) {
                tags[i].setCreate_user(sysUserDetails.getId());
                tags[i].setSort((long) i);
            }
            tagsService.saveOrUpdateBatch(Arrays.asList(tags));
        }
        return Result.success("保存成功", null);
    }

    /**
     * 删除标签
     *
     * @param tag
     * @return
     */
    @DeleteMapping("/delete")
    public Result deleteTag(@RequestBody Tags tag) {
        SysUserDetails sysUserDetails = (SysUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tagsService.remove(new QueryWrapper<Tags>().eq("create_user", sysUserDetails.getId()).eq("id", tag.getId()));
        return Result.success("删除成功", null);
    }
}
