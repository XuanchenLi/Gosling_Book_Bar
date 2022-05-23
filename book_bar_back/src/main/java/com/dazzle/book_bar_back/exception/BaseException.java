package com.dazzle.book_bar_back.exception;

import com.dazzle.book_bar_back.utils.ResultCode;
import lombok.Data;

/**
 * @ClassName:BaseException
 * @Description: TODO
 * @Author:Dazz1e
 * @Date:2022/5/22 下午 4:43
 * Version V1.0
 * https://juejin.cn/post/7002926832160866318#heading-2
 */
@Data
public class BaseException extends RuntimeException {

    private static final int BASE_EXCEPTION_CODE = ResultCode.INTERNAL_ERROR.code();
    private static final String BASE_EXCEPTION_MESSAGE = ResultCode.INTERNAL_ERROR.message();

    private Integer code;
    private String message;

    public BaseException() {
        super(BASE_EXCEPTION_MESSAGE);
        this.code = BASE_EXCEPTION_CODE;
        this.message = BASE_EXCEPTION_MESSAGE;
    }

    public BaseException(String message) {
        super(message);
        this.code = BASE_EXCEPTION_CODE;
        this.message = message;
    }

    public BaseException(ResultCode resultCode) {
        super(resultCode.message());
        this.code = resultCode.code();
        this.message = resultCode.message();
   }

    public BaseException(Throwable cause) {
        super(cause);
        this.code = BASE_EXCEPTION_CODE;
        this.message = BASE_EXCEPTION_MESSAGE;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.code = BASE_EXCEPTION_CODE;
        this.message = message;
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}

