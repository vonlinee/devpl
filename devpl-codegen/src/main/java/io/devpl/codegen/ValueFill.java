package io.devpl.codegen;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @since 11
 */
public class ValueFill {

    static Map<Class<?>, Object> defaultValueMap = new HashMap<>();

    static {
        defaultValueMap.put(int.class, 0);
        defaultValueMap.put(double.class, 0D);
        defaultValueMap.put(float.class, 0F);
        defaultValueMap.put(char.class, ' ');
        defaultValueMap.put(long.class, 0L);
        defaultValueMap.put(byte.class, 0);
        defaultValueMap.put(short.class, 0);
        defaultValueMap.put(boolean.class, false);
        defaultValueMap.put(Byte.class, (byte) 0);
        defaultValueMap.put(Short.class, (short) 0);
        defaultValueMap.put(Boolean.class, Boolean.FALSE);
        defaultValueMap.put(Integer.class, 0);
        defaultValueMap.put(Long.class, 0L);
        defaultValueMap.put(Float.class, (float) 0);
        defaultValueMap.put(Double.class, 1.0);
        defaultValueMap.put(String.class, "Empty String");
        defaultValueMap.put(BigDecimal.class, BigDecimal.valueOf(0));
        defaultValueMap.put(BigInteger.class, BigInteger.valueOf(0));
        defaultValueMap.put(Date.class, new Date());
        defaultValueMap.put(LocalDateTime.class, LocalDateTime.now());
        defaultValueMap.put(LocalDate.class, LocalDate.now());
        defaultValueMap.put(Object.class, new Object());
    }

