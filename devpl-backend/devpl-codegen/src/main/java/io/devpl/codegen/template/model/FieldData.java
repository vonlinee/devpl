package io.devpl.codegen.template.model;

import io.devpl.codegen.template.TemplateArguments;

import java.lang.reflect.Modifier;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isStaticFlag() {
        return staticFlag;
    }

    public void setStaticFlag(boolean staticFlag) {
        this.staticFlag = staticFlag;
    }

    public boolean isCommentOn() {
        return commentOn;
    }

    public void setCommentOn(boolean commentOn) {
        this.commentOn = commentOn;
    }
}
