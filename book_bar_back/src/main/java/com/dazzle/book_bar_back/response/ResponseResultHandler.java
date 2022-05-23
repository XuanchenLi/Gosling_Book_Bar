package com.dazzle.book_bar_back.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @ClassName:ResponseResultHandler
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/22 下午 4:59
 * Version V1.0
 */
@RestControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {
    /* 使用统一返回体的标识 */
    private static final String RESPONSE_RESULT_ANNOTATION = "RESPONSE-RESULT-ANNOTATION";

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(sra).getRequest();
        ResponseResult responseResult = (ResponseResult) request.getAttribute(RESPONSE_RESULT_ANNOTATION);
        // 判断返回体是否需要处理
        return responseResult != null;
   }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // 异常响应体则直接返回code+message的消息体
        if (body instanceof ErrorResult) {
            return body;
       }
        // 正常响应体则返回Result包装的code+message+data的消息体
        return Result.success(body);
   }
}

