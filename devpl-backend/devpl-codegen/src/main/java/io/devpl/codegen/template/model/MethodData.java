package io.devpl.codegen.template.model;

import io.devpl.codegen.template.TemplateArguments;
import lombok.Getter;
import lombok.Setter;

/**
 * 方法信息
 */
@Getter
@Setter
public class MethodData implements TemplateArguments {

    private String name;
    // 参数个数
    private int paramCount;
    private String returnType;
    private boolean commentOn = true; // 是否开启注释
}
