package com.baomidou.mybatisplus.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import org.jetbrains.annotations.NotNull;

/**
 * 填充接口
 * @author nieqiurong
 * @since 3.5.0 2020/11/30.
 */
public interface IFill {

    @NotNull
    String getName();

    @NotNull
    FieldFill getFieldFill();

}
