package io.devpl.backend.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 项目选择VO
 */
@Setter
@Getter
public class ProjectSelectVO {

    private Long projectId;

    private String projectName;

    public ProjectSelectVO(Long projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }
}
