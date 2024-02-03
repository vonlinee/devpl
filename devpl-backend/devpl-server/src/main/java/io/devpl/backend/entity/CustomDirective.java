package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("custom_directive")
public class CustomDirective {
    /**
     * 指令ID
     */
    @TableId(value = "directive_id", type = IdType.AUTO)
    private Long directiveId;

    /**
     * 指令名称
     */
    @TableField(value = "directive_name")
    private String directiveName;

    /**
     * 实现部分代码
     */
    @TableField(value = "source_code")
    private String sourceCode;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 备注信息
     */
    @TableField(value = "remark")
    private String remark;
}
