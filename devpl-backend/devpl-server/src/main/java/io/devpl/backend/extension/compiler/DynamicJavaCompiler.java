package io.devpl.backend.extension.compiler;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * 使用javax.tool实现java代码编译
 * <a href="https://www.cnblogs.com/andysd/p/10081443.html">...</a>
 */
public class DynamicJavaCompiler {

    /**
     * 获取java的编译器
     */
    private final JavaCompiler compiler;

    /**
     * 存放编译过程中输出的信息
     */
    private final DiagnosticCollector<JavaFileObject> diagnosticsCollector;
    private final JavaClassFileManager jfm;

    public DynamicJavaCompiler() {
        this(Locale.CHINA, StandardCharsets.UTF_8);
    }

    public DynamicJavaCompiler(Locale locale, Charset charset) {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.diagnosticsCollector = new DiagnosticCollector<>();
        // 标准的内容管理器,更换成自己的实现，覆盖部分方法
        this.jfm = new JavaClassFileManager(compiler.getStandardFileManager(diagnosticsCollector, locale, charset));
    }

    /**
     * 编译字符串源代码,编译失败在 diagnosticsCollector 中获取提示信息
     *
     * @return 编译结果
     */
    public CompilationResult compile(String fullClassName, String sourceCode) {
        CompilationResult result = new CompilationResult();
        // 构造源代码对象
        result.start();
        JavaFileObject javaFileObject = new StringJavaFileObject(fullClassName, sourceCode);
        // 获取一个编译任务
        JavaCompiler.CompilationTask task = compiler.getTask(null, jfm, diagnosticsCollector, null, null, List.of(javaFileObject));
        result.stop();
        // 执行编译任务
        result.setFailed(task.call());
        if (result.isFailed()) {
            result.setCompileMessage(getCompilerMessage());
        }
        return result;
    }

    /**
     * 执行main方法，重定向System.out.print
     */
    public void runMethod(String method, CompilationResult result) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, UnsupportedEncodingException {
        PrintStream out = System.out;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            // PrintStream ps = new PrintStream("/Users/andy/Desktop/tem.sql"); //输出到文件
            System.setOut(printStream);
            ClassLoader loader = jfm.getClassLoader(null);
            Class<?> aClass = loader.loadClass(result.getFullClassName());
            Method main = aClass.getMethod(method, String[].class);
            Object[] pars = new Object[]{1};
            pars[0] = new String[]{};
            main.invoke(null, pars); // 调用main方法
        } finally {
            // 还原默认打印的对象
            System.setOut(out);
        }
    }

    /**
     * @return 编译信息(错误 警告)
     */
    public String getCompilerMessage() {
        StringBuilder sb = new StringBuilder();
        List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticsCollector.getDiagnostics();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            sb.append(diagnostic.toString()).append("\r\n");
        }
        return sb.toString();
    }
}

