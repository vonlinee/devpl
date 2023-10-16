package io.devpl.fxui.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Utils {

    /**
     * 调用Object的toString方法，不管子类是否重写toString
     *
     * @param obj obj
     * @return obj toString
     */
    public static String objectToString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.getClass().getName() + "@" + Integer.toHexString(obj.hashCode());
    }

    public static <T> T whenNull(T val, T defaultValue) {
        if (val == null) {
            return defaultValue;
        }
        return val;
    }

    public static void println(Object... args) {
        for (Object arg : args) {
            System.out.print(arg);
            System.out.print(" ");
        }
        System.out.print("\n");
    }

    /**
     * getConstructor 方法入参是可变长参数列表，对应类中构造方法的入参类型，这里使用无参构造。
     * newInstance 返回的是泛型 T，取决于 clazz 的类型 Class<T>。这里直接用 Object 接收了。
     * 调用默认方法创建对象实例
     * @param clazz Class对象
     * @return 创建的对象实例
     */
    public static <T> T instantiate(Class<T> clazz) throws RuntimeException {
        try {
            final Constructor<T> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("failed to instantiate class " + clazz + " cause:", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("failed to instantiate class " + clazz + " cause: no default constructor in Class[" + clazz + "]", e);
        }
    }
}
