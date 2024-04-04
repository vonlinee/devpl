package io.devpl.codegen;

/**
 * 定义常量
 */
public interface ConstVal {

    String MODULE_NAME = "ModuleName";
    String ENTITY = "Entity";
    String SERVICE = "Service";
    String SERVICE_IMPL = "ServiceImpl";
    String MAPPER = "Mapper";
    String XML = "Xml";
    String CONTROLLER = "Controller";
    String PARENT = "Parent";

    String JAVA_TMPDIR = "java.io.tmpdir";

    String JAVA_SUFFIX = ".java";
    String KT_SUFFIX = ".kt";

    /**
     * 实体模板路径
     */
    String TEMPLATE_ENTITY_JAVA = "/templates/%s/entity.java";

    /**
     * 实体模板路径(kotlin模板)
     */
    String TEMPLATE_ENTITY_KT = "/templates/%s/entity.kt";

    /**
     * 控制器模板路径
     */
    String TEMPLATE_CONTROLLER = "/templates/%s/controller.java";

    /**
     * Mapper模板路径
     */
    String TEMPLATE_MAPPER_JAVA = "/templates/%s/mapper.java";

    /**
     * MapperXml模板路径
     */
    String TEMPLATE_MAPPER_XML = "/templates/%s/mapper.xml";

    /**
     * Service模板路径
     */
    String TEMPLATE_SERVICE = "/templates/%s/service.java";

    /**
     * ServiceImpl模板路径
     */
    String TEMPLATE_SERVICE_IMPL = "/templates/%s/serviceImpl.java";

    String SUPER_MAPPER_CLASS = "com.baomidou.mybatisplus.core.mapper.BaseMapper";
    String SUPER_SERVICE_CLASS = "com.baomidou.mybatisplus.extension.service.IService";
    String SUPER_SERVICE_IMPL_CLASS = "com.baomidou.mybatisplus.extension.service.impl.ServiceImpl";

}
