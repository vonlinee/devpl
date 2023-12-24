module devpl.fxui {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires devpl.sdk.internal;
    requires devpl.codegen;

    exports io.devpl.fxui to javafx.graphics;
    exports io.devpl.fxui.model;

    opens io.devpl.fxui.model;

    requires com.jfoenix;
}
