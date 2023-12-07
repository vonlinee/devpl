package io.devpl.backend.utils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 反射工具类 <br />
 * <br />
 * 提供了一系列的获取某一个类的信息的方法<br />
 * 包括获取全类名，实现的接口，接口的泛型等<br />
 * 并且提供了根据Class类型获取对应的实例对象的方法，以及修改属性和调用对象的方法等
 * 提供访问私有变量, 获取泛型类型 Class, 提取集合中元素属性等 Utils 函数
 */
public class ReflectionUtils {

    /**
     * @param object       对象
     * @param fieldName    字段名
     * @param defaultValue 默认值
     * @param <T>          默认值类型
     * @return 字段值
     * @throws ClassCastException 类型转换失败
     */
    @SuppressWarnings("unchecked")
    public static <K extends T, T> T getTypedValue(Object object, String fieldName, K defaultValue) {
        return (T) getValue(object, fieldName, defaultValue);
    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param object       对象
     * @param fieldName    对象的字段
     * @param defaultValue 当反射操作抛异常时，返回该值，获取的值为null时返回null
     * @return 字段值
     */
    public static Object getValue(Object object, String fieldName, Object defaultValue) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        Object result = null;
        if (tryMakeAccessible(field)) {
            try {
                result = field.get(object);
            } catch (IllegalAccessException e) {
                // ignore
                result = defaultValue;
            }
        }
        return result;
    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param object    对象
     * @param fieldName 对象的字段
     * @return 字段值
     */
    public static Object getValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        if (!tryMakeAccessible(field)) {
            return null;
        }
        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            // ignore
        }
        return result;
    }

    /**
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
     *
     * @param object    对象
     * @param fieldName 对象的字段
     * @param value     设置的字段值
     */
    public static void setValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        if (!tryMakeAccessible(field)) {
            return;
        }
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            // ignore
        }
    }


    /**
     * 通过反射, 获得定义 Class 时声明的父类的泛型参数的类型
     * 如: public EmployeeDao extends BaseDao<Employee, String>
     *
     * @param clazz 指定的Class
     * @param index 第几个泛型参数
     * @return 泛型类型
     */
    public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class<?>) params[index];
    }

    /**
     * 通过反射, 获得 Class 定义中声明的父类的泛型参数类型
     * 如: public EmployeeDao extends BaseDao<Employee, String>
     *
     * @param <T>   泛型
     * @param clazz 目标Class
     * @return 泛型Class
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSuperGenericType(Class<T> clazz) {
        return (Class<T>) getSuperClassGenricType(clazz, 0);
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredMethod
     *
     * @param object         对象
     * @param methodName     方法名
     * @param parameterTypes 方法的参数类型列表
     * @return Method对象
     */
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                // Method 不在当前类定义, 继续向上转型
            }
        }

        return null;
    }

    /**
     * 使 filed 变为可访问
     *
     * @param field 字段
     * @see Field#trySetAccessible() 会抛异常，JDK9+
     */
    public static boolean tryMakeAccessible(Field field) {
        try {
            return field.trySetAccessible();
        } catch (SecurityException securityException) {
            // JDK 8会抛出此异常
            return false;
        }
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     * getDeclaredFiled 仅能获取类本身的属性成员（包括私有、共有、保护）
     *
     * @param object    对象
     * @param filedName 字段名
     * @return Field实例
     */
    public static Field getDeclaredField(Object object, String filedName) {
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(filedName);
            } catch (NoSuchFieldException e) {
                // Field 不在当前类定义, 继续向上转型
            }
        }
        return null;
    }

    /**
     * 仅能获取类(及其父类)的public属性成员
     *
     * @param obj       对象
     * @param fieldName 获取的字段名
     * @return Field对象
     */
    public static Field getField(Object obj, String fieldName) throws NoSuchFieldException {
        return Objects.requireNonNull(obj).getClass().getField(fieldName);
    }

    /**
     * 直接调用对象方法, 而忽略修饰符(private, protected)
     *
     * @param object         对象
     * @param methodName     方法名
     * @param parameterTypes 参数类型
     * @param parameters     方法执行的参数
     * @return 方法返回值
     * @throws InvocationTargetException 执行方法出错
     * @throws IllegalArgumentException  执行方法出错
     */
    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes,
                                      Object[] parameters) throws ReflectiveOperationException {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        }
        method.setAccessible(true);
        return method.invoke(object, parameters);
    }


    /**
     * 获取包名
     *
     * @return 包名【String类型】
     */
    public static String getPackage(Class<?> clazz) {
        Package pck = clazz.getPackage();
        if (null != pck) {
            return pck.getName();
        } else {
            return "没有包！";
        }
    }

    /**
     * 获取继承的父类的全类名
     *
     * @return 继承的父类的全类名【String类型】
     */
    public static String getSuperClassName(Class<?> clazz) {
        Class<?> superClass = clazz.getSuperclass();
        if (null != superClass) {
            return superClass.getName();
        } else {
            return "没有父类！";
        }
    }

    /**
     * 获取全类名
     *
     * @return 全类名【String类型】
     */
    public static String getClassName(Class<?> clazz) {
        return clazz.getName();
    }

    /**
     * 获取父类的泛型
     *
     * @return 父类的泛型【Class类型】
     */
    public static Class<?> getSuperClassGenericParameterizedType(Class<?> clazz) {
        Type genericSuperClass = clazz.getGenericSuperclass();
        Class<?> superClassGenericParameterizedType = null;
        // 判断父类是否有泛型
        if (genericSuperClass instanceof ParameterizedType pt) {
            // 向下转型，以便调用方法
            // 只取第一个，因为一个类只能继承一个父类
            Type superClazz = pt.getActualTypeArguments()[0];
            // 转换为Class类型
            superClassGenericParameterizedType = (Class<?>) superClazz;
        }
        return superClassGenericParameterizedType;
    }

    /**
     * 获取接口的所有泛型
     *
     * @return 所有的泛型接口【每一个泛型接口的类型为Class，最后保存到一个List集合中】
     */
    public static List<Class<?>> getInterfaceGenericParameterizedTypes(Class<?> clazz) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        int len = genericInterfaces.length;
        List<Class<?>> list = new ArrayList<>();
        for (Type genericInterface : genericInterfaces) {
            // 判断接口是否有泛型
            if (genericInterface instanceof ParameterizedType pt) {
                // 得到所有的泛型【Type类型的数组】
                Type[] interfaceTypes = pt.getActualTypeArguments();
                for (Type interfaceType : interfaceTypes) {
                    // 获取对应的泛型【Type类型】
                    // 转换为Class类型
                    Class<?> interfaceClass = (Class<?>) interfaceType;
                    list.add(interfaceClass);
                }
            }
        }
        return list;
    }

    /**
     * 根据传入的类的Class对象，以及构造方法的形参的Class对象，获取对应的构造方法对象
     *
     * @param clazz          类的Class对象
     * @param parameterTypes 构造方法的形参的Class对象【可以不写】
     * @return 构造方法对象【Constructor类型】
     */
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        return clazz.getDeclaredConstructor(parameterTypes);
    }

    /**
     * 根据传入的构造方法对象，以及，获取对应的实例
     *
     * @param constructor 构造方法对象
     * @param initargs    传入构造方法的实参【可以不写】
     * @return 对应的实例【Object类型】
     */
    public static Object getNewInstance(Constructor<?> constructor, Object... initargs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        constructor.setAccessible(true);
        return constructor.newInstance(initargs);
    }

    /**
     * 根据传入的属性名字符串，修改对应的属性值
     *
     * @param clazz 类的Class对象
     * @param name  属性名
     * @param obj   要修改的实例对象
     * @param value 修改后的新值
     */
    public static void setField(Class<?> clazz, String name, Object obj, Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        field.set(obj, value);
    }

    /**
     * 根据传入的方法名字符串，获取对应的方法
     *
     * @param clazz          类的Class对象
     * @param name           方法名
     * @param parameterTypes 方法的形参对应的Class类型【可以不写】
     * @return 方法对象【Method类型】
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        return clazz.getDeclaredMethod(name, parameterTypes);
    }

    /**
     * 根据传入的方法对象，调用对应的方法
     *
     * @param method 方法对象
     * @param obj    要调用的实例对象【如果是调用静态方法，则可以传入null】
     * @param args   传入方法的实参【可以不写】
     * @return 方法的返回值【没有返回值，则返回null】
     */
    public static Object invokeMethod(Method method, Object obj, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        method.setAccessible(true);
        return method.invoke(obj, args);
    }
}
