package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "file_generation_unit")
public class FileGenerationUnit {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "unit_name")
    private String unitName;

    @TableField(value = "remark")
    private String remark;
}
