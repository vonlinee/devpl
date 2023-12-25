package io.devpl.fxui1.mvc;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller {

    /**
     * 样式文件
     * @return 样式文件路径
     */
    String style() default "";

    /**
     * if this view is used in root Node of a stage, then the label value
     * will be used to set the title of the Stage
     * @return the label of this View
     */
    String label() default "";
}
