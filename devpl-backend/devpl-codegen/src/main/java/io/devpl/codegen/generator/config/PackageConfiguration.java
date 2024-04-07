package io.devpl.codegen.generator.config;

import io.devpl.codegen.ConstVal;
import io.devpl.sdk.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 包相关的配置项
 */
public class PackageConfiguration {

    /**
     * 包配置信息
     */
    private final Map<String, String> packageInfo = new HashMap<>();
    /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    private String parent = "org.example";

    /**
     * 父包模块名
     */
    private String moduleName = "";
    /**
     * Entity包名
     */
    private String entity = "entity";

    /**
     * Service包名
     */
    private String service = "service";

    /**
     * Service Impl包名
     */
    private String serviceImpl = "service.impl";

    /**
     * Mapper包名
     */
    private String mapper = "mapper";

    /**
     * Mapper XML包名
     */
    private String xml = "mapper.xml";

    /**
     * Controller包名
     */
    private String controller = "controller";

    private PackageConfiguration() {
    }

    /**
     * 父包名
     */
    @NotNull
    public String getParent() {
        if (StringUtils.hasText(moduleName)) {
            return parent + "." + moduleName;
        }
        return parent;
    }

    /**
     * 连接父子包名
     *
     * @param subPackage 子包名
     * @return 连接后的包名
     */
    @NotNull
    public String joinPackage(String subPackage) {
        String parent = getParent();
        return !StringUtils.hasText(parent) ? subPackage : (parent + "." + subPackage);
    }

    /**
     * 获取包配置信息
     *
     * @return 包配置信息
     * @since 3.5.0
     */
    @NotNull
    public Map<String, String> getPackageInfo() {
        if (packageInfo.isEmpty()) {
            packageInfo.put(ConstVal.MODULE_NAME, this.getModuleName());
            packageInfo.put(ConstVal.ENTITY, this.joinPackage(this.getEntity()));
            packageInfo.put(ConstVal.MAPPER, this.joinPackage(this.getMapper()));
            packageInfo.put(ConstVal.XML, this.joinPackage(this.getXml()));
            packageInfo.put(ConstVal.SERVICE, this.joinPackage(this.getService()));
            packageInfo.put(ConstVal.SERVICE_IMPL, this.joinPackage(this.getServiceImpl()));
            packageInfo.put(ConstVal.CONTROLLER, this.joinPackage(this.getController()));
            packageInfo.put(ConstVal.PARENT, this.getParent());
        }
        return Collections.unmodifiableMap(this.packageInfo);
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getEntity() {
        return entity;
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

    public static PackageConfiguration.Builder builder() {
        return new Builder();
    }

    /**
     * 构建者
     *
     * @author nieqiurong
     * @since 3.5.0
     */
    public static class Builder {

        private final PackageConfiguration packageConfig;

        public Builder() {
            this.packageConfig = new PackageConfiguration();
        }

        /**
         * 指定父包名
         *
         * @param parent 父包名
         * @return this
         */
        public Builder parentPackageName(@NotNull String parent) {
            this.packageConfig.parent = parent;
            return this;
        }

        /**
         * 指定模块名称
         *
         * @param moduleName 模块名
         * @return this
         */
        public Builder moduleName(@NotNull String moduleName) {
            this.packageConfig.moduleName = moduleName;
            return this;
        }

        /**
         * 指定实体包名
         *
         * @param entity 实体包名
         * @return this
         */
        public Builder entity(@NotNull String entity) {
            this.packageConfig.entity = entity;
            return this;
        }

        /**
         * 指定service接口包名
         *
         * @param service service包名
         * @return this
         */
        public Builder service(@NotNull String service) {
            this.packageConfig.service = service;
            return this;
        }

        /**
         * service实现类包名
         *
         * @param serviceImpl service实现类包名
         * @return this
         */
        public Builder serviceImpl(@NotNull String serviceImpl) {
            this.packageConfig.serviceImpl = serviceImpl;
            return this;
        }

        /**
         * 指定mapper接口包名
         *
         * @param mapper mapper包名
         * @return this
         */
        public Builder mapper(@NotNull String mapper) {
            this.packageConfig.mapper = mapper;
            return this;
        }

        /**
         * 指定xml包名
         *
         * @param xml xml包名
         * @return this
         */
        public Builder xml(@NotNull String xml) {
            this.packageConfig.xml = xml;
            return this;
        }

        /**
         * 指定控制器包名
         *
         * @param controller 控制器包名
         * @return this
         */
        public Builder controller(@NotNull String controller) {
            this.packageConfig.controller = controller;
            return this;
        }

        /**
         * 连接父子包名
         *
         * @param subPackage 子包名
         * @return 连接后的包名
         */
        @NotNull
        public String joinPackage(@NotNull String subPackage) {
            return this.packageConfig.joinPackage(subPackage);
        }

        public PackageConfiguration build() {
            return this.packageConfig;
        }
    }
}
