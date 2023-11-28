package io.devpl.backend.common;

import io.devpl.backend.common.query.StatusCode;

/**
 * 自定义服务器异常
 */
public final class ServerException extends RuntimeException {

    private int code;
    private String msg;

    public ServerException(String msg) {
        super(msg);
        this.code = StatusCode.INTERNAL_SERVER_ERROR.getCode();
        this.msg = msg;
    }

    public ServerException(StatusCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public ServerException(String msg, Throwable e) {
        super(msg, e);
        this.code = StatusCode.INTERNAL_SERVER_ERROR.getCode();
        this.msg = msg;
    }

    public static ServerException create(String msgTemplate, Object... args) {
        return new ServerException(msgTemplate.formatted(args));
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
