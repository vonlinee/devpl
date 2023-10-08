package io.devpl.generator.compiler;

/**
 * Create by andy on 2018-12-06 15:21
 */
public class Test1 {

    public static void main(String[] args) {
        String code = """
                public class HelloWorld {
                    public static void main(String []args) {
                        for(int i=0; i < 1; i++){
                            System.out.println("Hello World!");
                        }
                    }
                }""";
        CustomStringJavaCompiler compiler = new CustomStringJavaCompiler(code);
        boolean res = compiler.compile();
        if (res) {
            System.out.println("编译成功");
            System.out.println("compilerTakeTime：" + compiler.getCompilerTakeTime());
            try {
                compiler.runMainMethod();
                System.out.println("runTakeTime：" + compiler.getRunTakeTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(compiler.getRunResult());
            System.out.println("诊断信息：" + compiler.getCompilerMessage());
        } else {
            System.out.println("编译失败");
            System.out.println(compiler.getCompilerMessage());
        }

    }
}

