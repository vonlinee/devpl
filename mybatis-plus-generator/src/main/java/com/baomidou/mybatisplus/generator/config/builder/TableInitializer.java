package com.baomidou.mybatisplus.generator.config.builder;

import com.baomidou.mybatisplus.generator.config.po.IntrospectedTable;

import java.util.Map;

public interface TableInitializer {

    Map<String, Object> renderData(IntrospectedTable tableInfo);
}
