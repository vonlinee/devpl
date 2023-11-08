package io.devpl.generator.domain.param;

import io.devpl.generator.common.PageQuery;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataTypeListParam extends PageQuery {

    private String typeGroupId;

    private String typeKey;

    private String typeName;
}
