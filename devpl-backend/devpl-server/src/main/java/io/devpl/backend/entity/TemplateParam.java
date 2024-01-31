package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 模板参数表
 *
 * @author 111 222
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
     * 模板ID
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
