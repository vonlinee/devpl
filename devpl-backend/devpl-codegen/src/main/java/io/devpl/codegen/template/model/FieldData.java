package io.devpl.codegen.template.model;

import io.devpl.codegen.template.TemplateArguments;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldData implements TemplateArguments {

    private String name; // 字段名称
    private String modifier; // 访问修饰符
    private String dataType; // 数据类型
    private String comment; // 注释信息
    private boolean staticFlag; // 是否静态
    private boolean commentOn = true; // 是否开启注释
}
