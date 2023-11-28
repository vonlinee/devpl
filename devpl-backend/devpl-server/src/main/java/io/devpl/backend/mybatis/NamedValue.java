package io.devpl.backend.mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NamedValue {

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private Object value;
}
