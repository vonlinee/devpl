package io.devpl.codegen.template.model;

import io.devpl.codegen.template.TemplateArguments;
import io.devpl.codegen.util.ClassUtils;
import io.devpl.sdk.util.CollectionUtils;
import lombok.Getter;
import lombok.Setter;
import org.beetl.android.util.ArrayMap;
import org.beetl.android.util.ArraySet;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 生成Java类的模板参数
 */
@Getter
@Setter
public class JavaFileTemplateArguments implements TemplateArguments {

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
    private Set<String> importItems;
    /**
     * 静态导入项
     */
    private Set<String> staticImportItem;
    /**
     * 父类型，只能有一个
     */
    private String superClass;

    /**
     * 父接口
     */
    private Set<String> superInterfaces;

    /**
     * 字段列表
     */
    private Set<FieldData> fields;

    /**
     * 类上的注解
     */
    private Set<String> annotations;

    /**
     * 方法列表
     */
    private Set<MethodData> methods;

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
            importItems = new HashSet<>(Arrays.asList(importTypes));
        } else {
            importItems.addAll(Arrays.asList(importTypes));
        }
    }

    public void addAnnotation(Class<? extends Annotation>... annotations) {
        if (this.annotations == null) {
            this.annotations = new ArraySet<>();
        }
    }

    public final void addSuperInterfaces(String... superInterfaces) {
        if (this.superInterfaces == null) {
            this.superInterfaces = new HashSet<>(Arrays.asList(superInterfaces));
        } else {
            this.superInterfaces.addAll(Arrays.asList(superInterfaces));
        }
    }

    public final void addSuperInterfaces(Class<?>... superInterfaces) {
        Objects.requireNonNull(superInterfaces, "super interfaces cannot be null");

        Set<String> packageNames = new HashSet<>();

        if (this.importItems == null) {
            this.importItems = new HashSet<>();
        }

        for (Class<?> superInterface : superInterfaces) {
            if (superInterface == null || !superInterface.isInterface()) {
                throw new IllegalArgumentException(superInterface + " is not a interface");
            }
            if (this.superInterfaces == null) {
                this.superInterfaces = new HashSet<>();
            }

            this.superInterfaces.add(ClassUtils.getSimpleName(superInterface.getName()));

            importItems.add(superInterface.getPackageName());
        }
    }

    public final void addField(FieldData fieldData) {
        if (this.fields == null) {
            this.fields = new HashSet<>();
        }
        this.fields.add(fieldData);
    }

    public void setImportItems(Collection<String> importItems) {
        if (this.importItems == null) {
            this.importItems = new HashSet<>(importItems);
        } else {
            this.importItems.clear();
            this.importItems.addAll(importItems);
        }
    }

    public void setFields(Collection<FieldData> fields) {
        this.fields = CollectionUtils.setAll(this.fields, fields);
    }

    public void setMethods(Collection<MethodData> methods) {
        this.methods = CollectionUtils.setAll(this.methods, methods);
    }
}
