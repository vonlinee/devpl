package io.devpl.fxui.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DataTypeModel {

    private final StringProperty typeKey = new SimpleStringProperty();
    private final StringProperty typeName = new SimpleStringProperty();

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

    @Override
    public String toString() {
        return "DataTypeModel{" +
            "typeKey=" + typeKey.get() +
            ", typeName=" + typeName.get() +
            '}';
    }
}
