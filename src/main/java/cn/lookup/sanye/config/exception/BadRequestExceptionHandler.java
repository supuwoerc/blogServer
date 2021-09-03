package cn.lookup.sanye.config.exception;

import cn.lookup.sanye.common.vo.Result;
import cn.lookup.sanye.exception.BadRequestException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/9/3 11:57
 * @Desc:desc 自定义请求异常捕获
 **/
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BadRequestExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public Result exceptionHandler(BadRequestException e) {
        return Result.fail(e.getCode(), e.getMessage(), null);
    }
}
