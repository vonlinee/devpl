/*
 * Copyright (c) 2011-2021, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baomidou.mybatisplus.generator.config.converts;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.ColumnJavaType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;

/**
 * MYSQL 数据库字段类型转换
 * bit类型数据转换 bit(1) -> Boolean类型  bit(2->64)  -> Byte类型
 *
 * @author hubin, hanchunlin, xiaoliang
 * @since 2017-01-20
 */
public class MySqlTypeConvert implements ITypeConvert {
    public static final MySqlTypeConvert INSTANCE = new MySqlTypeConvert();

    /**
     * 转换为日期类型
     *
     * @param config 配置信息
     * @param type   类型
     * @return 返回对应的列类型
     */
    public static ColumnJavaType toDateType(GlobalConfig config, String type) {
        String dateType = type.replaceAll("\\(\\d+\\)", "");
        return switch (config.getDateType()) {
            case ONLY_DATE -> DbColumnType.DATE;
            case SQL_PACK -> switch (dateType) {
                case "date", "year" -> DbColumnType.DATE_SQL;
                case "time" -> DbColumnType.TIME;
                default -> DbColumnType.TIMESTAMP;
            };
            case TIME_PACK -> switch (dateType) {
                case "date" -> DbColumnType.LOCAL_DATE;
                case "time" -> DbColumnType.LOCAL_TIME;
                case "year" -> DbColumnType.YEAR;
                default -> DbColumnType.LOCAL_DATE_TIME;
            };
        };
    }

    /**
     * @inheritDoc
     */
    @Override
    public ColumnJavaType processTypeConvert(GlobalConfig config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("char", "text", "json", "enum").then(DbColumnType.STRING))
            .test(TypeConverts.contains("bigint").then(DbColumnType.LONG))
            .test(TypeConverts.containsAny("tinyint(1)", "bit(1)").then(DbColumnType.BOOLEAN))
            .test(TypeConverts.contains("bit").then(DbColumnType.BYTE))
            .test(TypeConverts.contains("int").then(DbColumnType.INTEGER))
            .test(TypeConverts.contains("decimal").then(DbColumnType.BIG_DECIMAL))
            .test(TypeConverts.contains("clob").then(DbColumnType.CLOB))
            .test(TypeConverts.contains("blob").then(DbColumnType.BLOB))
            .test(TypeConverts.contains("binary").then(DbColumnType.BYTE_ARRAY))
            .test(TypeConverts.contains("float").then(DbColumnType.FLOAT))
            .test(TypeConverts.contains("double").then(DbColumnType.DOUBLE))
            .test(TypeConverts.containsAny("date", "time", "year").then(t -> toDateType(config, t)))
            .or(DbColumnType.STRING);
    }
}
