package io.devpl.generator.common.exception;

/**
 * 状态码
 */
public enum StatusCode {
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
