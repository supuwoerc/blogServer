package cn.lookup.sanye.controller;


import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangqm<sanye>
 * @since 2021-08-05
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private IUploadService uploadService;

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/delete")
    public Result adminDelete(@RequestBody Long[] ids) {
        uploadService.delete(ids);
        return Result.success("删除成功");
    }
}
