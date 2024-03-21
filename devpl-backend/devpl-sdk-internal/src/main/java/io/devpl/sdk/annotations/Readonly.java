package io.devpl.sdk.annotations;

import java.lang.annotation.*;

/**
 * 标注集合只读
 * 1.作用在方法参数上，表示此方法对该值只读，而不会进行写操作
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD})
public @interface Readonly {

}
