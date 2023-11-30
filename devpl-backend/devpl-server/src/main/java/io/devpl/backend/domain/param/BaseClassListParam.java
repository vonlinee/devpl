package io.devpl.backend.domain.param;

import io.devpl.backend.common.query.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseClassListParam extends PageParam {

    private Long id;

    private String code;

    private boolean needFieldInfo = true;
}
