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
import io.devpl.fxui.utils.FXControl;
import io.devpl.fxui.utils.FXUtils;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

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
                            .placeholder("typeKey")
                            .label("类型Key"),
                        Field.ofStringType(formObject.typeNameProperty())
                            .placeholder("Unknown")
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
            public List<DataTypeItem> loadPageData(int pageNum, int pageSize) {
                return dataTypeItemMapper.selectPage(pageNum, pageSize);
            }

            @Override
            public DataTypeModel convert(DataTypeModel oldForm, DataTypeItem row) {

                oldForm.setTypeKey(row.getTypeKey());
                oldForm.setTypeName(row.getTypeName());

                return oldForm;
            }

            @Override
            public DataTypeItem convert(DataTypeModel formObject) {
                DataTypeItem dataTypeItem = new DataTypeItem();
                dataTypeItem.setTypeKey(formObject.getTypeKey());
                dataTypeItem.setTypeName(formObject.getTypeName());
                return dataTypeItem;
            }

            @Override
            public void save(DataTypeItem record) {
                int insert = dataTypeItemMapper.insert(record);
                System.out.println("插入" + insert + "条");
            }

            @Override
            public void update(DataTypeItem record) {
            }

            @Override
            public void delete(DataTypeItem record) {
                int i = dataTypeItemMapper.deleteById(record.getId());
                System.out.println("删除" + i + "条");
            }
        });

        setCenter(table);

        HBox hBox = new HBox();

        hBox.getChildren().add(FXControl.button("111", event -> {
            List<Integer> selectedIndeies = table.getSelectedIndeies();
            System.out.println(selectedIndeies);
        }));

        setBottom(hBox);
    }
}
