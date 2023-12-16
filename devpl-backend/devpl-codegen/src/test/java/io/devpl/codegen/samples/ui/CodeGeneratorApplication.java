package io.devpl.codegen.samples.ui;

import javax.swing.*;
import java.awt.*;

/**
 * 提供一个UI界面，除Swing外没有其他任何依赖，只有这一个文件
 */
public class CodeGeneratorApplication extends JFrame {

    public CodeGeneratorApplication() throws HeadlessException {
        setTitle("代码生成器");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
