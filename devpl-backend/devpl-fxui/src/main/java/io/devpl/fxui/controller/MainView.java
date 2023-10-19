package io.devpl.fxui.controller;

import io.devpl.fxui.controller.dbconn.ConnManageView;
import io.devpl.fxui.event.DeleteConnEvent;
import io.devpl.fxui.model.ConnectionConfig;
import io.devpl.fxui.tools.mybatis.MyBatisXmlToolPane;
import io.devpl.fxui.mvvm.FxmlBinder;
import io.devpl.fxui.mvvm.FxmlView;
import io.fxtras.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 主视图
 */
@FxmlBinder(location = "layout/MainView.fxml", label = "主界面")
public class MainView extends FxmlView {

    @FXML
    public Tab tabMbg;
    @FXML
    public TabPane tabpContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tab tab = new Tab("MyBatis工具", new MyBatisXmlToolPane());
        tab.setClosable(false);
        tabpContainer.getTabs().add(tab);
    }

    @FXML
    public void showConnectionManagePane(MouseEvent mouseEvent) {
        StageManager.show(ConnManageView.class);
    }

    /**
     * 添加新连接，点击每个连接将填充子TreeItem
     * @param connectionInfo 连接信息
     */
    @Subscribe(name = "add-new-connection", threadMode = ThreadMode.BACKGROUND)
    public void addConnection(ConnectionConfig connectionInfo) {
        // trvDbNavigation.addConnection(connectionInfo);
    }

    /**
     * 删除数据库连接
     * @param event
     */
    @Subscribe
    public void removeConnection(DeleteConnEvent event) {
        // ObservableList<TreeItem<String>> children = trvDbNavigation.getRoot().getChildren();
        // Iterator<TreeItem<String>> iterator = children.iterator();
        // for (String connectionName : event.getConnectionNames()) {
        //     while (iterator.hasNext()) {
        //         TreeItem<String> next = iterator.next();
        //         if (next.getValue().equals(connectionName)) {
        //             iterator.remove();
        //         }
        //     }
        // }
    }
}
