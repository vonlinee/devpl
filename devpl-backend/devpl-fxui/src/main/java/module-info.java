module devpl.fxui {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    exports io.devpl.fxui to javafx.graphics;
    exports io.devpl.fxui.model;

    opens io.devpl.fxui.model;

    requires com.jfoenix;
}
