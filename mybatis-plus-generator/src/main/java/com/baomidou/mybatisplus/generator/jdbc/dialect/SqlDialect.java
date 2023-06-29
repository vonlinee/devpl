package com.baomidou.mybatisplus.generator.jdbc.dialect;

import com.baomidou.mybatisplus.generator.jdbc.SqlDataType;
import com.baomidou.mybatisplus.generator.jdbc.CommonJavaType;

public interface SqlDialect {

    SqlDataType sqlType(CommonJavaType javaDataType);

    CommonJavaType javaType(SqlDataType sqlDataType);
}
