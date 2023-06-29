package io.devpl.sdk.lang.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
public @interface UseCarefully {

}
