package io.devpl.backend.domain.param;

import io.devpl.backend.common.query.PageParam;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataTypeMappingParam extends PageParam {

    private Long typeId;

    private Long anotherTypeId;

    private List<DataTypeMappingParam> mappings;
}
