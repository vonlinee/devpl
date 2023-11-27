package io.devpl.generator.domain.param;

import io.devpl.generator.common.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectListParam extends PageParam {

    /**
     * 项目名称
     */
    private String projectName;
}
