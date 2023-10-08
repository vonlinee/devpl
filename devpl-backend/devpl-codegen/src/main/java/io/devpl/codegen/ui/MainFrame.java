package io.devpl.codegen.ui;

import io.devpl.codegen.sql.SqlUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Set;

public class MainFrame extends JFrame {

    JTextArea txaRestult = new JTextArea();

    public MainFrame() {
        this.setSize(600, 500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();

        this.add(jPanel);

        BorderLayout layout = new BorderLayout();
        layout.setHgap(10);
        layout.setVgap(10);

        jPanel.setLayout(layout);

        JTextArea jTextArea = new JTextArea();



        JButton btn = new JButton("解析");
        btn.setSize(100, 100);
        jPanel.add(btn, BorderLayout.CENTER);
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
        jPanel.add(btn, BorderLayout.NORTH);

        jTextArea.setSize(400, 400);
        txaRestult.setSize(400, 400);

        jPanel.add(jTextArea, BorderLayout.EAST);
        jPanel.add(txaRestult, BorderLayout.WEST);
    }

    public static void main(String[] args) {

        MainFrame main = new MainFrame();
        main.setVisible(true);
    }
}
