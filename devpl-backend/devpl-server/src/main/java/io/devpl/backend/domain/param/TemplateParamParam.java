package io.devpl.backend.domain.param;

import io.devpl.backend.entity.TemplateParam;
import lombok.Data;

import java.util.List;

@Data
public class TemplateParamParam {

    private List<TemplateParam> params;
}
