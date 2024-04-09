package org.apache.ddlutils.jdbc;

import io.devpl.codegen.jdbc.SqlDataType;
import io.devpl.codegen.type.CommonJavaType;

public interface TypeAdapter {

    SqlDataType sqlType(CommonJavaType javaDataType);

    CommonJavaType javaType(SqlDataType sqlDataType);
}
