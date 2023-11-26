package io.devpl.generator.domain.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableImportParam {

    private Long dataSourceId;

    private String tableName;

    private Integer option;
}
