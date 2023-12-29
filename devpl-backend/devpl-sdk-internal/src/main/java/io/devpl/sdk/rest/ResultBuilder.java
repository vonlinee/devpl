package io.devpl.sdk.rest;

/**
 * 不对外暴露
 *
 * @param <T> Result携带的数据类型
 * @since 0.0.1
 */
public interface ResultBuilder<T> extends RestfulResultBuilder<Result<T>, ResultBuilder<T>> {

    ResultBuilder<T> setData(T data);

    default ResultBuilder<T> data(T data) {
        return setData(data);
    }
}
