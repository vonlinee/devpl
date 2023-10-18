package io.devpl.fxui.view;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import io.devpl.fxui.components.table.TableData;
import io.devpl.fxui.components.table.TableOperation;
import io.devpl.fxui.components.table.TablePane;
import io.devpl.fxui.mapper.DataTypeItemMapper;
import io.devpl.fxui.mapper.MyBatis;
import io.devpl.fxui.utils.FXUtils;
import javafx.scene.layout.BorderPane;

public class TypeMappingTable extends BorderPane {

    DataTypeItemMapper dataTypeItemMapper = MyBatis.getMapper(DataTypeItemMapper.class);

    public TypeMappingTable() {
        setCenter(FXUtils.createTableView(DataTypeItem.class));

        TablePane<DataTypeItem> table = new TablePane<>(DataTypeItem.class);

        table.setTableOperation(new TableOperation<>() {
            @Override
            public TableData<DataTypeItem> loadPage(int pageNum, int pageSize) {
                return TableData.of(dataTypeItemMapper.selectPage(pageNum, pageSize));
            }
        });

        setCenter(table);

        UserModel model = new UserModel();

        Form loginForm = Form.of(
            Group.of(
                Field.ofStringType(model.usernameProperty())
                    .label("Username"),
                Field.ofStringType(model.passwordProperty())
                    .label("Password")
                    .required("This field can’t be empty")
            )
        ).title("Login");

        table.setAddForm(new FormRenderer(loginForm));

    }
}
