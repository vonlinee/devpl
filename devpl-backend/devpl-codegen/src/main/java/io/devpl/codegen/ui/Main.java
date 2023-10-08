package io.devpl.codegen.ui;

import io.devpl.codegen.sql.SqlUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Set;

public class Main extends JFrame {

    JTextArea txaRestult = new JTextArea();

    public Main() {
        this.setSize(600, 500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();

        this.add(jPanel);

        jPanel.setLayout(new GridLayout());

        JTextArea jTextArea = new JTextArea();

        jTextArea.setSize(400, 400);

        jPanel.add(jTextArea);
        jPanel.add(txaRestult);

        JButton btn = new JButton("解析");
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String text = jTextArea.getText();
                if (text == null || text.isBlank()) {
                    return;
                }
                Map<String, Set<String>> selectColumns = SqlUtils.getSelectColumns(text);
                txaRestult.setText(String.valueOf(selectColumns));
            }
        });
        jPanel.add(btn);
    }

    public static void main(String[] args) {

        Main main = new Main();
        main.setVisible(true);
    }
}
