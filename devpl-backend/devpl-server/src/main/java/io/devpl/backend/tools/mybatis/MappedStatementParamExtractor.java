package io.devpl.backend.tools.mybatis;

import io.devpl.codegen.type.TypeInferenceStrategy;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Collection;

public interface MappedStatementParamExtractor {

    void setTypeInferenceStrategy(TypeInferenceStrategy<StatementParam> strategy);

    void apply(MappedStatement mappedStatement);

    Collection<StatementParam> getParams();
}