    /**
     * @param type Type is the common superinterface for all types in the Java programming language. These include raw types, parameterized types, array types, type variables and primitive types.
     * @param <T>  返回值
     * @return type对应的空对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T createDefaultValueForType(Type type) {
        Object emptyObject = null;
        if (type instanceof Class) {
            final Class<?> classType = (Class<?>) type;
            // 获取类型参数列表
            // final TypeVariable<? extends Class<?>>[] typeParameters = classType.getTypeParameters();
            emptyObject = createDefaultValueForClass(classType);
        } else if (type instanceof ParameterizedType) {
            // 参数化类型
            final ParameterizedType parameterizedType = (ParameterizedType) type;
            // 泛型对象，可能是List，Map等等
            emptyObject = createDefaultValueForParameterizedType(parameterizedType);
        } else if (type instanceof WildcardType) {
            // TODO 通配符类型
            final WildcardType wildcardType = (WildcardType) type;
        }
        return (T) emptyObject;
    }

    @SuppressWarnings("unchecked")
    public static <T> T createDefaultValueForClass(Class<?> classType) {
        Object emptyObject = null;
        if (classType.isPrimitive()) {
            // 基本类型
            return (T) defaultValueMap.get(classType);
        } else if (classType.isInterface()) {
            // 接口
            if (List.class.isAssignableFrom(classType)) {
                emptyObject = (T) new ArrayList<>();
            } else if (Map.class.isAssignableFrom(classType)) {
                emptyObject = (T) new HashMap<>();
            } else if (Set.class.isAssignableFrom(classType)) {
                emptyObject = (T) new HashSet<>();
            }
            // 其余的接口暂不考虑
            return (T) emptyObject;
        } else if (classType.isArray()) {
            // 数组
            Class<?> componentType = classType.getComponentType();
            emptyObject = Array.newInstance(componentType, 1);
            Object componentValue = createDefaultValueForType(componentType);
            Array.set(emptyObject, 0, componentValue);
            return (T) emptyObject;
        } else if (classType.isAnnotation()) {
            // 注解
            // 注解暂不考虑
            return null;
        } else if (Modifier.isAbstract(classType.getModifiers())) {
            // 抽象类暂不考虑
        }
        // 普通类
        if (defaultValueMap.containsKey(classType)) {
            // 普通类：String，Date，LocalDate，LocalDateTime等等
            return (T) defaultValueMap.get(classType);
        } else {
            // 其他普通类
            final Field[] declaredFields = classType.getDeclaredFields();
            // 普通对象类型
            // 创建空对象
            emptyObject = newInstance(classType);
            for (Field field : declaredFields) {
                Type genericType = field.getGenericType();
                final Object value = createDefaultValueForType(genericType);
                field.setAccessible(true);
                try {
                    field.set(emptyObject, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return (T) emptyObject;
    }

    /**
     * 创建泛型对象类型的默认值
     * 有可能类的字段中并未用到类型参数
     *
     * @param rawClass            对象类型，不是List,Set,Map
     * @param actualTypeArguments 该对象类型的类型参数
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T createDefaultValueForGenericCustomClass(Class<?> rawClass, Type[] actualTypeArguments) {
        final Object emptyObject = newInstance(rawClass);
        final Field[] declaredFields = rawClass.getDeclaredFields();
        final TypeVariable<? extends Class<?>>[] typeParameters = rawClass.getTypeParameters();
        final Map<String, Type> typeVariableMapping = new HashMap<>();
        for (int i = 0; i < typeParameters.length; i++) {
            typeVariableMapping.put(typeParameters[i].getName(), actualTypeArguments[i]);
        }
        for (Field declaredField : declaredFields) {
            final Type genericType = declaredField.getGenericType();
            Object value;
            // 类型变量参数
            if (genericType instanceof TypeVariable) {
                Type type = typeVariableMapping.get(((TypeVariable<?>) genericType).getName());
                value = createDefaultValueForType(type);
            } else {
                value = createDefaultValueForType(genericType);
            }
            try {
                declaredField.setAccessible(true);
                declaredField.set(emptyObject, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return (T) emptyObject;
    }

    /**
     * 泛型对象
     *
     * @param parameterizedType
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T createDefaultValueForParameterizedType(ParameterizedType parameterizedType) {
        final Type rawType = parameterizedType.getRawType();
        if (rawType instanceof Class<?>) {
            // 原始类型
            Class<?> rawClass = (Class<?>) rawType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (List.class.isAssignableFrom(rawClass)) {
                // 只有一个泛型参数
                Type actualTypeArgument = actualTypeArguments[0];
                List<Object> list = new ArrayList<>();
                final Object value = createDefaultValueForType(actualTypeArgument);
                list.add(value);
                return (T) list;
            } else if (Map.class.isAssignableFrom(rawClass)) {
                Map<Object, Object> map = new HashMap<>();
                final Object key = createDefaultValueForType(actualTypeArguments[0]);
                final Object value = createDefaultValueForType(actualTypeArguments[1]);
                map.put(key, value);
                return (T) map;
            } else if (Set.class.isAssignableFrom(rawClass)) {
                Set<Object> set = new HashSet<>();
                final Object value = createDefaultValueForType(actualTypeArguments[0]);
                set.add(value);
                return (T) set;
            } else {
                // TODO 其他泛型对象
                return createDefaultValueForGenericCustomClass(rawClass, actualTypeArguments);
            }
        }
        return null;
    }

    /**
     * 反射创建对象实例
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) {
        T instance = null;
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            constructor.setAccessible(true);
            Parameter[] parameters = constructor.getParameters();
            if (parameters.length == 0) {
                instance = (T) newInstance(constructor, null);
            } else {
                final Object[] constructorArgs = createConstructorArgs(parameters);
                instance = (T) newInstance(constructor, constructorArgs);
            }
            if (instance != null) {
                break;
            }
        }
        if (instance == null) {
            System.out.println("创建空对象失败:" + clazz);
        }
        return instance;
    }

    private static Object[] createConstructorArgs(Parameter[] parameters) {
        final Object[] args = new Object[parameters.length];
        int index = 0;
        for (Parameter parameter : parameters) {
            Type parameterizedType = parameter.getParameterizedType();
            args[index++] = createDefaultValueForType(parameterizedType);
        }
        return args;
    }

    private static <T> T newInstance(Constructor<T> constructor, Object[] args) {
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }
}
