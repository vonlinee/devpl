package io.devpl.sdk.rest;

/**
 * @param <T> 携带的数据类型
 * @since 0.0.1
 */
public class Result<T> extends RestfulResultTemplate implements ResultBuilder<T> {

    /**
     * 存放具体的业务数据
     */
    private T data;

    Result() {
        super();
    }

    Result(Status status, String toast) {
        this(status.getCode(), status.getMessage(), null, toast);
    }

    Result(int code, String message) {
        this(code, message, null, null);
    }

    Result(int code, String message, String toast) {
        this(code, message, null, toast);
    }

    Result(int code, String message, T data, String toast) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
        this.toast = toast;
    }

    @Override
    public Result<T> build() {
        if (this.code == 0) this.code = -1;
        if (this.message == null) this.message = "";
        return this;
    }

    @Override
    public ResultBuilder<T> setCode(int code) {
        this.code = code;
        return this;
    }

    @Override
    public ResultBuilder<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public ResultBuilder<T> setThrowable(Throwable throwable) {
        if (this.throwable == null || throwable != this.throwable) {
            this.stacktrace = getStackTrace(throwable);
        }
        this.throwable = throwable;
        return this;
    }

    @Override
    public ResultBuilder<T> setToast(String toastMessage) {
        this.toast = toastMessage;
        return this;
    }

    @Override
    public ResultBuilder<T> setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
        return this;
    }

    @Override
    public ResultBuilder<T> setData(T data) {
        this.data = data;
        return this;
    }

    public T getData() {
        return data;
    }

    @Override
    protected void toJSONString(StringBuilder result) {

    }
}
