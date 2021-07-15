package cn.lookup.sanye.common.vo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/14 21:08
 * @Desc: 统一放回结果
 */
@Data
public class Result {
    private int code;
    private String message;
    private Object data;
    public static Result success(Object data){
        return success(200,"操作成功",data);
    }
    public static Result success(int code,String msg,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }
    public static Result fail(Object data){
        return success(400,"操作失败",data);
    }
    public static Result fail(int code,String msg,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }
    public static void write(int code, String message, Object data, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        Result result = Result.fail(code, message, data);
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}
