package io.devpl.codegen.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BorderLayoutDemo {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private JLabel msglabel;

    public BorderLayoutDemo() {
        prepareGUI();
    }

    public static void main(String[] args) {
        BorderLayoutDemo swingLayoutDemo = new BorderLayoutDemo();
        swingLayoutDemo.showBorderLayoutDemo();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Java SWING BorderLayout示例(yiibai.com)");
        mainFrame.setSize(600, 600);
        mainFrame.setLayout(new GridLayout(3, 1));

        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setSize(350, 100);

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        controlPanel.setSize(500, 500);

        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
    }

    private void showBorderLayoutDemo() {
        headerLabel.setText("Layout in action: BorderLayout");

        JPanel panel = new JPanel();
        panel.setBackground(Color.darkGray);
        panel.setSize(600, 500);
        BorderLayout layout = new BorderLayout();
        layout.setHgap(10);
        layout.setVgap(10);

        panel.setLayout(layout);
        panel.add(new JButton("中心按钮"), BorderLayout.CENTER);
        panel.add(new JButton("行开始"), BorderLayout.LINE_START);
        panel.add(new JButton("行结束"), BorderLayout.LINE_END);
        panel.add(new JButton("东侧"), BorderLayout.EAST);
        panel.add(new JButton("西侧"), BorderLayout.WEST);
        panel.add(new JButton("北侧"), BorderLayout.NORTH);
        panel.add(new JButton("南侧"), BorderLayout.SOUTH);

        controlPanel.add(panel);
        mainFrame.setVisible(true);
    }
}
