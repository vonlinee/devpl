package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.devpl.backend.common.JsonTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数据表
 */
@Getter
@Setter
@TableName("table_generation")
public class TableGeneration {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 数据源ID
     */
    private Long datasourceId;

    /**
     * 数据库名
     */
    private String databaseName;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 实体类名称
     */
    private String className;

    /**
     * 功能名
     */
    private String tableComment;
    /**
     * 项目包名
     */
    private String packageName;
    /**
     * 项目版本号
     */
    private String version;
    /**
     * 作者
     */
    private String author;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 生成方式  1：zip压缩包   2：自定义目录
     */
    private Integer generatorType;
    /**
     * 后端生成路径
     */
    private String backendPath;
    /**
     * 前端生成路径
     */
    private String frontendPath;
    /**
     * 模块名
     */
    private String moduleName;
    /**
     * 功能名
     */
    private String functionName;
    /**
     * 表单布局
     */
    private Integer formLayout;

    /**
     * 基类ID
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Long baseclassId;

    /**
     * 逻辑删除字段
     */
    @TableField(value = "is_deleted")
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 数据模型
     */
    @TableField(value = "template_arguments", typeHandler = JsonTypeHandler.class)
    private Map<String, Object> templateArguments;

    // 非数据库字段

    /**
     * 字段列表
     */
    @TableField(exist = false)
    private List<TableGenerationField> fieldList;

    /**
     * 连接名称
     */
    @TableField(exist = false)
    private String connectionName;
}
