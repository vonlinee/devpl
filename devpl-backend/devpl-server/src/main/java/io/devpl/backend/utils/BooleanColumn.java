package io.devpl.backend.utils;

import java.lang.annotation.*;
import java.sql.Types;

/**
 * 标注布尔类型的列
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BooleanColumn {

    /**
     * 数据库字段的jdbc类型
     *
     * @return 数据库字段的jdbc类型
     */
    int jdbcType() default Types.TINYINT;
}
