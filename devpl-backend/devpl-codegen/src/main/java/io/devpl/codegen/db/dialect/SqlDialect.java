package io.devpl.codegen.db.dialect;

import io.devpl.codegen.jdbc.CommonJavaType;
import io.devpl.codegen.jdbc.SqlDataType;

public interface SqlDialect {

    SqlDataType sqlType(CommonJavaType javaDataType);

    CommonJavaType javaType(SqlDataType sqlDataType);
}