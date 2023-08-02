package io.devpl.generator.common;

import java.util.List;

/**
 * 解封装
 */
public interface ApiService {

    void setAddress(String address);

    <T> List<T> getForList(String api, Class<T> dataClass);
}
