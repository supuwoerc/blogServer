package cn.lookup.sanye.controller;

import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.utils.RedisUtil;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/14 20:46
 * @Desc: 验证码接口
 */
@RestController
public class CaptchaController {
    @Autowired
    private RedisUtil redisUtil;
    @Value("${captcha.width}")
    private int width;
    @Value("${captcha.height}")
    private int height;
    @GetMapping("/captcha")
    public Result get(){
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(width, height);
        captcha.setLen(2);
        String resultValue = captcha.text();
        String key = UUID.randomUUID().toString();
        redisUtil.set(key,resultValue,30);
        HashMap<String, Object> jsonResult = new HashMap<>();
        jsonResult.put("codeKey",key);
        jsonResult.put("code2Base64",captcha.toBase64());
        return Result.success(jsonResult);
    }
}
