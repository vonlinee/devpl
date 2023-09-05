package samples;

import com.baomidou.mybatisplus.generator.jdbc.JDBCDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class DriverTest {

    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException {
        try {
            Class.forName(JDBCDriver.ORACLE.getDriverClassName());
        } catch (Exception exception) {
            System.out.println("第一次加载失败");
        }

        try {
            ClassLoader classLoader = new URLClassLoader(new URL[]{new URL("file:///D:\\Develop\\Code\\Github\\devpl-backend\\mybatis-plus-generator\\libs\\ojdbc8.jar")}, Thread.currentThread().getContextClassLoader());
            Thread.currentThread().setContextClassLoader(classLoader);

            try {
                Class<?> clazz = classLoader.loadClass(JDBCDriver.ORACLE.getDriverClassName());

                System.out.println(clazz);

                Class<?> clazz1 = Class.forName(JDBCDriver.ORACLE.getDriverClassName());

                System.out.println(clazz == clazz1);
            } catch (Exception exception) {
                System.out.println("加载失败");
            }
        } catch (Exception exception) {
            System.out.println("初始化类加载器失败");
        }

        try {
            Class.forName(JDBCDriver.ORACLE.getDriverClassName());
        } catch (Exception exception) {
            System.out.println("第二次加载失败");
        }

    }
}
