package com.dazzle.book_bar_back.exception.handler;

import com.alibaba.fastjson.JSON;
import com.dazzle.book_bar_back.response.ErrorResult;
import com.dazzle.book_bar_back.utils.enums.ResultCode;
import com.dazzle.book_bar_back.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName:AccessDeniedHandlerImpl
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/23 下午 4:20
 * Version V1.0
 */
@Component
@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorResult result = ErrorResult.error(ResultCode.ACCESS_DENIED, "权限不足");
        String json = JSON.toJSONString(result);
        WebUtil.renderString(response, json);
    }
}
