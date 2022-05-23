package com.dazzle.book_bar_back.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName:WebUtil
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/23 下午 6:09
 * Version V1.0
 */
public class WebUtil {
    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string) {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
