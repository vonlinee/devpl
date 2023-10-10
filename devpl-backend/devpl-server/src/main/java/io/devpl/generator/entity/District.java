package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 地区信息
 */
@Data
@TableName(value = "province_city_district")
public class District {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 父节点ID
     */
    private Integer pid;

    /**
     * 名称
     */
    private String name;
}
