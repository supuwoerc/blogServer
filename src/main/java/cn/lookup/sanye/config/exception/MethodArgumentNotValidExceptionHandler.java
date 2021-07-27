package cn.lookup.sanye.config.exception;

import cn.lookup.sanye.common.vo.Result;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/27 11:33
 * @Desc: 拆分多个全局异常捕获器，自定义顺序，这个处理类处理参数校验失败的异常
 **/
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request, MethodArgumentNotValidException e){
        return Result.fail(500,e.getBindingResult().getFieldError().getDefaultMessage(),null);
    }
}
