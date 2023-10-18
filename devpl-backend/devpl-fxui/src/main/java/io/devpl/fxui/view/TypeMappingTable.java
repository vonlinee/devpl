package io.devpl.fxui.view;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.util.BindingMode;
import io.devpl.fxui.components.table.TableData;
import io.devpl.fxui.components.table.TableOperation;
import io.devpl.fxui.components.table.TablePane;
import io.devpl.fxui.components.table.TablePaneOption;
import io.devpl.fxui.mapper.DataTypeItemMapper;
import io.devpl.fxui.mapper.MyBatis;
import io.devpl.fxui.utils.FXUtils;
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
                        Field.ofStringType(formObject.typeKeyProperty())
                            .label("类型Key"),
                        Field.ofStringType(formObject.typeNameProperty())
                            .label("类型名称")
                            .required("This field can’t be empty")
                    )
                ).title("新增类型");
                loginForm.binding(BindingMode.CONTINUOUS);
                return loginForm;
            });

        TablePane<DataTypeItem> table = new TablePane<>(option);

        table.setTableOperation(new TableOperation<DataTypeModel, DataTypeItem>() {
            @Override
            public TableData<DataTypeItem> loadPage(int pageNum, int pageSize) {

                System.out.println(Thread.currentThread().getName());
                List<DataTypeItem> list = dataTypeItemMapper.selectPage((pageNum - 1) * pageSize, pageSize);
                long count = dataTypeItemMapper.count();
                return new TableData<>(list, count, pageNum, pageSize);
            }

            @Override
            public DataTypeItem convert(DataTypeModel formObject) {
                System.out.println(Thread.currentThread().getName());
                DataTypeItem dataTypeItem = new DataTypeItem();
                dataTypeItem.setTypeKey(formObject.getTypeKey());
                dataTypeItem.setTypeName(formObject.getTypeName());
                return dataTypeItem;
            }

            @Override
            public void save(DataTypeItem record) {
                System.out.println(Thread.currentThread().getName());
                int insert = dataTypeItemMapper.insert(record);
                System.out.println("插入" + insert + "条");
            }
        });

        setCenter(table);
    }
}
