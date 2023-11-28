package io.devpl.backend.common.query;

/**
 * 自定义业务状态码
 */
public enum StatusCode {

    OK(2000, "OK"),
    NOT_FOUND(404, "Not Found"),
    FORBIDDEN(403, "Forbidden"),
    IO_ERROR(6001, "IO异常"),
    FILE_NOT_FOUND(6002, "文件不存在"),
    RESOURCE_NOT_FOUND(4040, "资源不存在"),
    INTERNAL_SERVER_ERROR(5000, "服务器异常，请稍后再试");

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
