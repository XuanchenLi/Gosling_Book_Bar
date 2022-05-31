package com.dazzle.book_bar_back.utils;

import org.springframework.stereotype.Component;

import java.util.Objects;
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

    public static int getLeadingInteger(String inputString, int base) {
        if (Objects.isNull(inputString) || Objects.equals("", inputString))
            return 0;
        char[] input = inputString.toLowerCase().toCharArray();
        if (input[0] != '+' && input[0] !='-' && !(input[0] > '0' && input[0] < '9'))
            return 0;
        boolean positive = input[0] != '-';
        int start = input[0] == '+' || input[0] == '-' ? 1 : 0;
        int end = start;
        for(; end < input.length; end++) {
            try {
                // check if still a digit in right base
                Integer.parseInt(Character.toString(input[end]), base);
            } catch(NumberFormatException e) {
                break;
            }
        }
        int length = end - start;
        int result = Integer.parseInt(new String(input, start, length), base);
        return result * (positive ? 1 : -1);
    }

    public static Long getLeadingLong(String inputString, int base) {
        if (Objects.isNull(inputString) || Objects.equals("", inputString))
            return 0L;
        char[] input = inputString.toLowerCase().toCharArray();
        if (input[0] != '+' && input[0] !='-' && !(input[0] > '0' && input[0] < '9'))
            return 0L;
        boolean positive = input[0] != '-';
        int start = input[0] == '+' || input[0] == '-' ? 1 : 0;
        int end = start;
        for(; end < input.length; end++) {
            try {
                // check if still a digit in right base
                Integer.parseInt(Character.toString(input[end]), base);
            } catch(NumberFormatException e) {
                break;
            }
        }
        int length = end - start;
        Long result = Long.parseLong(new String(input, start, length), base);
        return result * (positive ? 1 : -1);
    }
}
