package io.devpl.backend.tools.mybatis;

import io.devpl.backend.domain.enums.MSParamDataType;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Set;

public interface MappedStatementParamExtractor {

    void apply(MappedStatement mappedStatement);

    MSParamDataType inferType(String paramName);

    Set<ParamMeta> getParams();
}
