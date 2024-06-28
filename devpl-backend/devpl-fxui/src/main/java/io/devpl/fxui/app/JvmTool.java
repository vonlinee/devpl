package io.devpl.fxui.app;

import io.devpl.fxui.utils.FXControl;
import io.devpl.fxui.utils.FXUtils;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JvmTool extends BorderPane {
    ListView<String> list;
    TextArea editor;

    public JvmTool() {

        VBox center = new VBox();

        list = new ListView<>();

        editor = new TextArea("""
            CodeEditor.newInstance(LanguageMode.PLAIN_TEXT);
            """);

        setCenter(center);

        HBox buttonBox = new HBox();

        buttonBox.getChildren().addAll(FXUtils.newButton("解析", event -> {
            String text = editor.getText();
            if (FXUtils.isStringHasText(text)) {
                parseCommand(text);
            }
        }));

        center.getChildren().addAll(editor, list, buttonBox);

        editor.setText("""
            D:\\Develop\\Java\\JDK\\oraclejdk17.0.7\\bin\\java.exe "-javaagent:D:\\Develop\\IDE\\IntelliJ IDEA 2023.3.2\\lib\\idea_rt.jar=12695:D:\\Develop\\IDE\\IntelliJ IDEA 2023.3.2\\bin" -Dfile.encoding=UTF-8 -classpath D:\\Develop\\Code\\devpl-backend\\devpl-backend\\devpl-fxui\\target\\classes;D:\\Develop\\Java\\MavenLocalRepo\\org\\openjfx\\javafx-controls\\17.0.1\\javafx-controls-17.0.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\openjfx\\javafx-controls\\17.0.1\\javafx-controls-17.0.1-win.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\openjfx\\javafx-fxml\\17.0.1\\javafx-fxml-17.0.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\openjfx\\javafx-fxml\\17.0.1\\javafx-fxml-17.0.1-win.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\openjfx\\javafx-base\\17.0.1\\javafx-base-17.0.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\openjfx\\javafx-base\\17.0.1\\javafx-base-17.0.1-win.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\openjfx\\javafx-graphics\\17.0.1\\javafx-graphics-17.0.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\openjfx\\javafx-graphics\\17.0.1\\javafx-graphics-17.0.1-win.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\openjfx\\javafx-swing\\17.0.1\\javafx-swing-17.0.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\openjfx\\javafx-swing\\17.0.1\\javafx-swing-17.0.1-win.jar;D:\\Develop\\Code\\devpl-backend\\devpl-backend\\devpl-sdk-internal\\target\\classes;D:\\Develop\\Java\\MavenLocalRepo\\net\\jodah\\typetools\\0.6.3\\typetools-0.6.3.jar;D:\\Develop\\Java\\MavenLocalRepo\\commons-collections\\commons-collections\\3.2.2\\commons-collections-3.2.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\apache\\commons\\commons-collections4\\4.4\\commons-collections4-4.4.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\github\\f4b6a3\\ulid-creator\\5.2.3\\ulid-creator-5.2.3.jar;D:\\Develop\\Java\\MavenLocalRepo\\javax\\json\\javax.json-api\\1.1.4\\javax.json-api-1.1.4.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\openjfx\\javafx-web\\17.0.1\\javafx-web-17.0.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\openjfx\\javafx-web\\17.0.1\\javafx-web-17.0.1-win.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\openjfx\\javafx-media\\17.0.1\\javafx-media-17.0.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\openjfx\\javafx-media\\17.0.1\\javafx-media-17.0.1-win.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\fxmisc\\richtext\\richtextfx\\0.11.2\\richtextfx-0.11.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\reactfx\\reactfx\\2.0-M5\\reactfx-2.0-M5.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\fxmisc\\undo\\undofx\\2.1.1\\undofx-2.1.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\fxmisc\\flowless\\flowless\\0.7.2\\flowless-0.7.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\fxmisc\\wellbehaved\\wellbehavedfx\\0.3.3\\wellbehavedfx-0.3.3.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\jetbrains\\annotations\\24.0.1\\annotations-24.0.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\google\\googlejavaformat\\google-java-format\\1.19.2\\google-java-format-1.19.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\google\\guava\\guava\\32.1.3-jre\\guava-32.1.3-jre.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\google\\guava\\failureaccess\\1.0.1\\failureaccess-1.0.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\google\\guava\\listenablefuture\\9999.0-empty-to-avoid-conflict-with-guava\\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\google\\code\\findbugs\\jsr305\\3.0.2\\jsr305-3.0.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\checkerframework\\checker-qual\\3.37.0\\checker-qual-3.37.0.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\google\\errorprone\\error_prone_annotations\\2.21.1\\error_prone_annotations-2.21.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\google\\j2objc\\j2objc-annotations\\2.8\\j2objc-annotations-2.8.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\mybatis\\mybatis\\3.5.15\\mybatis-3.5.15.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\github\\pagehelper\\pagehelper\\5.3.2\\pagehelper-5.3.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\github\\jsqlparser\\jsqlparser\\4.5\\jsqlparser-4.5.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\dlsc\\formsfx\\formsfx-core\\11.6.0\\formsfx-core-11.6.0.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\mysql\\mysql-connector-j\\8.0.33\\mysql-connector-j-8.0.33.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\google\\protobuf\\protobuf-java\\3.21.9\\protobuf-java-3.21.9.jar;D:\\Develop\\Java\\MavenLocalRepo\\de\\marhali\\json5-java\\2.0.0\\json5-java-2.0.0.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\google\\code\\gson\\gson\\2.10.1\\gson-2.10.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\apache\\poi\\poi\\4.1.2\\poi-4.1.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\commons-codec\\commons-codec\\1.13\\commons-codec-1.13.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\apache\\commons\\commons-math3\\3.6.1\\commons-math3-3.6.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\zaxxer\\SparseBitSet\\1.2\\SparseBitSet-1.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\apache\\poi\\poi-ooxml\\4.1.2\\poi-ooxml-4.1.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\apache\\poi\\poi-ooxml-schemas\\4.1.2\\poi-ooxml-schemas-4.1.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\apache\\xmlbeans\\xmlbeans\\3.1.0\\xmlbeans-3.1.0.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\apache\\commons\\commons-compress\\1.19\\commons-compress-1.19.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\github\\virtuald\\curvesapi\\1.06\\curvesapi-1.06.jar;D:\\Develop\\Java\\MavenLocalRepo\\cn\\afterturn\\easypoi-base\\4.4.0\\easypoi-base-4.4.0.jar;D:\\Develop\\Java\\MavenLocalRepo\\ognl\\ognl\\3.2.6\\ognl-3.2.6.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\javassist\\javassist\\3.20.0-GA\\javassist-3.20.0-GA.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\slf4j\\slf4j-api\\2.0.7\\slf4j-api-2.0.7.jar;D:\\Develop\\Java\\MavenLocalRepo\\javax\\validation\\validation-api\\1.1.0.Final\\validation-api-1.1.0.Final.jar;D:\\Develop\\Java\\MavenLocalRepo\\cn\\afterturn\\easypoi-annotation\\4.4.0\\easypoi-annotation-4.4.0.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\dom4j\\dom4j\\2.1.3\\dom4j-2.1.3.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\sun\\jna\\jna\\3.0.9\\jna-3.0.9.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\kordamp\\ikonli\\ikonli-javafx\\12.3.1\\ikonli-javafx-12.3.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\kordamp\\ikonli\\ikonli-core\\12.3.1\\ikonli-core-12.3.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\kordamp\\ikonli\\ikonli-materialdesign2-pack\\12.3.1\\ikonli-materialdesign2-pack-12.3.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\kordamp\\ikonli\\ikonli-fontawesome5-pack\\12.3.1\\ikonli-fontawesome5-pack-12.3.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\javax\\annotation\\jsr250-api\\1.0\\jsr250-api-1.0.jar;D:\\Develop\\Java\\MavenLocalRepo\\jakarta\\inject\\jakarta.inject-api\\2.0.1\\jakarta.inject-api-2.0.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\jcraft\\jsch\\0.1.54\\jsch-0.1.54.jar;D:\\Develop\\Java\\MavenLocalRepo\\commons-io\\commons-io\\2.7\\commons-io-2.7.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\yaml\\snakeyaml\\2.2\\snakeyaml-2.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\springframework\\spring-jdbc\\6.1.3\\spring-jdbc-6.1.3.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\springframework\\spring-beans\\6.1.3\\spring-beans-6.1.3.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\springframework\\spring-core\\6.1.3\\spring-core-6.1.3.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\springframework\\spring-jcl\\6.1.3\\spring-jcl-6.1.3.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\springframework\\spring-tx\\6.1.3\\spring-tx-6.1.3.jar;D:\\Develop\\Java\\MavenLocalRepo\\commons-dbutils\\commons-dbutils\\1.7\\commons-dbutils-1.7.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\antlr\\ST4\\4.3.4\\ST4-4.3.4.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\antlr\\antlr-runtime\\3.5.3\\antlr-runtime-3.5.3.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\squareup\\javapoet\\1.13.0\\javapoet-1.13.0.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\mybatis\\generator\\mybatis-generator-core\\1.4.2\\mybatis-generator-core-1.4.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\baomidou\\mybatis-plus-annotation\\3.5.5\\mybatis-plus-annotation-3.5.5.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\baomidou\\mybatis-plus-core\\3.5.5\\mybatis-plus-core-3.5.5.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\baomidou\\mybatis-plus-extension\\3.5.5\\mybatis-plus-extension-3.5.5.jar;D:\\Develop\\Code\\devpl-backend\\devpl-backend\\devpl-commons\\target\\classes;D:\\Develop\\Java\\MavenLocalRepo\\org\\jsoup\\jsoup\\1.17.1\\jsoup-1.17.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\projectlombok\\lombok\\1.18.28\\lombok-1.18.28.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\springframework\\spring-web\\6.1.3\\spring-web-6.1.3.jar;D:\\Develop\\Java\\MavenLocalRepo\\io\\micrometer\\micrometer-observation\\1.12.2\\micrometer-observation-1.12.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\io\\micrometer\\micrometer-commons\\1.12.2\\micrometer-commons-1.12.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\fasterxml\\jackson\\core\\jackson-databind\\2.16.1\\jackson-databind-2.16.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\fasterxml\\jackson\\core\\jackson-annotations\\2.16.1\\jackson-annotations-2.16.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\fasterxml\\jackson\\core\\jackson-core\\2.16.1\\jackson-core-2.16.1.jar;D:\\Develop\\Code\\devpl-backend\\devpl-backend\\devpl-codegen\\target\\classes;D:\\Develop\\Java\\MavenLocalRepo\\ch\\qos\\logback\\logback-classic\\1.4.8\\logback-classic-1.4.8.jar;D:\\Develop\\Java\\MavenLocalRepo\\ch\\qos\\logback\\logback-core\\1.4.8\\logback-core-1.4.8.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\alibaba\\fastjson2\\fastjson2\\2.0.29\\fastjson2-2.0.29.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\jfinal\\enjoy\\5.0.0\\enjoy-5.0.0.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\freemarker\\freemarker\\2.3.31\\freemarker-2.3.31.jar;D:\\Develop\\Java\\MavenLocalRepo\\io\\swagger\\swagger-annotations\\1.6.2\\swagger-annotations-1.6.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\apache\\velocity\\velocity-engine-core\\2.3\\velocity-engine-core-2.3.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\ibeetl\\beetl\\3.7.0.RELEASE\\beetl-3.7.0.RELEASE.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\antlr\\antlr4-runtime\\4.9.2\\antlr4-runtime-4.9.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\github\\javaparser\\javaparser-core\\3.25.8\\javaparser-core-3.25.8.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\alibaba\\druid\\1.2.8\\druid-1.2.8.jar;D:\\Develop\\Java\\MavenLocalRepo\\javax\\annotation\\javax.annotation-api\\1.3.2\\javax.annotation-api-1.3.2.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\apache\\commons\\commons-lang3\\3.14.0\\commons-lang3-3.14.0.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\jooq\\jooq\\3.19.7\\jooq-3.19.7.jar;D:\\Develop\\Java\\MavenLocalRepo\\io\\r2dbc\\r2dbc-spi\\1.0.0.RELEASE\\r2dbc-spi-1.0.0.RELEASE.jar;D:\\Develop\\Java\\MavenLocalRepo\\org\\reactivestreams\\reactive-streams\\1.0.3\\reactive-streams-1.0.3.jar;D:\\Develop\\Code\\devpl-backend\\devpl-backend\\apache-ddlutils\\target\\classes;D:\\Develop\\Java\\MavenLocalRepo\\io\\github\\palexdev\\materialfx\\11.17.0\\materialfx-11.17.0.jar;D:\\Develop\\Java\\MavenLocalRepo\\io\\github\\palexdev\\mfxcore\\11.8.0\\mfxcore-11.8.0.jar;D:\\Develop\\Java\\MavenLocalRepo\\io\\github\\palexdev\\mfxlocalization\\11.1.0\\mfxlocalization-11.1.0.jar;D:\\Develop\\Java\\MavenLocalRepo\\io\\github\\palexdev\\mfxresources\\11.9.1\\mfxresources-11.9.1.jar;D:\\Develop\\Java\\MavenLocalRepo\\io\\github\\palexdev\\mfxeffects\\11.3.0\\mfxeffects-11.3.0.jar;D:\\Develop\\Java\\MavenLocalRepo\\io\\github\\palexdev\\virtualizedfx\\11.9.6\\virtualizedfx-11.9.6.jar;D:\\Develop\\Java\\MavenLocalRepo\\com\\github\\hervegirod\\fxsvgimage\\1.1\\fxsvgimage-1.1.jar io.devpl.fxui.Launcher
            """);


        JavaProcessTableBase table = new JavaProcessTableBase();

        center.getChildren().addAll(table);

        center.getChildren().add(FXControl.button("刷新Java进程", event -> table.refreshData()));
    }

    public static void parseCommand(String command) {
        // 使用空格作为分隔符来分割命令字符串
        String[] parts = split(command);
        // windows /path/to/java.exe
        if (parts.length > 0) {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("windows")) {
                parts[0] = parts[0].replace("\\", "/");
            }
            Path path = Path.of(parts[0]);
            if (path.getFileName().toString().equals("java.exe")) {
                // 移除"java"元素
                String[] argsArray = Arrays.copyOfRange(parts, 1, parts.length);
                // 处理剩下的部分，这里简单地打印出来

                for (int i = 0; i < argsArray.length; i++) {

                    String arg = argsArray[i];
                    if (arg.startsWith("\"") && arg.endsWith("\"")) {
                        arg = arg.substring(1, arg.length() - 2);
                    }
                    // 这里可以根据需要判断参数类型（如-cp是类路径，后面跟着的是路径）
                    if (arg.startsWith("-")) {
                        // 这是一个选项，处理它（例如，-cp后面跟着的是类路径）
                        System.out.println("Option: " + arg);
                        // 可能还需要处理选项的值（例如，-cp后面的路径）

                        if (arg.startsWith("-D")) {
                            // 环境变量
                        }

                        if (arg.startsWith("-classpath")) {

                        }
                    }
                }
            }
        } else {
            System.out.println("The command does not start with 'java'.");
        }
    }

    /**
     * 分割字符串
     *
     * @param str 字符串 需要保证“成对出现
     * @return 任意多个空格分割的字符串，使用"包裹的空格无效
     */
    public static String[] split(String str) {
        boolean flag = false;
        List<String> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 碰到空格
            if (c == ' ') {
                if (sb.isEmpty()) {
                    // 空格，忽略掉
                    continue;
                }
                if (flag) {
                    // 有引号，忽略空格
                    sb.append(c);
                } else {
                    // 保存结果
                    res.add(sb.toString());
                    // 清空
                    sb.setLength(0);
                }
            } else if (c == '"') {
                if (!flag) {
                    flag = true;
                } else {
                    // 保存结果
                    res.add(sb.toString());
                    // 清空
                    sb.setLength(0);
                    flag = false;
                }
            } else {
                sb.append(c);
            }
        }

        if (!sb.isEmpty()) {
            res.add(sb.toString());
        }

        return res.toArray(new String[0]);
    }


}
