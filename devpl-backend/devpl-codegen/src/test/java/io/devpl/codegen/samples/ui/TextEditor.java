package io.devpl.codegen.samples.ui;

import javax.swing.*;

public class TextEditor extends JScrollPane {

    JTextArea textArea;

    public TextEditor() {
        this.textArea = new JTextArea();
        setViewportView(textArea);
        this.textArea.setVisible(true);
    }

    public void setText(String text) {
        textArea.setText(text);
    }
}
