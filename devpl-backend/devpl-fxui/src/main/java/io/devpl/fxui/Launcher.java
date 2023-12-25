package io.devpl.fxui;

import io.devpl.fxui.app.TestApplication;
import javafx.application.Application;

import java.io.IOException;

public class Launcher {
    public static void main(String[] args) throws IOException {
        Application.launch(TestApplication.class, args);
    }
}
