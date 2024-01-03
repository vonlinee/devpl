package io.devpl.backend.common.exception;

import io.devpl.backend.common.query.StatusCode;

/**
 * 自定义服务器异常
 * 后台代码所有异常类继承此类
 */
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

    public static BusinessException create(String msgTemplate, Object... args) {
        return new BusinessException(msgTemplate.formatted(args));
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
