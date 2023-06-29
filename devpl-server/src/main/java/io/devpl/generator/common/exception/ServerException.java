package io.devpl.generator.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义服务器异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerException extends RuntimeException {

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
}