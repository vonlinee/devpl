package io.devpl.codegen.template.model;

import io.devpl.codegen.template.TemplateArguments;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Modifier;

@Setter
@Getter
public class FieldData implements TemplateArguments {

    private String name; // 字段名称
    private String modifier; // 访问修饰符
    private String dataType; // 数据类型
    private String comment; // 注释信息
    private boolean staticFlag; // 是否静态
    private boolean commentOn = true; // 是否开启注释

    public void setModifier(int modifier) {
        this.modifier = Modifier.toString(modifier);
    }

    public void setModifier(javax.lang.model.element.Modifier modifier) {
        this.modifier = modifier.toString();
    }
}
