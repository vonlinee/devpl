package io.devpl.generator.common.query;

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
     * 响应数据
     */
    private T data;
    /**
     * 堆栈信息，仅在开发时使用，生产环境始为null
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
}
