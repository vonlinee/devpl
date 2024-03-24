package io.devpl.backend.extension.compiler;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内置的JavaFileManager是面向类路径下的Java源码文件进行加载，这里也需要自行实现JavaFileManager
 * 自定义一个JavaFileManage来控制编译之后字节码的输出位置
 */
public class JavaClassFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    /**
     * 存放编译之后的字节码(key:类全名,value:编译之后输出的字节码)
     */
    private final Map<String, ByteJavaFileObject> javaFileObjectMap = new ConcurrentHashMap<>();

    /**
     * 加载动态编译生成类的父类加载器
     */
    private final ClassLoader classLoader;

    /**
     * @param fileManager com.sun.tools.javac.file.JavacFileManager
     */
    public JavaClassFileManager(JavaFileManager fileManager) {
        super(fileManager);
        classLoader = new StringClassLoader(this);
    }

    /**
     * 获取输出的文件对象，它表示给定位置处指定类型的指定类。
     * 获得Java字节码文件对象
     * 编译器编译源码时会将Java源码对象编译转为Java字节码对象
     *
     * @param location  源码位置
     * @param className 类名
     * @param kind      文件类型
     * @param sibling   Java源码对象
     * @return Java字节码文件对象
     */
    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        ByteJavaFileObject javaFileObject = new ByteJavaFileObject(className, kind);
        javaFileObjectMap.put(className, javaFileObject);
        return javaFileObject;
    }

    /**
     * 根据提供的 location、className 和 kind，getJavaFileForInput 方法会尝试在文件系统中查找对应的 Java 文件对象
     * 如果找到文件，该方法会返回一个 JavaFileObject，该对象封装了对文件的抽象访问，允许读取文件内容。
     * @param location  a location
     * @param className Java类的全限定类名，包括包名
     * @param kind      the kind of file, must be one of {@link
     *                  JavaFileObject.Kind#SOURCE SOURCE} 源代码文件 or {@link
     *                  JavaFileObject.Kind#CLASS CLASS} 类文件
     * @return JavaFileObject
     * @throws IOException 如果找不到文件或发生其他 I/O 错误，该方法会抛出 IOException
     * @see javax.tools.StandardLocation#SOURCE_PATH 源文件的位置
     * @see javax.tools.StandardLocation#CLASS_PATH 类文件的位置
     */
    @Override
    public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
        if (location instanceof ClassPathLocation classPathLocation) {

        }
        return super.getJavaFileForInput(location, className, kind);
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        if (location == null) {
            return this.classLoader;
        }
        return super.getClassLoader(location);
    }

    public ByteJavaFileObject getObject(String name) {
        return javaFileObjectMap.get(name);
    }
}
