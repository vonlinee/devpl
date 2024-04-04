package io.devpl.codegen.generator.config;

import io.devpl.codegen.ConstVal;
import io.devpl.sdk.util.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 模板路径配置项
 */
public class TemplateConfiguration {

    public static TemplateConfiguration.Builder builder() {
        return new TemplateConfiguration.Builder();
    }

    /**
     * 设置实体模板路径
     */
    private String entity;

    /**
     * 设置实体模板路径(kotlin模板)
     */
    private String entityKt;

    /**
     * 设置控制器模板路径
     */
    private String controller;

    /**
     * 设置Mapper模板路径
     */
    private String mapper;

    /**
     * 设置MapperXml模板路径
     */
    private String xml;

    /**
     * 设置Service模板路径
     */
    private String service;

    /**
     * 设置ServiceImpl模板路径
     */
    private String serviceImpl;

    /**
     * 是否禁用实体模板（默认 false）
     */
    private boolean disableEntity;

    /**
     * 不对外爆露
     */
    private TemplateConfiguration() {
        this.entity = ConstVal.TEMPLATE_ENTITY_JAVA;
        this.entityKt = ConstVal.TEMPLATE_ENTITY_KT;
        this.controller = ConstVal.TEMPLATE_CONTROLLER;
        this.mapper = ConstVal.TEMPLATE_MAPPER_JAVA;
        this.xml = ConstVal.TEMPLATE_MAPPER_XML;
        this.service = ConstVal.TEMPLATE_SERVICE;
        this.serviceImpl = ConstVal.TEMPLATE_SERVICE_IMPL;
    }

    /**
     * 获取实体模板路径
     *
     * @param kotlin 是否kotlin
     * @return 模板路径
     */
    public String getEntityTemplatePath(boolean kotlin) {
        if (!this.disableEntity) {
            if (kotlin) {
                return StringUtils.whenBlank(this.entityKt, ConstVal.TEMPLATE_ENTITY_KT);
            }
            return StringUtils.whenBlank(this.entity, ConstVal.TEMPLATE_ENTITY_JAVA);
        }
        return null;
    }

    /**
     * 禁用模板
     *
     * @param templateTypes 模板类型
     * @return this
     * @since 3.3.2
     */
    public TemplateConfiguration disable(@NotNull BuiltinTargetFile... templateTypes) {
        if (templateTypes != null) {
            for (BuiltinTargetFile templateType : templateTypes) {
                switch (templateType) {
                    case ENTITY_JAVA -> {
                        this.entity = null;
                        this.entityKt = null;
                        // 暂时没其他多的需求,使用一个单独的boolean变量进行支持一下.
                        this.disableEntity = true;
                    }
                    case CONTROLLER -> this.controller = null;
                    case MAPPER -> this.mapper = null;
                    case MAPPER_XML -> this.xml = null;
                    case SERVICE -> this.service = null;
                    case SERVICE_IMPL -> this.serviceImpl = null;
                    default -> {
                    }
                }
            }
        }
        return this;
    }

    /**
     * 禁用全部模板
     *
     * @return this
     */
    public TemplateConfiguration disable() {
        return disable(BuiltinTargetFile.values());
    }

    public String getService() {
        return service;
    }

    public String getServiceImpl() {
        return serviceImpl;
    }

    public String getMapper() {
        return mapper;
    }

    public String getXml() {
        return xml;
    }

    public String getController() {
        return controller;
    }

    /**
     * 模板路径配置构建者
     *
     * @author nieqiurong 3.5.0
     */
    public static class Builder {

        private final TemplateConfiguration templateConfiguration;

        /**
         * 默认生成一个空的
         */
        public Builder() {
            this.templateConfiguration = new TemplateConfiguration();
        }

        /**
         * 禁用所有模板
         *
         * @return this
         */
        public Builder disable() {
            this.templateConfiguration.disable();
            return this;
        }

        /**
         * 禁用模板
         *
         * @return this
         */
        public Builder disable(@NotNull BuiltinTargetFile... templateTypes) {
            this.templateConfiguration.disable(templateTypes);
            return this;
        }

        /**
         * 设置实体模板路径(JAVA)
         *
         * @param entityTemplate 实体模板
         * @return this
         */
        public Builder entity(@NotNull String entityTemplate) {
            this.templateConfiguration.disableEntity = false;
            this.templateConfiguration.entity = entityTemplate;
            return this;
        }

        /**
         * 设置实体模板路径(kotlin)
         *
         * @param entityKtTemplate 实体模板
         * @return this
         */
        public Builder entityKt(@NotNull String entityKtTemplate) {
            this.templateConfiguration.disableEntity = false;
            this.templateConfiguration.entityKt = entityKtTemplate;
            return this;
        }

        /**
         * 设置service模板路径
         *
         * @param serviceTemplate service接口模板路径
         * @return this
         */
        public Builder service(@NotNull String serviceTemplate) {
            this.templateConfiguration.service = serviceTemplate;
            return this;
        }

        /**
         * 设置serviceImpl模板路径
         *
         * @param serviceImplTemplate service实现类模板路径
         * @return this
         */
        public Builder serviceImpl(@NotNull String serviceImplTemplate) {
            this.templateConfiguration.serviceImpl = serviceImplTemplate;
            return this;
        }

        /**
         * 设置mapper模板路径
         *
         * @param mapperTemplate mapper模板路径
         * @return this
         */
        public Builder mapper(@NotNull String mapperTemplate) {
            this.templateConfiguration.mapper = mapperTemplate;
            return this;
        }

        /**
         * 设置mapperXml模板路径
         *
         * @param xmlTemplate xml模板路径
         * @return this
         */
        public Builder xml(@NotNull String xmlTemplate) {
            this.templateConfiguration.xml = xmlTemplate;
            return this;
        }

        /**
         * 设置控制器模板路径
         *
         * @param controllerTemplate 控制器模板路径
         * @return this
         */
        public Builder controller(@NotNull String controllerTemplate) {
            this.templateConfiguration.controller = controllerTemplate;
            return this;
        }

        /**
         * 构建模板配置对象
         *
         * @return 模板配置对象
         */
        public TemplateConfiguration build() {
            return this.templateConfiguration;
        }
    }
}
