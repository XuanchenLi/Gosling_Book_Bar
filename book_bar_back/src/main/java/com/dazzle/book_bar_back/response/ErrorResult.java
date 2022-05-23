package com.dazzle.book_bar_back.response;

import com.dazzle.book_bar_back.utils.ResultCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName:ErrorResult
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/22 下午 4:54
 * Version V1.0
 */
@Data
public class ErrorResult implements Serializable {
    private Integer code;
    private String message;
    private boolean success = false;
    @JsonIgnore
    private ResultCode resultCode;

    public static ErrorResult error() {
        ErrorResult result = new ErrorResult();
        result.setResultCode(ResultCode.INTERNAL_ERROR);
        return result;
   }

    public static ErrorResult error(String message) {
        ErrorResult result = new ErrorResult();
        result.setCode(ResultCode.INTERNAL_ERROR.code());
        result.setMessage(message);
        return result;
   }

    public static ErrorResult error(Integer code, String message) {
        ErrorResult result = new ErrorResult();
        result.setCode(code);
        result.setMessage(message);
        return result;
   }

   public static ErrorResult error(ResultCode resultCode, String message) {
        ErrorResult result = new ErrorResult();
        result.setCode(resultCode.code());
        result.setResultCode(resultCode);
        result.setMessage(message);
        return result;
   }
}
