package io.devpl.generator.tools.parser.java;

public class FieldsData {
    private String name;
    private String typeName;
    private String className;

    public FieldsData(String name, String typeName, String className) {
        this.name = name;
        this.typeName = typeName;
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "FieldsData{" + "className='" + className + '\'' + ", name='" + name + '\'' + ", typeName='" + typeName + '\'' + '}';
    }
}
