package io.devpl.backend.domain.param;

import io.devpl.backend.common.PageParam;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataTypeListParam extends PageParam {

    private String typeGroupId;

    private String typeKey;

    private String typeName;
}
