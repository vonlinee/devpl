package groovy;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个是Groovy的第一个小程序，脚本为：
 * package groovy
 * def helloworld(){
 * println "hello world"
 * }
 */
public class GroovyTest {

    @Test
    public void test1() {
        //创建GroovyShell
        GroovyShell groovyShell = new GroovyShell();

        String scriptText = """
            package groovy;

            def HelloWorld(){
              println "hello world"
            }

            /**
             * 简易加法
             * @param a 数字a
             * @param b 数字b
             * @return 和
             */
            static def add(int a, int b) {
                return a + b
            }
            """;
        Script script = groovyShell.parse(scriptText);
        //执行
        script.invokeMethod("HelloWorld", null);
    }

    @Test
    public void test2() {

        GroovyShell groovyShell = new GroovyShell();

        String scriptText = """
            package groovy

            /**
             * 简易加法
             * @param a 数字a
             * @param b 数字b
             * @return 和
             */
            def add(int a, int b) {
                return a + b
            }

            /**
             * map转化为String
             * @param paramMap 参数map
             * @return 字符串
             */
            def mapToString(Map<String, String> paramMap) {
                StringBuilder stringBuilder = new StringBuilder();
                paramMap.forEach({ key, value ->
                    stringBuilder.append("key:" + key + ";value:" + value)
                })
                return stringBuilder.toString()
            }
            """;
        Script script = groovyShell.parse(scriptText);
        //执行加法脚本
        Object[] params1 = new Object[]{1, 2};
        int sum = (int) script.invokeMethod("add", params1);
        System.out.println("a加b的和为:" + sum);
        //执行解析脚本
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("科目1", "语文");
        paramMap.put("科目2", "数学");
        Object[] params2 = new Object[]{paramMap};
        String result = (String) script.invokeMethod("mapToString", params2);
        System.out.println("mapToString:" + result);
    }
}
