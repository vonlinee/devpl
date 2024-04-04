package io.devpl.codegen.db.dialect;

import io.devpl.codegen.jdbc.SqlDataType;
import io.devpl.codegen.type.CommonJavaType;

public class DefaultTypeAdapter implements TypeAdapter {

    @Override
    public SqlDataType sqlType(CommonJavaType javaDataType) {
        return null;
    }

    @Override
    public CommonJavaType javaType(SqlDataType sqlDataType) {
        return null;
    }
}
