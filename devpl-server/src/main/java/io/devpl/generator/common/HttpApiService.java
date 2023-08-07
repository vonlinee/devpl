package io.devpl.generator.common;

import java.util.List;

/**
 * 解封装
 */
public interface HttpApiService {

    void setApiAddress(String address);

    <T> List<T> getForList(String api, Class<T> dataClass);
}
