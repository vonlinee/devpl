package io.devpl.codegen.db.dialect.mysql;

import io.devpl.codegen.type.CommonJavaType;
import io.devpl.codegen.jdbc.SqlDataType;
import io.devpl.codegen.db.dialect.SqlDialect;

public class MySQL5Dialect implements SqlDialect {

    @Override
    public SqlDataType sqlType(CommonJavaType javaDataType) {
        return null;
    }

    @Override
    public CommonJavaType javaType(SqlDataType sqlDataType) {
        return null;
    }
}
