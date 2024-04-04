package io.devpl.codegen.samples.ui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UIHelper {

    static {
        setLookAndFeel();
    }

    public static String readString(File file) {
        if (file == null || file.isDirectory()) {
            return "";
        }
        try {
            List<String> lines = new ArrayList<>();
            if (file.canRead()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                }
                return String.join("\n", lines);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    /**
     * <a href="https://www.formdev.com/flatlaf/">...</a>
     */
    private static void initialize() {
        FlatLightLaf.install();
        FlatArcIJTheme.install();
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
    }

    public static void setLookAndFeel() {
        initialize();
//        try {
//            // 设置外观
//            // 跨平台外观
//            // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//            // 和系统一样的外观
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
//                 IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static void center(JFrame frame) {
        // 获取屏幕的中心点
//        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
//
//        Rectangle bounds = gc.getBounds();
        // 获取屏幕的大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 计算中心点并设置JFrame的位置
        int centerX = (screenSize.width - frame.getWidth()) / 2;
        int centerY = (screenSize.height - frame.getHeight()) / 2;
        // 设置JFrame的位置
        frame.setLocation(centerX, centerY);
    }

    public static void showFrame(String title, JPanel rootPane, int w, int h) {
        JFrame jFrame = new JFrame(title);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(w, h);
        jFrame.getRootPane().setContentPane(rootPane);
        UIHelper.center(jFrame);
        jFrame.setVisible(true);
    }

    public static void runLater(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }
}
