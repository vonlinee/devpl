package io.devpl.fxui.common;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.lang.reflect.Field;

public class FieldValueFactory<S, T> implements Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> {

    Field field;

    public FieldValueFactory(Field field) {
        this.field = field;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ObservableValue<T> call(TableColumn.CellDataFeatures<S, T> param) {
        try {
            return (ObservableValue<T>) field.get(param.getValue());
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
