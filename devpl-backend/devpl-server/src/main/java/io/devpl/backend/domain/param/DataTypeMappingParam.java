package io.devpl.backend.domain.param;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataTypeMappingParam {

    private Long typeId;

    private Long anotherTypeId;

    private List<DataTypeMappingParam> mappings;
}
