package io.devpl.generator.entity;

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
    private int id;

    /**
     * 父节点ID
     */
    private int pid;

    /**
     * 名称
     */
    private String name;
}
