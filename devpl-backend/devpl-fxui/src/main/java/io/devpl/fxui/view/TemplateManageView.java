package io.devpl.fxui.view;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import io.devpl.fxui.components.table.TableData;
import io.devpl.fxui.components.table.TableOperation;
import io.devpl.fxui.components.table.TablePane;
import io.devpl.fxui.components.table.TablePaneOption;
import io.devpl.fxui.utils.Utils;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;

public class TemplateManageView extends BorderPane {

    public TemplateManageView() {

//        TableView<TemplateInfo> tableView = FXUtils.createTableView(TemplateInfo.class);
//
//        setCenter(tableView);

        TablePane<TemplateInfo> table = new TablePane<>(TemplateInfo.class);

        table.setTableOperation(new TableOperation<>() {

            @Override
            public TableData<TemplateInfo> loadPage(int pageNum, int pageSize) {
                List<TemplateInfo> list = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    TemplateInfo templateInfo = new TemplateInfo();
                    templateInfo.setTemplateName("Template" + i);
                    list.add(templateInfo);
                }
                Utils.println("load page %s pageSize = %s", pageNum, pageSize);
                return TableData.of(list);
            }

            @Override
            public void save(TemplateInfo record) {

            }

            @Override
            public void saveBatch(List<TemplateInfo> records) {

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
