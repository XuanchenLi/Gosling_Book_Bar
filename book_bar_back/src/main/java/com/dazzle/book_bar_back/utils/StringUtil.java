package com.dazzle.book_bar_back.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @ClassName:StringUtil
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/22 下午 4:11
 * Version V1.0
 */

public class StringUtil {
    public static String allocateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
