package org.apache.ddlutils.jdbc;

import io.devpl.codegen.jdbc.SqlDataType;
import io.devpl.codegen.type.CommonJavaType;
import org.apache.ddlutils.jdbc.TypeAdapter;

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
