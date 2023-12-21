package io.devpl.fxui.model;

public class FieldNode {

    private String name;
    private String description;
    private String dataType;

    public FieldNode() {
    }

    public FieldNode(String name) {
        this.name = name;
        this.description = name;
    }

    public FieldNode(String name, String description, String dataType) {
        this.name = name;
        this.description = description;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "FieldNode{" +
            "name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", dataType='" + dataType + '\'' +
            '}';
    }
}
