package io.devpl.codegen.samples.ui;

import javax.swing.*;
import java.awt.*;

public class CodeGeneratorApplication extends JFrame {

    FileTreeView fileTreeView = new FileTreeView();
    TextEditor textEditor;

    public CodeGeneratorApplication() throws HeadlessException {
        setTitle("代码生成器");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BorderLayout layout = new BorderLayout();

        setLayout(layout);

        Container contentPane = getContentPane();

        contentPane.setLayout(layout);

        textEditor = new TextEditor();

        contentPane.add(fileTreeView, BorderLayout.WEST);

        fileTreeView.setNodeSelectionHandler(file -> {
            textEditor.setText(UIHelper.readString(file));
        });

        contentPane.add(textEditor, BorderLayout.CENTER);

        UIHelper.center(this);
    }

    public void launch() {
        setVisible(true);
    }

    public static void start() {
        new CodeGeneratorApplication().launch();
    }

    public static void main(String[] args) {
        CodeGeneratorApplication.start();
    }
}
