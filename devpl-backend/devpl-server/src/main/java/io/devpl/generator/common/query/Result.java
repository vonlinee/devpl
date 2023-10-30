package io.devpl.generator.common.query;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

/**
 * 单条结果的响应数据
 */
public class Result<T> extends AbstractResult<T> implements Serializable {

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        result.setCode(StatusCode.OK.getCode());
        result.setMsg(StatusCode.OK.getMsg());
        return result;
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
        Result<T> result = error(throwable.getMessage());
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        result.setStackTrace(throwable.toString());
        return result;
    }

    public static <T> Result<T> error(int code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
