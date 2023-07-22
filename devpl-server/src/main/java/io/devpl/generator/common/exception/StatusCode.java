package io.devpl.generator.common.exception;

/**
 * Http状态码 + 自定义业务状态码
 */
public enum StatusCode {

    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    IO_ERROR(6001, "IO异常"),
    INTERNAL_SERVER_ERROR(500, "服务器异常，请稍后再试");

    private final int code;
    private final String msg;

    StatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
