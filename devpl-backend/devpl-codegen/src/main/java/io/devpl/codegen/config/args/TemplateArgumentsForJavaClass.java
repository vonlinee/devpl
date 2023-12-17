package io.devpl.codegen.config.args;

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

    private String packageName;
    private String className;
    /**
     * 类类型，枚举类，接口，类或者注解
     */
    private String classType;
    private String modifier;
    private List<String> importItems;
    private List<String> staticImportItem;
    /**
     * 父类型
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
