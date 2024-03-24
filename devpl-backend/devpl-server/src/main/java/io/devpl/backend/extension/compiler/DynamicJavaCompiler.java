package io.devpl.backend.extension.compiler;

import io.devpl.codegen.template.TemplateDirective;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * TODO 动态编译 沙箱环境
 * 使用javax.tool实现java代码编译
 * <a href="https://www.cnblogs.com/andysd/p/10081443.html">...</a>
 */
@Component
public class DynamicJavaCompiler {

    /**
     * 获取java的编译器
     */
    private final JavaCompiler compiler;

    /**
     * 存放编译过程中输出的信息
     */
    private final DiagnosticCollector<JavaFileObject> diagnosticsCollector;
    private final JavaFileManager jfm;

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

        List<JavaFileObject> javaFileObjects = new ArrayList<>();
        javaFileObjects.add(javaFileObject);

        try {
            JavaFileObject templateDirectiveJfo = jfm.getJavaFileForInput(new JavaFileManager.Location() {
                @Override
                public String getName() {
                    ClassPathResource resource = new ClassPathResource("static/samples/TemplateDirective.java");
                    return resource.getPath();
                }

                @Override
                public boolean isOutputLocation() {
                    return false;
                }
            }, TemplateDirective.class.getName(), JavaFileObject.Kind.SOURCE);

            if (templateDirectiveJfo != null) {
                javaFileObjects.add(templateDirectiveJfo);
            }
        } catch (IOException e) {
            result.setFailed(true);
            return result;
        }

        StringWriter out = new StringWriter();

        // 获取一个编译任务
        JavaCompiler.CompilationTask task = compiler.getTask(out, jfm, diagnosticsCollector, null, null, javaFileObjects);

        result.stop();
        // 执行编译任务

        Boolean callResult = task.call();

        if (callResult) {

            ClassLoader classloader = jfm.getClassLoader(null);
            try {
                Class<?> clazz = classloader.loadClass(fullClassName);
                result.addCompiledClass(fullClassName, clazz);
            } catch (ClassNotFoundException e) {
                result.appendMsg("无法加载类").appendMsg(fullClassName);
            }
            if (result.isFailed()) {
                result.appendMsg(getCompilerMessage());
            }
            // 输出编译信息

            List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticsCollector.getDiagnostics();
            diagnostics.forEach(diagnostic -> {
                if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                    result.setFailed(true);
                }
                result.appendMsg("[").appendMsg(diagnostic.getKind()).appendMsg("]").appendMsg(diagnostic.getMessage(Locale.CHINA));
            });
        } else {
            result.setFailed(true);
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

