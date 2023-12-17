package io.devpl.codegen.config.args;

import io.devpl.codegen.ConstVal;
import io.devpl.codegen.config.*;
import io.devpl.codegen.core.TableGeneration;
import io.devpl.codegen.util.ClassUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.LoggingCache;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 控制器属性配置
 */
public class MapperTemplateArguments extends TemplateArgumentsForJavaClass implements TableInitializer {

    /**
     * 自定义继承的Mapper类全称，带包名
     */
    private String superClass = ConstVal.SUPER_MAPPER_CLASS;
    /**
     * Mapper标记注解
     *
     * @since 3.5.3
     */
    private Class<? extends Annotation> mapperAnnotationClass;
    /**
     * 是否开启BaseResultMap（默认 false）
     *
     * @since 3.5.0
     */
    private boolean baseResultMap;
    /**
     * 是否开启baseColumnList（默认 false）
     *
     * @since 3.5.0
     */
    private boolean baseColumnList;
    /**
     * 转换输出Mapper文件名称
     *
     * @since 3.5.0
     */
    private Function<String, String> converterMapperFileName = (entityName -> entityName + ConstVal.MAPPER);
    /**
     * 转换输出Xml文件名称
     *
     * @since 3.5.0
     */
    private Function<String, String> converterXmlFileName = (entityName -> entityName + ConstVal.MAPPER);
    /**
     * 是否覆盖已有文件（默认 false）
     *
     * @since 3.5.2
     */
    private boolean fileOverride;
    /**
     * 设置缓存实现类
     *
     * @since 3.5.0
     */
    private Class<? extends Cache> cache;

    private MapperTemplateArguments() {
    }

    @NotNull
    public String getSuperClass() {
        return superClass;
    }

    @Deprecated
    public boolean isMapperAnnotation() {
        return mapperAnnotationClass != null;
    }

    public boolean isBaseResultMap() {
        return baseResultMap;
    }

    public boolean isBaseColumnList() {
        return baseColumnList;
    }

    public Function<String, String> getConverterMapperFileName() {
        return converterMapperFileName;
    }

    public Function<String, String> getConverterXmlFileName() {
        return converterXmlFileName;
    }

    public Class<? extends Cache> getCache() {
        return this.cache == null ? LoggingCache.class : this.cache;
    }

    public boolean isFileOverride() {
        return fileOverride;
    }

    @Override
    @NotNull
    public Map<String, Object> renderData(@NotNull TableGeneration tableInfo) {
        Map<String, Object> data = new HashMap<>();
        data.put("enableCache", this.cache != null);
        data.put("mapperAnnotation", mapperAnnotationClass != null);
        data.put("mapperAnnotationClass", mapperAnnotationClass);
        data.put("baseResultMap", this.baseResultMap);
        data.put("baseColumnList", this.baseColumnList);
        data.put("superMapperClassPackage", this.superClass);
        if (this.cache != null) {
            Class<? extends Cache> cacheClass = this.getCache();
            data.put("cache", cacheClass);
            data.put("cacheClassName", cacheClass.getName());
        }
        data.put("superMapperClass", ClassUtils.getSimpleName(this.superClass));
        return data;
    }

    public static class Builder extends BaseBuilder {

        private final MapperTemplateArguments mapper = new MapperTemplateArguments();

        public Builder(StrategyConfig strategyConfig) {
            super(strategyConfig);
        }

        /**
         * 父类Mapper
         *
         * @param superClass 类名
         * @return this
         */
        public Builder superClass(@NotNull String superClass) {
            this.mapper.superClass = superClass;
            return this;
        }

        /**
         * 父类Mapper
         *
         * @param superClass 类
         * @return this
         * @since 3.5.0
         */
        public Builder superClass(@NotNull Class<?> superClass) {
            return superClass(superClass.getName());
        }

        /**
         * 标记 Mapper 注解
         *
         * @param annotationClass 注解Class
         * @return this
         * @since 3.5.3
         */
        public Builder mapperAnnotation(Class<? extends Annotation> annotationClass) {
            this.mapper.mapperAnnotationClass = annotationClass;
            return this;
        }

        /**
         * 开启baseResultMap
         *
         * @return this
         * @since 3.5.0
         */
        public Builder enableBaseResultMap() {
            this.mapper.baseResultMap = true;
            return this;
        }

        /**
         * 开启baseColumnList
         *
         * @return this
         * @since 3.5.0
         */
        public Builder enableBaseColumnList() {
            this.mapper.baseColumnList = true;
            return this;
        }

        /**
         * 设置缓存实现类
         *
         * @param cache 缓存实现
         * @return this
         * @since 3.5.0
         */
        public Builder cache(@NotNull Class<? extends Cache> cache) {
            this.mapper.cache = cache;
            return this;
        }

        /**
         * 输出Mapper文件名称转换
         *
         * @param converter 　转换处理
         * @return this
         * @since 3.5.0
         */
        public Builder convertMapperFileName(@NotNull Function<String, String> converter) {
            this.mapper.converterMapperFileName = converter;
            return this;
        }

        /**
         * 转换Xml文件名称处理
         *
         * @param converter 　转换处理
         * @return this
         * @since 3.5.0
         */
        public Builder convertXmlFileName(@NotNull Function<String, String> converter) {
            this.mapper.converterXmlFileName = converter;
            return this;
        }

        /**
         * 格式化Mapper文件名称
         *
         * @param format 　格式
         * @return this
         * @since 3.5.0
         */
        public Builder formatMapperFileName(@NotNull String format) {
            return convertMapperFileName((entityName) -> String.format(format, entityName));
        }

        /**
         * 格式化Xml文件名称
         *
         * @param format 格式
         * @return this
         * @since 3.5.0
         */
        public Builder formatXmlFileName(@NotNull String format) {
            return convertXmlFileName((entityName) -> String.format(format, entityName));
        }

        /**
         * 覆盖已有文件
         */
        public Builder enableFileOverride() {
            this.mapper.fileOverride = true;
            return this;
        }

        @NotNull
        public MapperTemplateArguments get() {
            return this.mapper;
        }
    }
}
