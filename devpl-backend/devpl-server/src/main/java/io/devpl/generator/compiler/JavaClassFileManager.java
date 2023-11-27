package io.devpl.generator.compiler;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
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
