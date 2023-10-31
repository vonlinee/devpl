package io.devpl.generator.common.query;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 抽象结果
 *
 * @param <T>
 */
abstract class AbstractResult<T> {

    /**
     * 编码 0表示成功，其他值表示失败
     */
    private int code = StatusCode.OK.getCode();
    /**
     * 消息内容
     */
    private String msg = StatusCode.OK.getMsg();
    /**
     * 响应数据，可能是单个对象或者对象数组
     */
    private T data;
    /**
     * 堆栈信息，仅在开发时使用，生产环境始终为null
     */
    private String stackTrace;

    public final int getCode() {
        return code;
    }

    public final void setCode(int code) {
        this.code = code;
    }

    public final String getMsg() {
        return msg;
    }

    public final void setMsg(String msg) {
        this.msg = msg;
    }

    public final T getData() {
        return data;
    }

    public final void setData(T data) {
        this.data = data;
    }

    public final String getStackTrace() {
        return stackTrace;
    }

    public final void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public final void setStatus(StatusCode status) {
        this.code = status.getCode();
        this.msg = status.getMsg();
    }

    public final void setCodeAndMsg(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public final void setStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        this.setStackTrace(throwable.toString());
    }
}
