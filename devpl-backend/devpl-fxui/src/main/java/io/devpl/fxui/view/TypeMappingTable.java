package io.devpl.fxui.view;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.util.BindingMode;
import io.devpl.fxui.components.table.TableOperation;
import io.devpl.fxui.components.table.TablePane;
import io.devpl.fxui.components.table.TablePaneOption;
import io.devpl.fxui.mapper.DataTypeItemMapper;
import io.devpl.fxui.mapper.MyBatis;
import io.devpl.fxui.utils.FXUtils;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.layout.BorderPane;

import java.util.List;

public class TypeMappingTable extends BorderPane {

    DataTypeItemMapper dataTypeItemMapper = MyBatis.getMapper(DataTypeItemMapper.class);

    public TypeMappingTable() {
        setCenter(FXUtils.createTableView(DataTypeItem.class));

        TablePaneOption option = TablePaneOption
            .model(DataTypeItem.class)
            .enablePagination(true)
            .enableToolbar(true)
            .form(new DataTypeModel(), formObject -> {
                Form loginForm = Form.of(
                    Group.of(
                        Field.ofSingleSelectionType(new SimpleListProperty<>(FXCollections.observableArrayList(List.of("JSON", "Java", "JDBC"))), formObject.typeGroupProperty())
                            .label("类型分组")
                            .placeholder("")
                            .select(0),
                        Field.ofStringType(formObject.typeKeyProperty())
                            .placeholder("typeKey")
                            .label("类型Key"),
                        Field.ofStringType(formObject.typeNameProperty())
                            .placeholder("Unknown")
                            .label("类型名称"),
                        Field.ofStringType(formObject.descriptionProperty())
                            .placeholder("")
                            .label("描述信息")
                    )
                ).title("新增类型");
                loginForm.binding(BindingMode.CONTINUOUS);
                return loginForm;
            });

        TablePane<DataTypeItem> table = new TablePane<>(DataTypeItem.class, option);

        table.setTableOperation(new TableOperation<DataTypeModel, DataTypeItem>() {
            @Override
            public List<DataTypeItem> loadPageData(int pageNum, int pageSize) {
                return dataTypeItemMapper.selectPage(pageNum, pageSize);
            }

            @Override
            public DataTypeItem extractForm(DataTypeModel oldForm, DataTypeItem row) {
                if (row == null) {
                    row = new DataTypeItem();
                }
                row.setTypeKey(oldForm.getTypeKey());
                row.setTypeName(oldForm.getTypeName());
                row.setTypeGroupId(oldForm.getTypeGroup());
                return row;
            }

            @Override
            public void fillForm(int rowIndex, DataTypeItem row, DataTypeModel formObject) {
                if (row != null) {
                    formObject.setTypeGroup(row.getTypeGroupId());
                    formObject.setTypeName(row.getTypeName());
                    formObject.setTypeKey(row.getTypeKey());
                } else {
                    formObject.setTypeGroup("Java");
                    formObject.setTypeName("typeName");
                    formObject.setTypeKey("typeKey");
                }
            }

            @Override
            public void save(DataTypeItem record) {
                int res = dataTypeItemMapper.insert(record);
                if (res > 0) {
                    record.setId((long) res);
                }
            }

            @Override
            public void update(DataTypeItem record) {
                if (record.getId() == null) {
                    return;
                }
                int insert = dataTypeItemMapper.updateById(record);
            }

            @Override
            public void delete(DataTypeItem record) {
                int i = dataTypeItemMapper.deleteById(record.getId());
            }
        });
        setCenter(table);
    }
}
