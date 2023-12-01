package io.devpl.backend.domain.param;

import io.devpl.backend.common.query.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateInfoListParam extends PageParam {

    private String templateName;

    private String templateType;
}
