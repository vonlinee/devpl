package io.devpl.codegen.config;

import io.devpl.codegen.util.ClassUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service属性配置
 */
public class Service extends TemplateArgumentsForJavaClass implements TableInitializer {

    private final static Logger LOGGER = LoggerFactory.getLogger(Service.class);
    /**
     * 自定义继承的Service类全称，带包名
     */
    private String superServiceClass = ConstVal.SUPER_SERVICE_CLASS;
    /**
     * 自定义继承的ServiceImpl类全称，带包名
     */
    private String superServiceImplClass = ConstVal.SUPER_SERVICE_IMPL_CLASS;
    /**
     * 转换输出Service文件名称
     *
     * @since 3.5.0
     */
    private Function<String, String> converterServiceFileName = (entityName -> "I" + entityName + ConstVal.SERVICE);
    /**
     * 转换输出ServiceImpl文件名称
     *
     * @since 3.5.0
     */
    private Function<String, String> converterServiceImplFileName = (entityName -> entityName + ConstVal.SERVICE_IMPL);
    /**
     * 是否覆盖已有文件（默认 false）
     *
     * @since 3.5.2
     */
    private boolean fileOverride;

    private Service() {
    }

    @NotNull
    public String getSuperServiceClass() {
        return superServiceClass;
    }

    @NotNull
    public String getSuperServiceImplClass() {
        return superServiceImplClass;
    }

    @NotNull
    public Function<String, String> getConverterServiceFileName() {
        return converterServiceFileName;
    }

    @NotNull
    public Function<String, String> getConverterServiceImplFileName() {
        return converterServiceImplFileName;
    }

    public boolean isFileOverride() {
        return fileOverride;
    }

    @Override
    @NotNull
    public Map<String, Object> renderData(@NotNull IntrospectedTable tableInfo) {
        Map<String, Object> data = new HashMap<>();
        data.put("superServiceClassPackage", this.superServiceClass);
        data.put("superServiceClass", ClassUtils.getSimpleName(this.superServiceClass));
        data.put("superServiceImplClassPackage", this.superServiceImplClass);
        data.put("superServiceImplClass", ClassUtils.getSimpleName(this.superServiceImplClass));
        return data;
    }

    public static class Builder extends BaseBuilder {

        private final Service service = new Service();

        public Builder(@NotNull StrategyConfig strategyConfig) {
            super(strategyConfig);
        }

        /**
         * Service接口父类
         *
         * @param clazz 类
         * @return this
         */
        public Builder superServiceClass(@NotNull Class<?> clazz) {
            return superServiceClass(clazz.getName());
        }

        /**
         * Service接口父类
         *
         * @param superServiceClass 类名
         * @return this
         */
        public Builder superServiceClass(@NotNull String superServiceClass) {
            this.service.superServiceClass = superServiceClass;
            return this;
        }

        /**
         * Service实现类父类
         *
         * @param clazz 类
         * @return this
         */
        public Builder superServiceImplClass(@NotNull Class<?> clazz) {
            return superServiceImplClass(clazz.getName());
        }

        /**
         * Service实现类父类
         *
         * @param superServiceImplClass 类名
         * @return this
         */
        public Builder superServiceImplClass(@NotNull String superServiceImplClass) {
            this.service.superServiceImplClass = superServiceImplClass;
            return this;
        }

        /**
         * 转换输出service接口文件名称
         *
         * @param converter 　转换处理
         * @return this
         * @since 3.5.0
         */
        public Builder convertServiceFileName(@NotNull Function<String, String> converter) {
            this.service.converterServiceFileName = converter;
            return this;
        }

        /**
         * 转换输出service实现类文件名称
         *
         * @param converter 　转换处理
         * @return this
         * @since 3.5.0
         */
        public Builder convertServiceImplFileName(@NotNull Function<String, String> converter) {
            this.service.converterServiceImplFileName = converter;
            return this;
        }

        /**
         * 格式化service接口文件名称
         *
         * @param format 　格式
         * @return this
         * @since 3.5.0
         */
        public Builder formatServiceFileName(@NotNull String format) {
            return convertServiceFileName((entityName) -> String.format(format, entityName));
        }

        /**
         * 格式化service实现类文件名称
         *
         * @param format 　格式
         * @return this
         * @since 3.5.0
         */
        public Builder formatServiceImplFileName(@NotNull String format) {
            return convertServiceImplFileName((entityName) -> String.format(format, entityName));
        }

        /**
         * 覆盖已有文件（该方法后续会删除，替代方法为enableFileOverride方法）
         *
         * @see #enableFileOverride()
         */
        @Deprecated
        public Builder fileOverride() {
            LOGGER.warn("fileOverride方法后续会删除，替代方法为enableFileOverride方法");
            this.service.fileOverride = true;
            return this;
        }

        /**
         * 覆盖已有文件
         */
        public Builder enableFileOverride() {
            this.service.fileOverride = true;
            return this;
        }

        @NotNull
        public Service get() {
            return this.service;
        }
    }
}
