package cn.lookup.sanye.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/23 17:18
 * @Desc:desc
 **/
@Controller
@Slf4j
public class DefaultRouterController {
    @RequestMapping("/")
    public String defaultPage(){
        log.info("访问服务,重定向到接口文档地址...");
        return "redirect:/doc.html";
    }
}