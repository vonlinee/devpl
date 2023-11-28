package io.devpl.backend.common.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSON {

    /**
     * 指定类型
     *
     * @return 类型
     */
    Class<?> type();

    /**
     * 指定保留的字段
     *
     * @return 保留的字段，多个用逗号分隔
     */
    String include() default "";

    /**
     * 指定需要去除的字段
     *
     * @return 去除的字段，多个用逗号分隔
     */
    String exclude() default "";
}
