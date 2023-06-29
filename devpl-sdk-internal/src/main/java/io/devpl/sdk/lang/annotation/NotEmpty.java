package io.devpl.sdk.lang.annotation;

import java.lang.annotation.*;

@Documented
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface NotEmpty {

}
