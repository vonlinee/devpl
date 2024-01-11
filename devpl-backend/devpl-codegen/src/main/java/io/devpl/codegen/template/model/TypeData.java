package io.devpl.codegen.template.model;

import io.devpl.codegen.template.TemplateArguments;

import java.util.*;

/**
 * 生成Java类的模板参数
 */
public class TypeData implements TemplateArguments {

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

    /**
     * 字段列表
     */
    private List<FieldData> fields;

    /**
     * 方法列表
     */
    private List<MethodData> methods;

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
        arguments.put("fields", this.fields);
        arguments.put("methods", this.methods);
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();
        fill(map);
        return map;
    }

    public final void addImport(String... importTypes) {
        if (importItems == null) {
            importItems = new ArrayList<>(Arrays.asList(importTypes));
        } else {
            importItems.addAll(Arrays.asList(importTypes));
        }
    }

    public final void addSuperInterfaces(String... superInterfaces) {
        if (this.superInterfaces == null) {
            this.superInterfaces = new ArrayList<>(Arrays.asList(superInterfaces));
        } else {
            this.superInterfaces.addAll(Arrays.asList(superInterfaces));
        }
    }

    public final void addField(FieldData fieldData) {
        if (this.fields == null) {
            this.fields = new ArrayList<>();
        }
        this.fields.add(fieldData);
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public List<String> getImportItems() {
        return importItems;
    }

    public void setImportItems(List<String> importItems) {
        this.importItems = importItems;
    }

    public List<String> getStaticImportItem() {
        return staticImportItem;
    }

    public void setStaticImportItem(List<String> staticImportItem) {
        this.staticImportItem = staticImportItem;
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public List<String> getSuperInterfaces() {
        return superInterfaces;
    }

    public void setSuperInterfaces(List<String> superInterfaces) {
        this.superInterfaces = superInterfaces;
    }

    public List<FieldData> getFields() {
        return fields;
    }

    public void setFields(List<FieldData> fields) {
        this.fields = fields;
    }

    public List<MethodData> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodData> methods) {
        this.methods = methods;
    }
}
