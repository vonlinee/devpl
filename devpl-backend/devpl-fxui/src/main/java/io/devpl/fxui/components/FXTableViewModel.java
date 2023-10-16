package io.devpl.fxui.components;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FXTableViewModel {

    /**
     * @return 1 => CONSTRAINED, 0 -> UNCONSTRAINED
     * @see javafx.scene.control.TableView#CONSTRAINED_RESIZE_POLICY
     * @see javafx.scene.control.TableView#UNCONSTRAINED_RESIZE_POLICY
     */
    int resizePolicy() default 1;
}
