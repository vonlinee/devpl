package io.devpl.codegen.db.dialect;

import io.devpl.codegen.jdbc.SqlDataType;
import io.devpl.codegen.type.CommonJavaType;

public interface TypeAdapter {

    SqlDataType sqlType(CommonJavaType javaDataType);

    CommonJavaType javaType(SqlDataType sqlDataType);
}
