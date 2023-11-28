package io.devpl.backend.domain.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MockColumnListParam {

    private Long dataSourceId;
    private String databaseName;
    private String tableName;
}
