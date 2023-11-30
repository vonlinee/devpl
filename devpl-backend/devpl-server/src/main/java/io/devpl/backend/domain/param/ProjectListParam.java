package io.devpl.backend.domain.param;

import io.devpl.backend.common.query.PageParam;
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
