package io.devpl.backend.common.exception;

import io.devpl.backend.common.query.StatusCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义服务器异常
 * 后台代码所有异常类继承此类
 * 约定惯例:
 * 1.controller层的异常只抛出此异常，或者Controller调用的Service中处理，只抛出此异常
 * 2.所有后台Service接口方法不显示声明异常信息，如果要处理异常，则应统计处理底层的异常，抛出此异常
 *
 * @see GlobalExceptionHandler
 */
@Setter
@Getter
public class BusinessException extends RuntimeException {

    private int code;
    private String msg;

    public BusinessException(Throwable throwable) {
        super(throwable);
        this.code = StatusCode.INTERNAL_SERVER_ERROR.getCode();
        this.msg = throwable.getMessage();
    }

    public BusinessException(String msg) {
        super(msg);
        this.code = StatusCode.INTERNAL_SERVER_ERROR.getCode();
        this.msg = msg;
    }

    public BusinessException(StatusCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public BusinessException(String msg, Throwable e) {
        super(msg, e);
        this.code = StatusCode.INTERNAL_SERVER_ERROR.getCode();
        this.msg = msg;
    }

    public static BusinessException create(String msg, Throwable e) {
        return new BusinessException(msg, e);
    }

    public static BusinessException create(String msgTemplate, Object... args) {
        return new BusinessException(msgTemplate.formatted(args));
    }
}
