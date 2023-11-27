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
package com.baomidou.mybatisplus.generator.config.converts;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.ColumnJavaType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;

/**
 * MYSQL 数据库字段类型转换
 *
 * @author hubin, hanchunlin
 * @since 2017-01-20
 */
public class FirebirdTypeConvert implements ITypeConvert {
    public static final FirebirdTypeConvert INSTANCE = new FirebirdTypeConvert();

    /**
     * 转换为日期类型
     *
     * @param config 配置信息
     * @param type   类型
     * @return 返回对应的列类型
     */
    public static ColumnJavaType toDateType(GlobalConfig config, String type) {
        return switch (config.getDateType()) {
            case ONLY_DATE -> DbColumnType.DATE;
            case SQL_PACK -> switch (type) {
                case "date", "year" -> DbColumnType.DATE_SQL;
                case "time" -> DbColumnType.TIME;
                default -> DbColumnType.TIMESTAMP;
            };
            case TIME_PACK -> switch (type) {
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
            .test(TypeConverts.containsAny("cstring", "text").then(DbColumnType.STRING))
            .test(TypeConverts.contains("short").then(DbColumnType.SHORT))
            .test(TypeConverts.contains("long").then(DbColumnType.LONG))
            .test(TypeConverts.contains("float").then(DbColumnType.FLOAT))
            .test(TypeConverts.contains("double").then(DbColumnType.DOUBLE))
            .test(TypeConverts.contains("blob").then(DbColumnType.BLOB))
            .test(TypeConverts.contains("int64").then(DbColumnType.LONG))
            .test(TypeConverts.containsAny("date", "time", "year").then(t -> toDateType(config, t)))
            .or(DbColumnType.STRING);
    }
}
