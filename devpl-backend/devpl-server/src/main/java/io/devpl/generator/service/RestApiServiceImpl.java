package io.devpl.generator.service;

import sun.reflect.generics.factory.CoreReflectionFactory;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.repository.FieldRepository;
import sun.reflect.generics.scope.ClassScope;

import java.lang.reflect.Type;

public class RestApiServiceImpl implements RestApiService {

    public Class<?> getWrapperClass() {
        return Object.class;
    }

    /**
     * 确定泛型类的实体类型
     *
     * @param responseEntityClass 实体类型
     * @return 实体类型
     */
    private Type findResponseType(Class<?> responseEntityClass) {
        String signature = getSignatureForList(responseEntityClass);
        return make(signature, getWrapperClass());
    }

    public String getSignatureForList(Class<?> type) {
        String name = type.getName().replace(".", "/");
        String wrapperClassSignature = getWrapperClass().getName().replace(".", "/");
        return "L" + wrapperClassSignature + "<Ljava/util/List<L" + name + ";>;>;";
    }

    /**
     * 手动生成ParameterizedType
     * module compiler option
     * --add-exports java.base/sun.reflect.generics.factory=ALL-UNNAMED
     * --add-exports java.base/sun.reflect.generics.scope=ALL-UNNAMED
     *
     * @param genericSignature 泛型签名，字段的泛型签名
     * @param declaringClass   字段声明所在的类
     * @return 泛型类型，即ParameterizedType
     */
    private Type make(String genericSignature, Class<?> declaringClass) {
        GenericsFactory genericsFactory = CoreReflectionFactory.make(declaringClass, ClassScope.make(declaringClass));
        FieldRepository fieldRepository = FieldRepository.make(genericSignature, genericsFactory);
        return fieldRepository.getGenericType();
    }
}
