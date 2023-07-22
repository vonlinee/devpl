package com.baomidou.mybatisplus.generator.function;

/**
 * 转换输出文件名称
 * @author nieqiurong 2020/11/05.
 * @since 3.5.0
 */
@FunctionalInterface
public interface ConverterFileName {

    String convert(String entityName);
}
