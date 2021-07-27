package cn.lookup.sanye.config.exception;

import cn.lookup.sanye.common.vo.Result;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/16 16:09
 * @Desc:自定义全局的异常捕获处理器,在最后捕获异常（order决定优先级）
 **/
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalDefaultExceptionHandler{
    /**
     * 全局的异常捕获处理(这里会捕获UserAccessDeniedHandler中抛出的权限不足的异常)
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request,RuntimeException e){
        return Result.fail(500,e.getMessage(),null);
    }
}
