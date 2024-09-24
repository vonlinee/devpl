package io.devpl.sdk.annotations;

import java.lang.annotation.*;

/**
 * The annotated element must not be {@code null} nor empty.
 * <p>
 * Supported types are:
 * <ul>
 * <li>{@code CharSequence} (length of character sequence is evaluated)</li>
 * <li>{@code Collection} (collection size is evaluated)</li>
 * <li>{@code Map} (map size is evaluated)</li>
 * <li>Array (array length is evaluated)</li>
 * </ul>
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface NotBlank {

}
