package io.devpl.backend.extension.compiler;

/**
 * 自定义类加载器, 用来加载动态的字节码
 */
public class StringClassLoader extends ClassLoader {

    JavaClassFileManager javaFileManager;

    public StringClassLoader(JavaClassFileManager javaFileManager) {
        this.javaFileManager = javaFileManager;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        ByteJavaFileObject fileObject = javaFileManager.getObject(name);
        if (fileObject != null) {
            byte[] bytes = fileObject.getCompiledBytes();
            return defineClass(name, bytes, 0, bytes.length);
        }
        try {
            return ClassLoader.getSystemClassLoader().loadClass(name);
        } catch (Exception e) {
            return super.findClass(name);
        }
    }
}
