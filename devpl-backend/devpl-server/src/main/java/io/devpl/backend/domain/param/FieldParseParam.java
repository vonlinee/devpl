package io.devpl.backend.domain.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 字段解析参数
 */
@Getter
@Setter
public class FieldParseParam {

    /**
     * 输入类型，针对不同的输入采用不同的解析策略
     * json
     * sql
     * 1.ddl
     * 2.qml
     * html
     * 1.html-table-dom
     * 2.html-table-text
     * other:
     * 1.url 解析url路径中的参数
     * 2.javaobj 解析JavaObject的toString方法打印的内容 (TODO)
     */
    @NotBlank(message = "输入类型为空")
    private String type;

    /**
     * 待解析的文本
     */
    @NotBlank(message = "待解析的文本为空")
    private String content;

    // ================= type = html  start ================================

    /**
     * 列映射: 字段名称
     */
    private String fieldNameColumn;

    /**
     * 列映射: 字段数据类型
     */
    private String fieldTypeColumn;

    /**
     * 列映射: 字段描述信息
     */
    private String fieldDescColumn;

    // ================= type = html  end ================================

    // ================= type = json start ================================
    /**
     * 是否解析多层JSON结构
     */
    private Boolean recursive = false;
    /**
     * 多层JSON结构如何返回 flat(平铺结构返回), tree(树形结构返回)
     */
    private String resultType = "flat";

    public boolean isParseJsonTreeEnabled() {
        return "tree".equalsIgnoreCase(resultType);
    }

    // ================= type = json end ================================

    // ================= type = sql start ================================

    /**
     * 数据库类型
     */
    private String dbType = "mysql";

    /**
     * SQL类型
     */
    private String sqlType = "DDL";

    // ================= type = sql end ================================
}
