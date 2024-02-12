package io.devpl.backend.domain.param;

import io.devpl.backend.common.query.PageParam;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MappedStatementListParam extends PageParam {

    private String statementId;

    private String statementType;

    private String namespace;
}
