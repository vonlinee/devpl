package io.devpl.fxui.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @see DataTypeItem
 */
public class DataTypeModel {

    private final ObjectProperty<String> typeGroup = new SimpleObjectProperty<>("");
    private final StringProperty typeKey = new SimpleStringProperty("");
    private final StringProperty typeName = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");

    public String getTypeKey() {
        return typeKey.get();
    }

    public StringProperty typeKeyProperty() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey.set(typeKey);
    }

    public String getTypeName() {
        return typeName.get();
    }

    public StringProperty typeNameProperty() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName.set(typeName);
    }

    public String getTypeGroup() {
        return typeGroup.get();
    }

    public ObjectProperty<String> typeGroupProperty() {
        return typeGroup;
    }

    public void setTypeGroup(String typeGroup) {
        this.typeGroup.set(typeGroup);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public String toString() {
        return "DataTypeModel{" +
            "typeGroup=" + typeGroup.get() +
            ", typeKey=" + typeKey.get() +
            ", typeName=" + typeName.get() +
            '}';
    }
}
