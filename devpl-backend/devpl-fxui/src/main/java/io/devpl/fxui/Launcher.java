package io.devpl.fxui;

import io.devpl.fxui.app.ToolsApplication;
import javafx.application.Application;

import java.io.IOException;

public class Launcher {
    public static void main(String[] args) throws IOException {
        Application.launch(ToolsApplication.class, args);
    }
}
