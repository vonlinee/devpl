package com.baomidou.mybatisplus.generator.engine;

import com.baomidou.mybatisplus.generator.config.builder.Context;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.Map;

/**
 * 模板引擎抽象类
 * @author hubin
 * @since 2018-01-10
 */
public abstract class AbstractTemplateEngine {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * 模板引擎初始化
     */
    @NotNull
    public abstract AbstractTemplateEngine init(@NotNull Context configBuilder);

    /**
     * 将模板转化成为文件
     * @param objectMap    渲染对象 MAP 信息
     * @param templatePath 模板文件
     * @param outputStream 文件输出位置
     * @throws Exception 异常
     * @since 3.5.0
     */
    public abstract void merge(@NotNull Map<String, Object> objectMap, @NotNull String templatePath, OutputStream outputStream) throws Exception;

    /**
     * 模板真实文件路径
     * @param filePath 文件路径
     * @return ignore
     */
    @NotNull
    public abstract String templateFilePath(@NotNull String filePath);
}
