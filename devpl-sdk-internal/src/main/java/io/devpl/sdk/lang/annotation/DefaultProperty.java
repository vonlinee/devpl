package io.devpl.sdk.lang.annotation;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DefaultProperty {
    String value();
}
