package cn.lookup.sanye.config;

import cn.lookup.sanye.common.vo.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/16 16:09
 * @Desc:自定义全局的异常捕获处理器
 **/
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    /**
     * 参数校验异常处理
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request,MethodArgumentNotValidException e){
        return Result.fail(500,e.getBindingResult().getFieldError().getDefaultMessage(),null);
    }

    /**
     * 全局的异常捕获处理
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request,Exception e){
        return Result.fail(500,e.getMessage(),null);
    }
}
