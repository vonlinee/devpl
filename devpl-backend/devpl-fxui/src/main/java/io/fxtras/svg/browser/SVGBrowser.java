package io.fxtras.svg.browser;

import io.fxtras.control.SplitPane;
import io.fxtras.svg.SVGImage;
import io.fxtras.svg.SVGLoader;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * SVG 浏览器
 */
public class SVGBrowser extends Application {
    private Stage stage = null;
    private final MenuBar menuBar = new MenuBar();
    private final TabPane tabPane = new TabPane();
    private final Map<Integer, SVGImage> imagesByIndex = new HashMap<>();

    public static void main(String[] args) {
        launch(args);
    }

    private Image newImage(String name) {
        URL url = this.getClass().getResource(name);
        return new Image(Objects.requireNonNull(url, "image url is null").toString());
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("SVG Browser");
        stage.setOnHidden(t -> {
            stage.close();
            System.exit(0);
        });

        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);

        MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(t -> open());

        MenuItem saveItem = new MenuItem("Save Image");
        saveItem.setOnAction(t -> save());

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(t -> {
            stage.close();
            System.exit(0);
        });
        fileMenu.getItems().addAll(openItem, saveItem, exitItem);

        ToolBar toolBar = new ToolBar();

        toolBar.getItems().addAll(newImageButton("zoomIn.png", t -> zoomIn()), newImageButton("zoomOut.png", t -> zoomOut()));

        VBox vBox = new VBox();
        vBox.getChildren().addAll(menuBar, toolBar, tabPane);

        stage.setScene(new Scene(vBox, 600, 600));
        stage.show();
    }

    private Button newImageButton(String name, EventHandler<ActionEvent> actionEventEventHandler) {
        Button zoomOut = new Button("", new ImageView(newImage(name)));
        zoomOut.setOnAction(actionEventEventHandler);
        return zoomOut;
    }

    /**
     * 缩小
     */
    private void zoomIn() {
        SVGImage image = getSelectedImage();
        if (image != null) {
            // 调用无效
            // image.scale(1.2d);
            double scaledWidth = image.getScaledWidth() * 0.9;
            double scaledHeight = image.getScaledHeight() * 0.9;
            image.setScaleX(scaledWidth);
            image.setScaleY(scaledHeight);
        }
    }

    /**
     * 增大
     */
    private void zoomOut() {
        SVGImage image = getSelectedImage();
        if (image != null) {
            double scaledWidth = image.getScaledWidth() * 1.1;
            double scaledHeight = image.getScaledHeight() * 1.1;
            image.setScaleX(scaledWidth);
            image.setScaleY(scaledHeight);
        }
    }

    private Node outerNode(Node node) {
        return centeredNode(node);
    }

    private Node centeredNode(Node node) {
        VBox vBox = new VBox(node);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    public boolean isEmpty() {
        return tabPane.getTabs().isEmpty();
    }

    public SVGImage getSelectedImage() {
        if (isEmpty()) {
            return null;
        }
        int index = tabPane.getSelectionModel().getSelectedIndex();
        return imagesByIndex.getOrDefault(index, null);
    }

    private void save() {
        SVGImage svgImage = getSelectedImage();
        if (svgImage == null) {
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PNG Files", "*.png"), new ExtensionFilter("JPEG Files", "*.jpg", ".jpeg"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Save as Image");
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            return;
        }
        String name = file.getName();
        int index = name.lastIndexOf('.');
        String ext = null;
        if (index < name.length() - 1) {
            ext = name.substring(index + 1);
        }
        if (ext != null && !ext.equals("png") && !ext.equals("jpg") && !ext.equals("jpeg")) {
            name = name + ".png";
            ext = "png";
            file = new File(file.getParentFile(), name);
        } else {
            ExtensionFilter filter = fileChooser.getSelectedExtensionFilter();
            if (filter == null) {
                name = name + ".png";
                ext = "png";
                file = new File(file.getParentFile(), name);
            }
        }
        if (ext == null) {
            ext = "png";
        } else if (ext.equals("jpeg")) {
            ext = "jpg";
        }
        svgImage.snapshot(ext, file);
    }

    /**
     * 上一次选择的目录
     */
    File lastDir;
    FileChooser fileChooser = new FileChooser();

    private void open() {
        fileChooser.setInitialDirectory(lastDir == null ? new File(System.getProperty("user.dir")) : lastDir);
        fileChooser.setTitle("Open SVG File");
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }

        lastDir = file.getParentFile();

        try {
            SVGImage image = SVGLoader.load(file.toURI().toURL());
            if (image == null) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid SVG file");
                alert.setContentText("The file appear not to be a valid SVG file");
                alert.showAndWait();
                return;
            }

            Group group = new Group(image);
            MyStackPane content = new MyStackPane(group);
            group.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
                // keep it at least as large as the content
                content.setMinWidth(newBounds.getWidth());
                content.setMinHeight(newBounds.getHeight());
            });

            VBox box = new VBox();

            SplitPane splitPane = new SplitPane();
            splitPane.setOrientation(Orientation.VERTICAL);
            ScrollPane scrollPane = new ScrollPane(content);
            scrollPane.setPannable(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefSize(500, 500);

            box.getChildren().addAll(scrollPane);

            TextArea textArea = new TextArea();
            textArea.setText(Files.readString(file.toPath()));

            splitPane.getItems().addAll(scrollPane, textArea);

            Tab tab = new Tab(file.getName(), splitPane);
            content.allowLayoutChildren(false);
            content.setOnScroll(event -> {
                double zoomFactor = 1.05;
                double deltaY = event.getDeltaY();
                if (deltaY < 0) {
                    zoomFactor = 1 / zoomFactor;
                }
                int index = tabPane.getSelectionModel().getSelectedIndex();
                if (imagesByIndex.containsKey(index)) {
                    // https://stackoverflow.com/questions/38604780/javafx-zoom-scroll-in-scrollpane
                    Node node = imagesByIndex.get(index);
                    Bounds groupBounds = node.getBoundsInLocal();
                    final Bounds viewportBounds = scrollPane.getViewportBounds();

                    double valX = scrollPane.getHvalue() * (groupBounds.getWidth() - viewportBounds.getWidth());
                    double valY = scrollPane.getVvalue() * (groupBounds.getHeight() - viewportBounds.getHeight());

                    // scale
                    node.setScaleX(node.getScaleX() * zoomFactor);
                    node.setScaleY(node.getScaleY() * zoomFactor);

                    Point2D posInZoomTarget = node.parentToLocal(new Point2D(event.getX(), event.getY()));
                    Point2D adjustment = node.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));
                    scrollPane.layout();
                    scrollPane.setViewportBounds(groupBounds);

                    groupBounds = group.getBoundsInLocal();
                    scrollPane.setHvalue((valX + adjustment.getX()) / (groupBounds.getWidth() - viewportBounds.getWidth()));
                    scrollPane.setVvalue((valY + adjustment.getY()) / (groupBounds.getHeight() - viewportBounds.getHeight()));
                }
            });
            tabPane.getTabs().add(tab);
            int tabIndex = tabPane.getTabs().size() - 1;
            imagesByIndex.put(tabIndex, image);
            tab.setOnClosed(event -> imagesByIndex.remove(tabIndex));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static class MyStackPane extends StackPane {
        private boolean allowLayoutChildren = true;

        private MyStackPane(Node root) {
            super(root);
        }

        private void allowLayoutChildren(boolean allow) {
            this.allowLayoutChildren = allow;
        }

        @Override
        public void layoutChildren() {
            if (allowLayoutChildren) {
                super.layoutChildren();
            }
        }
    }
}
