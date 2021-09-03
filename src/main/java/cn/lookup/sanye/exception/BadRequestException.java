package cn.lookup.sanye.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/9/3 11:55
 * @Desc:desc 自定义请求异常
 **/
@Data
@AllArgsConstructor
public class BadRequestException extends RuntimeException {
    private int code;
    private String message;
}
