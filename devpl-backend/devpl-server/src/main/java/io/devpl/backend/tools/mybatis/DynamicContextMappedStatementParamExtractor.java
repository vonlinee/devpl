package io.devpl.backend.tools.mybatis;

import io.devpl.backend.domain.enums.MSParamDataType;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Set;

public class DynamicContextMappedStatementParamExtractor  implements MappedStatementParamExtractor {
    @Override
    public void apply(MappedStatement mappedStatement) {

    }

    @Override
    public MSParamDataType inferType(String paramName) {
        return null;
    }

    @Override
    public Set<ParamMeta> getParams() {
        return null;
    }
}
