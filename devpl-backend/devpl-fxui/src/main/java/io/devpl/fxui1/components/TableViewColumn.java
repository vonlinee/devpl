package io.devpl.fxui1.components;

import java.lang.annotation.*;

/**
 * the column config of the TableView
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableViewColumn {

    /**
     * name of the target column
     * @return name
     */
    String name();

    /**
     * max height
     * @return max height
     */
    double maxHeight() default -1;

    Class<?> type() default String.class;
}
