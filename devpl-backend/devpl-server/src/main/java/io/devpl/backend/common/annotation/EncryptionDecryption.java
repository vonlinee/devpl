package io.devpl.backend.common.annotation;

import java.lang.annotation.*;

/**
 * 加密解密注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptionDecryption {

}
