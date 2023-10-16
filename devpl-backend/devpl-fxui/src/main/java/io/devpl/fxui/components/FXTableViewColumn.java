package io.devpl.fxui.components;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FXTableViewColumn {

    String field() default "";

    String title();

    int order() default 0;

    boolean sortable() default false;

    double width() default -1;

    double minWidth() default -1;

    double maxWidth() default -1;
}
