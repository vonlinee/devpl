package io.devpl.backend.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 数据类型映射关系树形节点VO
 * 两级的树形结构
 */
@Data
public class DataTypeMappingNodeVO {

    private Long id;
    private Long groupId;
    private String typeGroupId;
    private String typeName;
    private List<DataTypeMappingNodeVO> mappings;
}
