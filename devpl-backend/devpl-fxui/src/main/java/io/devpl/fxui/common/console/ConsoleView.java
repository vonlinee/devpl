package io.devpl.fxui.common.console;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsoleView extends BorderPane {

    private final PrintStream out;
    private final TextArea textArea;
    private final TextField title;
    private final InputStream in;
    private boolean pauseBeforeExit = true;
    private Runnable cli;
    ExecutorService exe;
//        final String[] args;

    public ConsoleView() {
        this(Charset.defaultCharset());
    }

    public ConsoleView(Charset charset) {

//      this.prefWidthProperty().bind(new SimpleDoubleProperty(this.getParent().prefWidth(USE_PREF_SIZE)));
        this.textArea = new TextArea();
        this.title = new TextField();
        this.textArea.setWrapText(true);
        KeyBindingUtils.installEmacsKeyBinding(this.textArea);
        setCenter(this.textArea);
        setBottom(title);

        final TextInputControlStream stream = new TextInputControlStream(this.textArea, Charset.defaultCharset());
        try {
            this.out = new PrintStream(stream.getOut(), true, charset.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        this.in = stream.getIn();

        exe = Executors.newFixedThreadPool(2);
        exe.submit(() -> {
            System.setOut(this.out);
            System.setIn(this.in);
            System.setErr(this.out);
        });

        final ContextMenu menu = new ContextMenu();
        menu.getItems()
                .add(createItem("Clear console", e -> {
                    try {
                        stream.clear();
                        this.textArea.clear();
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                }));
        this.textArea.setContextMenu(menu);

//    setPrefWidth(600);
//    setPrefHeight(400);
    }

    private MenuItem createItem(String name, EventHandler<ActionEvent> a) {
        final MenuItem menuItem = new MenuItem(name);
        menuItem.setOnAction(a);
        return menuItem;
    }

    public void setTitle(final String title) {
        Platform.runLater(() -> this.title.setText(title));
    }
//    protected final void setPauseBeforeExit(final boolean pauseBeforeExit) {
//    this.pauseBeforeExit = pauseBeforeExit;
//  }

    public void invokeMain(Runnable e) {
        exe.submit(e);
// new Thread(e).start();
    }

    ;
}
