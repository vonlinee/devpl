package io.devpl.tookit.fxui.view;

import io.fxtras.component.TableViewColumn;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TableColumninitializer<R> {

    public <C> List<TableColumn<R, C>> initColumns(Class<R> rowClass) {
        final Field[] declaredFields = rowClass.getDeclaredFields();
        final List<TableColumn<R, C>> columnsToBeAdd = new ArrayList<>();
        for (Field declaredField : declaredFields) {
            final TableViewColumn tvc = declaredField.getAnnotation(TableViewColumn.class);
            if (tvc == null) {
                continue;
            }
            final Class<?> type = declaredField.getType();
            final String propertyName = declaredField.getName();
            // 根据数据类型推断选择使用什么列
            TableColumn<R, C> column = new TableColumn<>(propertyName);
            column.setEditable(true);
            // 只支持单层对象
            column.setCellValueFactory(getCellValueFactory(propertyName));
            column.setCellFactory(getCellFactory(rowClass, type, propertyName));
            column.setText(tvc.name());
            columnsToBeAdd.add(column);
        }
        return columnsToBeAdd;
    }


    /**
     * 只适合单层对象，不适合嵌套对象
     * @param propertyName 属性名
     * @param <T>          列数据类型
     * @return 单元格工厂
     */
    @NonNull
    public <T> Callback<TableColumn.CellDataFeatures<R, T>, ObservableValue<T>> getCellValueFactory(String propertyName) {
        return new PropertyValueFactory<>(propertyName);
    }

    /**
     * 针对数据类的字段定制StringConverter
     * @param rowClass     行数据类
     * @param propertyType 属性字段类型
     * @param propertyName 属性名称
     * @param <C>          列数据类型
     * @return StringConverter实例
     */
    @NonNull
    public <C> StringConverter<C> getStringConverter(Class<R> rowClass, Class<?> propertyType, String propertyName) {
        return new StringConverter<>() {
            @Override
            public String toString(C object) {
                return String.valueOf(object);
            }

            @Override
            public C fromString(String string) {
                return null;
            }
        };
    }

    /**
     * 针对数据类的字段定制单元格工厂
     * @param rowClass     行数据类
     * @param propertyType 属性字段类型
     * @param propertyName 属性名称
     * @param <T>          列数据类型
     * @return StringConverter实例
     */
    @NonNull
    public <T> Callback<TableColumn<R, T>, TableCell<R, T>> getCellFactory(Class<R> rowClass, Class<?> propertyType, String propertyName) {
        return TextFieldTableCell.forTableColumn(getStringConverter(rowClass, propertyType, propertyName));
    }
}
