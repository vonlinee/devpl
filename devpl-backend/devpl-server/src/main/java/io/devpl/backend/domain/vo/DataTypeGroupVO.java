package io.devpl.backend.domain.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataTypeGroupVO {

    private Long id;
    private String typeGroupId;
    private String typeGroupName;
    private String remark;
    private boolean internal;
}
