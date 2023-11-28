package io.devpl.backend.domain.param;

import lombok.Data;

import java.util.List;

@Data
public class DataTypeMappingParam {

    private Long typeId;

    private Long anotherTypeId;

    private List<DataTypeMappingParam> mappings;
}
