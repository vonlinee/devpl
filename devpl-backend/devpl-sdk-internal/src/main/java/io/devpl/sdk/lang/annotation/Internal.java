package io.devpl.sdk.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Program elements annotated &#64;Internal are intended for POI internal use
 * only.
 * <p>
 * Such elements are not public by design and likely to be removed, have their
 * signature change, or have their access level decreased from public to
 * protected, package, or private in future versions of POI without notice.
 * <p>
 * &#64;Internal elements are eligible for immediate modification or removal and
 * are not subject to the POI project policy of deprecating an element for 2
 * major releases before removing.
 * @author Yegor Kozlov
 * @since POI-3.6
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Internal {
    String value() default "";

    /**
     * The POI version when an element was declared internal. This is not the same
     * as an &#64;since javadoc annotation which specifies when the feature itself
     * was added. A feature that was made internal after it was added may have a
     * different since and Internal-since version numbers.
     */
    String since() default "";
}
