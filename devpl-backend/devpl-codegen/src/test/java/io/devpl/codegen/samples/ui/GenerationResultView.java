package io.devpl.codegen.samples.ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GenerationResultView extends JPanel {

    FileTreeView fileTreeView = new FileTreeView();
    TextEditor textEditor;

    public GenerationResultView(File rootDir) {
        setSize(800, 600);
        BorderLayout layout = new BorderLayout();

        setLayout(layout);
        this.setLayout(layout);

        textEditor = new TextEditor();
        this.add(fileTreeView, BorderLayout.WEST);

        fileTreeView.setRootDirectory(rootDir);

        fileTreeView.setNodeSelectionHandler(file -> {
            textEditor.setText(UIHelper.readString(file));
        });

        this.add(textEditor, BorderLayout.CENTER);
    }
}
