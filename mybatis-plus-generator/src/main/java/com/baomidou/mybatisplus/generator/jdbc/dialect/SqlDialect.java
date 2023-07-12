package com.baomidou.mybatisplus.generator.jdbc.dialect;

import com.baomidou.mybatisplus.generator.jdbc.CommonJavaType;
import com.baomidou.mybatisplus.generator.jdbc.SqlDataType;

public interface SqlDialect {

    SqlDataType sqlType(CommonJavaType javaDataType);

    CommonJavaType javaType(SqlDataType sqlDataType);
}
