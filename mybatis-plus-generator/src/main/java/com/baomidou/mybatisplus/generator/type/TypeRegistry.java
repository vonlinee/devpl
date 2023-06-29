/*
 * Copyright (c) 2011-2021, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.generator.type;

import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 类型注册处理类
 * @author nieqiurong 2022/5/11.
 */
public class TypeRegistry {

    private static final Map<Integer, IColumnType> typeMap = new HashMap<>();

    static {
        // byte[]
        typeMap.put(Types.BINARY, DbColumnType.BYTE_ARRAY);
        typeMap.put(Types.BLOB, DbColumnType.BYTE_ARRAY);
        typeMap.put(Types.LONGVARBINARY, DbColumnType.BYTE_ARRAY);
        typeMap.put(Types.VARBINARY, DbColumnType.BYTE_ARRAY);
        //byte
        typeMap.put(Types.TINYINT, DbColumnType.BYTE);
        //long
        typeMap.put(Types.BIGINT, DbColumnType.LONG);
        //boolean
        typeMap.put(Types.BIT, DbColumnType.BOOLEAN);
        typeMap.put(Types.BOOLEAN, DbColumnType.BOOLEAN);
        //short
        typeMap.put(Types.SMALLINT, DbColumnType.SHORT);
        //string
        typeMap.put(Types.CHAR, DbColumnType.STRING);
        typeMap.put(Types.CLOB, DbColumnType.STRING);
        typeMap.put(Types.VARCHAR, DbColumnType.STRING);
        typeMap.put(Types.LONGVARCHAR, DbColumnType.STRING);
        typeMap.put(Types.LONGNVARCHAR, DbColumnType.STRING);
        typeMap.put(Types.NCHAR, DbColumnType.STRING);
        typeMap.put(Types.NCLOB, DbColumnType.STRING);
        typeMap.put(Types.NVARCHAR, DbColumnType.STRING);
        //date
        typeMap.put(Types.DATE, DbColumnType.DATE);
        //timestamp
        typeMap.put(Types.TIMESTAMP, DbColumnType.TIMESTAMP);
        //double
        typeMap.put(Types.FLOAT, DbColumnType.DOUBLE);
        typeMap.put(Types.REAL, DbColumnType.DOUBLE);
        //int
        typeMap.put(Types.INTEGER, DbColumnType.INTEGER);
        //bigDecimal
        typeMap.put(Types.NUMERIC, DbColumnType.BIG_DECIMAL);
        typeMap.put(Types.DECIMAL, DbColumnType.BIG_DECIMAL);
        //TODO 类型需要补充完整
    }

    /**
     * 根据字段元数据获取对应的Java类型
     * @param jdbcType JDBC类型
     * @param length   长度
     * @param dateType 日期类型
     * @param scale    精度，某些字段没有
     * @return Java类型
     */
    public static JavaType getColumnType(Integer jdbcType, Integer length, DateType dateType, Integer scale) {
        //TODO 是否用包装类??? 可以尝试判断字段是否允许为null来判断是否用包装类
        // TODO 需要增加类型处理，尚未补充完整
        JavaType columnType;
        switch (jdbcType) {
            case Types.BIT -> {
                if (length > 1) {
                    columnType = DbColumnType.BYTE_ARRAY;
                } else {
                    columnType = DbColumnType.BOOLEAN;
                }
            }
            case Types.DATE -> columnType = switch (dateType) {
                case SQL_PACK -> DbColumnType.DATE_SQL;
                case TIME_PACK -> DbColumnType.LOCAL_DATE;
                default -> DbColumnType.DATE;
            };
            case Types.TIME -> {
                if (dateType == DateType.TIME_PACK) {
                    columnType = DbColumnType.LOCAL_TIME;
                } else {
                    columnType = DbColumnType.TIME;
                }
            }
            case Types.DECIMAL, Types.NUMERIC -> {
                if (scale > 0 || length > 18) {
                    columnType = typeMap.get(jdbcType);
                } else if (length > 9) {
                    columnType = DbColumnType.LONG;
                } else if (length > 4) {
                    columnType = DbColumnType.INTEGER;
                } else {
                    columnType = DbColumnType.SHORT;
                }
            }
            case Types.TIMESTAMP -> {
                if (dateType == DateType.TIME_PACK) {
                    columnType = DbColumnType.LOCAL_DATE_TIME;
                } else if (dateType == DateType.ONLY_DATE) {
                    columnType = DbColumnType.DATE;
                } else {
                    columnType = DbColumnType.TIMESTAMP;
                }
            }
            default -> columnType = typeMap.getOrDefault(jdbcType, DbColumnType.OBJECT);
        }
        return columnType;
    }
}
