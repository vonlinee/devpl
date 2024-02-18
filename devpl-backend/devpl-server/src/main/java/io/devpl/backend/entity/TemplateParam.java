package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 模板参数表(形参)
 *
 * @author vonline
 * @see TemplateArgument 实参
 * @since 1.0.0 2023-07-29
 */
@Getter
@Setter
@TableName("template_param")
public class TemplateParam {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 模板ID，为null表示所有模板通用的参数
     */
    private Integer templateId;

    /**
     * 参数key, 一般为出现在模板中的变量名,单个模板内唯一
     */
    private String paramKey;

    /**
     * 参数名
     */
    private String paramName;

    /**
     * 参数值,默认参数值, 未提供该参数时使用此值
     * 暂时仅支持字面值，不支持动态表达式求值
     */
    private String paramValue;

    /**
     * 数据类型, 统一所有的数据类型
     */
    private String dataType;

    /**
     * 备注信息
     */
    private String remark;
}
