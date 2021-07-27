package cn.lookup.sanye.config.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/27 11:34
 * @Desc: 拆分多个全局异常捕获器，自定义顺序，这个处理类处理403权限不足的异常
 **/
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AccessDeniedExceptionHandler{
    /**
     * 解决GlobalDefaultExceptionHandler会消费掉 AccessDeniedException ,导致 AccessDeniedHandler不会被触发的问题.
     * @param
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public void exceptionHandler(AccessDeniedException e) throws AccessDeniedException {
        throw e;
    }
}
