package io.devpl.generator.mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NamedValue {

    private String name;

    private Object value;
}
