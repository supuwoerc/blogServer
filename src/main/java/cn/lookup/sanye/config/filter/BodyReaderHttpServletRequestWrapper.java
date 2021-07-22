package cn.lookup.sanye.config.filter;


import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/7/22 10:53
 * @Desc: 包装request,解决用inputStream读取request里面的json参数时,inputStream不能在过滤器链上重复调用的问题,以及导致@RequestBody无法读取参数的问题
 **/
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private byte[] requestBody = null;//用于将流保存下来
    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        //https://blog.csdn.net/kaizhangzhang/article/details/97900961
        /*这里会导致formData登录时request.getParameter读取参数失败，因为只要调用过request.getInputStream()和request.getParameter互斥,
        调整到getInputStream是再保存这个流*/
        requestBody = StreamUtils.copyToByteArray(request.getInputStream());
    }
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(requestBody==null){
            requestBody = StreamUtils.copyToByteArray(super.getInputStream());
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }
            @Override
            public boolean isFinished() {
                return false;
            }
            @Override
            public boolean isReady() {
                return false;
            }
            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }
    @Override
    public BufferedReader getReader() throws IOException{
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}