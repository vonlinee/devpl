package io.devpl.backend.domain.param;

import io.devpl.backend.common.query.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldInfoListParam extends PageParam {

    private String fieldKey;
    private String fieldName;

    /**
     * 逗号拼接
     */
    private String excludedKeys;
}
