package io.devpl.backend.domain.param;

import io.devpl.backend.common.query.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelListParam extends PageParam {

    private Long id;

    private String code;

    private boolean needFieldInfo = true;
}
