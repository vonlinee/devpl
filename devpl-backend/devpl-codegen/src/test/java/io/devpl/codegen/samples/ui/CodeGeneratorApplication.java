package io.devpl.codegen.samples.ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class CodeGeneratorApplication extends JFrame {

    FileTreeView fileTreeView = new FileTreeView();
    TextEditor textEditor;

    public CodeGeneratorApplication() throws HeadlessException {

        UIHelper.setLookAndFeel();

        setTitle("代码生成器");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BorderLayout layout = new BorderLayout();

        setLayout(layout);

        Container contentPane = getContentPane();

        JSplitPane splitPane = new JSplitPane();

        contentPane.setLayout(layout);

        textEditor = new TextEditor();

        splitPane.setLeftComponent(fileTreeView);
        splitPane.setRightComponent(textEditor);

        contentPane.add(splitPane, BorderLayout.CENTER);

        fileTreeView.setNodeSelectionHandler(file -> {
            textEditor.setText(UIHelper.readString(file));
        });

        fileTreeView.setRootDirectory(new File("D:/Temp"));

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
