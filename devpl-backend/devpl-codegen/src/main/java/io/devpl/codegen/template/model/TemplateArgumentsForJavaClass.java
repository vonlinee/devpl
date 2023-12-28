package io.devpl.codegen.template.model;

import io.devpl.codegen.template.TemplateArguments;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 生成Java类的模板参数
 */
@Getter
@Setter
public class TemplateArgumentsForJavaClass implements TemplateArguments {

    /**
     * 包名
     */
    private String packageName;
    /**
     * 类型
     */
    private String className;
    /**
     * 类类型，枚举类，接口，类或者注解
     */
    private String classType;
    /**
     * 修饰符
     */
    private String modifier;
    /**
     * 导入的类型
     */
    private List<String> importItems;
    /**
     * 静态导入项
     */
    private List<String> staticImportItem;
    /**
     * 父类型，只能有一个
     */
    private String superClass;
    /**
     * 父接口
     */
    private List<String> superInterfaces;

    @Override
    public void fill(Map<String, Object> arguments) {
        arguments.put("packageName", this.packageName);
        arguments.put("className", this.className);
        arguments.put("classType", this.classType);
        arguments.put("modifier", this.modifier);
        arguments.put("importItems", this.importItems);
        arguments.put("staticImportItem", this.staticImportItem);
        arguments.put("superClass", this.superClass);
        arguments.put("superInterfaces", this.superInterfaces);
    }
}
