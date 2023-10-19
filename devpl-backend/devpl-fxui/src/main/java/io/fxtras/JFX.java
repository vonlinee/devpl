package io.fxtras;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.util.Arrays;

public class JFX {

    private JFX() {
    }

    public static Stage newStage(String title, Window owner, Modality modality, Scene scene) {
        return newStage(title, owner, modality, scene, true);
    }

    public static Stage newStage(String title, Window owner, Modality modality, Scene scene, boolean resizable) {
        Stage stage = new Stage();
        if (title != null) stage.setTitle(title);
        if (owner != null) stage.initOwner(owner);
        if (modality != null) stage.initModality(modality);
        if (scene != null) stage.setScene(scene);
        stage.setMaximized(false);
        stage.setResizable(resizable);
        return stage;
    }

    public static MenuItem newMenuItem(String text, EventHandler<ActionEvent> actionEventHandler) {
        final MenuItem menuItem = new MenuItem(text);
        menuItem.setOnAction(actionEventHandler);
        return menuItem;
    }

    public static Stage newDialogStage(String title, Window owner, Scene scene) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initOwner(owner);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(true);
        return stage;
    }

    /**
     * 工具类
     */
    public static final StringConverter<String> DEFAULT_STRING_CONVERTER = new DefaultStringConverter();

    public static Button newButton(String text) {
        Button button = new Button();
        button.setText(text);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setDefaultButton(true);
        return button;
    }

    public static Button newButton(String text, EventHandler<? super MouseEvent> value) {
        Button button = new Button();
        button.setText(text);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setDefaultButton(true);
        button.setOnMouseClicked(value);
        return button;
    }

    public static Button newButton(String text, Node graph) {
        Button button = new Button();
        button.setText(text);
        button.setGraphic(graph);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setDefaultButton(true);
        return button;
    }

    public static Button newButton(String text, Node graph, boolean defaultButton) {
        Button button = new Button();
        button.setText(text);
        button.setGraphic(graph);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setDefaultButton(defaultButton);
        return button;
    }

    public static Button newButton(String text, Node graph, Pos alignment, boolean defaultButton) {
        Button button = new Button();
        button.setText(text);
        button.setGraphic(graph);
        button.setAlignment(alignment);
        button.setDefaultButton(defaultButton);
        return button;
    }

    /**
     * 给控件添加一个按钮
     *
     * @param group Group容器
     * @param text  按钮文本
     */
    public static Button addButton(Group group, String text, EventHandler<? super MouseEvent> value) {
        Button btn = newButton(text, value);
        group.getChildren().add(btn);
        return btn;
    }

    /**
     * 给控件添加一个按钮
     *
     * @param pane 容器面板
     * @param text 文本
     */
    public static Button addButton(Pane pane, String text, EventHandler<? super MouseEvent> value) {
        Button btn = newButton(text, value);
        pane.getChildren().add(btn);
        return btn;
    }

    /**
     * 加载图片
     *
     * @param pathname 相对路径
     * @param size     高度=宽度
     * @return ImageView
     */
    public static ImageView loadImageView(String pathname, double size) {
        ImageView dbImage = new ImageView(pathname);
        dbImage.setFitHeight(size);
        dbImage.setFitWidth(size);
        return dbImage;
    }

    /**
     * 加载图片
     *
     * @param pathname 相对路径
     * @param w        宽度
     * @param h        高度
     * @return ImageView
     */
    public static ImageView loadImageView(String pathname, double w, double h) {
        ImageView dbImage = new ImageView(pathname);
        dbImage.setFitHeight(h);
        dbImage.setFitWidth(w);
        return dbImage;
    }

    /**
     * ImageView对象是否可以缓存
     *
     * @param pathname 路径名称
     * @param size     图片尺寸大小
     * @param userData 节点缓存的数据
     * @return ImageView实例
     */
    public static ImageView loadImageView(String pathname, double size, Object userData) {
        ImageView dbImage = new ImageView(pathname);
        dbImage.setFitHeight(size);
        dbImage.setFitWidth(size);
        dbImage.setUserData(userData);
        return dbImage;
    }

    public static ImageView loadImageView(String pathname, double w, double h, Object userData) {
        ImageView dbImage = new ImageView(pathname);
        dbImage.setFitHeight(h);
        dbImage.setFitWidth(w);
        dbImage.setUserData(userData);
        return dbImage;
    }

    public static void setTooltip(Control control, String tipText) {
        control.setTooltip(new Tooltip(tipText));
    }

    // ================================ 集合封装 ===========================

    /**
     * Observable集合实质上也是对普通集合的包装
     *
     * @param elements 元素
     * @param <E>      元素类型
     * @return FXCollections.observableArrayList
     */
    public static <E> ObservableList<E> arrayOf(E[] elements) {
        return FXCollections.observableArrayList(elements);
    }

    public static <E> ObservableList<E> observableList(E[] elements) {
        return FXCollections.observableList(Arrays.asList(elements));
    }

    // ================================ 集合封装 ===========================

    /**
     * 不要在Controller的构造，initialize方法里调用
     *
     * @param node 节点
     * @param <W>  Window
     * @return Window
     */
    public static <W extends Window> W getStage(Node node) {
        @SuppressWarnings("unchecked") final W window = (W) node.getScene().getWindow();
        return window;
    }

    /**
     * 从事件获取当前事件源所在的舞台对象
     * When accessing a Stage, timing is important, as the Stage is not created
     * until the very end of a View-creation process.
     * <a href="https://edencoding.com/stage-controller/">...</a>
     *
     * @param event JavaFX event
     * @return 当前事件源所在的舞台对象
     * @throws RuntimeException 如果事件源不是Node
     */
    public static Stage getStage(Event event) {
        final Object source = event.getSource();
        if (source instanceof Node) {
            final Node node = (Node) source;
            final Scene scene = node.getScene();
            if (scene == null) {
                throw new RuntimeException("node [" + node + "] has not been bind to a scene!");
            }
            final Window window = scene.getWindow();
            if (window instanceof Stage) {
                return (Stage) window;
            }
            throw new RuntimeException("the window [" + window + "] is not a stage");
        } else {
            throw new RuntimeException("event source is not a node");
        }
    }

    public static Insets newInsets(double size) {
        return new Insets(size);
    }
}
