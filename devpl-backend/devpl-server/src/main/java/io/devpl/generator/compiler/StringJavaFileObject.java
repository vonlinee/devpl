package io.devpl.generator.compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

/**
 * 自定义一个字符串的源码对象
 */
public class StringJavaFileObject extends SimpleJavaFileObject {

    /**
     * 源码
     */
    private final String contents;

    /**
     * java源代码 => StringJavaFileObject对象 的时候使用
     *
     * @param className 类名
     * @param content   源码
     */
    public StringJavaFileObject(String className, String content) {
        super(URI.create("string:///" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
        this.contents = content;
    }

    // 字符串源码会调用该方法
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return contents;
    }
}
