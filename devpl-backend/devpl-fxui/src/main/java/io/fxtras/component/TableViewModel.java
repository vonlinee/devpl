package io.fxtras.component;

import java.lang.annotation.*;

/**
 * the column config of the TableView
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableViewModel {

    /**
     * table name
     * @return table name
     */
    String name();
}