package com.baomidou.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.config.po.IntrospectedTable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * 渲染模板接口
 * TODO 模板逻辑与视图分离
 * @author nieqiurong 2020/11/9.
 * @since 3.5.0
 */
public interface ITemplate {

    Map<String, Object> renderData(IntrospectedTable tableInfo);
}
