package io.devpl.generator.common.query;

import java.io.Serializable;

/**
 * 单条结果的响应数据
 */
public class Result<T> extends AbstractResult<T> implements Serializable {

    Result(T data) {
        this.setData(data);
    }

    Result(StatusCode statusCode) {
        this.setStatus(statusCode);
    }

    Result(int code, String message, T data) {
        this.setData(data);
        this.setCodeAndMsg(code, message);
    }

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> error() {
        return error(StatusCode.INTERNAL_SERVER_ERROR);
    }

    public static <T> Result<T> error(String msg) {
        return error(StatusCode.INTERNAL_SERVER_ERROR.getCode(), msg);
    }

    public static <T> Result<T> error(StatusCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> Result<T> exception(Throwable throwable) {
        Result<T> result = error();
        result.setStackTrace(throwable);
        return result;
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }
}
