package com.demo.weibo.common.exception;

import com.demo.weibo.common.enums.ExceptionEnums;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(chain = true)
public class WeiboException extends RuntimeException {

    private Integer state;

    private String message;

    public WeiboException(String message){
        this.message = message;
    }

    public WeiboException(Integer state, String message){
        this.state = state;
        this.message = message;
    }

    /**
     *
     * @param state 异常状态
     * @param message 异常消息
     * @param cause 原始异常对象
     */
    public WeiboException(Integer state, String message, Throwable cause){
        super(cause);
        this.state = state;
        this.message = message;
    }

    public WeiboException(ExceptionEnums exceptionEnums){
        this.state = exceptionEnums.getState();
        this.message = exceptionEnums.getMessage();
    }

    /**
     *
     * @param exceptionEnums 异常枚举
     * @param cause 异常原始对象
     */
    public WeiboException(ExceptionEnums exceptionEnums, Throwable cause){
        super(cause);
        this.state = exceptionEnums.getState();
        this.message = exceptionEnums.getMessage();
    }

}
