package io.devpl.backend.domain.param;

import io.devpl.backend.common.query.PageParam;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GenTableListParam extends PageParam {

    private String tableName;
}
