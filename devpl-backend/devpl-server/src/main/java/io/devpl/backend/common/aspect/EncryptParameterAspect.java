package io.devpl.backend.common.aspect;

import io.devpl.backend.common.annotation.Encrypt;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.common.utils.EncryptUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 处理参数加密解密切面
 */
@Aspect
@Slf4j
@Component
public class EncryptParameterAspect {

    /**
     * 实体类所在包名
     */
    @Value("${devpl.package.entity:}")
    private String entityPackageName;

    /**
     * 加密解密切点方法
     */
    @Pointcut("@annotation(io.devpl.backend.common.annotation.EncryptionDecryption)")
    public void encryptionDecryptionPointCut() {
    }

    /**
     * 切面方法：page、list、get、save、update、tableList
     *
     * @param proceedingJoinPoint ProceedingJoinPoint
     * @return 切点方法返回值
     * @throws Throwable 切点方法抛异常
     */
    // @Around("execution(* io.devpl.generator.controller.DataSourceController.*(..))")
    @Around("encryptionDecryptionPointCut()")
    public Object doProcess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 处理请求入参
        List<Object> methodArgs = this.getMethodArgs(proceedingJoinPoint);
        for (Object item : methodArgs) {
            handleItem(item, true);
        }
        Object result = proceedingJoinPoint.proceed();
        // 处理返回值
        handleObject(result);
        return result;
    }

    /**
     * 获取方法的请求参数
     */
    private List<Object> getMethodArgs(ProceedingJoinPoint proceedingJoinPoint) {
        return Arrays.stream(proceedingJoinPoint.getArgs())
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    /**
     * 加密返回结果中的字段
     *
     * @param object 待加密的数据
     */
    private void handleObject(Object object) {
        // 仅处理类型是Result的返回对象
        if (!(object instanceof Result) || Objects.isNull(((Result<?>) object).getData())) {
            return;
        }
        Object data = ((Result<?>) object).getData();
        if (data instanceof List || data instanceof ListResult) {
            List<?> itemList = data instanceof List ? (List<?>) data : ((ListResult<?>) data).getData();
            itemList.forEach(f -> handleItem(f, false));
        } else {
            handleItem(data, false);
        }
    }

    /**
     * 加密/解密具体对象下的字段
     *
     * @param item      需要加解密的对象
     * @param isDecrypt true：解密，false：加密
     */
    private void handleItem(Object item, boolean isDecrypt) {
        // 只处理在entity包下面的对象
        if (item == null || Objects.isNull(item.getClass().getPackage()) || !item.getClass().getPackage().getName().startsWith(entityPackageName)) {
            return;
        }
        // 遍历所有字段
        Field[] fields = item.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 若该字段被EncryptParameter注解,则进行解密/加密
            Class<?> fieldType = field.getType();
            if (fieldType == String.class && Objects.nonNull(AnnotationUtils.findAnnotation(field, Encrypt.class))) {
                // 设置private类型允许访问
                field.setAccessible(Boolean.TRUE);
                try {
                    Object val = field.get(item);
                    if (val instanceof String) {
                        String newFieldValue = isDecrypt ? EncryptUtils.decrypt((String) val) : EncryptUtils.encrypt((String) field.get(item));
                        field.set(item, newFieldValue);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                } finally {
                    field.setAccessible(Boolean.FALSE);
                }
            }
        }
    }
}
