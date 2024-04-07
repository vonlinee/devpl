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
package io.devpl.codegen.db.converts;

import io.devpl.codegen.db.JavaFieldDataType;
import io.devpl.codegen.db.DbFieldDataType;
import io.devpl.codegen.generator.config.GlobalConfiguration;
import io.devpl.codegen.generator.config.TypeConverter;

/**
 * MYSQL 数据库字段类型转换
 */
public class FirebirdTypeConverter implements TypeConverter {
    public static final FirebirdTypeConverter INSTANCE = new FirebirdTypeConverter();

    /**
     * 转换为日期类型
     *
     * @param config 配置信息
     * @param type   类型
     * @return 返回对应的列类型
     */
    public static JavaFieldDataType toDateType(GlobalConfiguration config, String type) {
        return switch (config.getDateType()) {
            case ONLY_DATE -> DbFieldDataType.DATE;
            case SQL_PACK -> switch (type) {
                case "date", "year" -> DbFieldDataType.DATE_SQL;
                case "time" -> DbFieldDataType.TIME;
                default -> DbFieldDataType.TIMESTAMP;
            };
            case TIME_PACK -> switch (type) {
                case "date" -> DbFieldDataType.LOCAL_DATE;
                case "time" -> DbFieldDataType.LOCAL_TIME;
                case "year" -> DbFieldDataType.YEAR;
                default -> DbFieldDataType.LOCAL_DATE_TIME;
            };
        };
    }

    /**
     * @inheritDoc
     */
    @Override
    public JavaFieldDataType convert(GlobalConfiguration config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("cstring", "text").then(DbFieldDataType.STRING))
            .test(TypeConverts.contains("short").then(DbFieldDataType.SHORT))
            .test(TypeConverts.contains("long").then(DbFieldDataType.LONG))
            .test(TypeConverts.contains("float").then(DbFieldDataType.FLOAT))
            .test(TypeConverts.contains("double").then(DbFieldDataType.DOUBLE))
            .test(TypeConverts.contains("blob").then(DbFieldDataType.BLOB))
            .test(TypeConverts.contains("int64").then(DbFieldDataType.LONG))
            .test(TypeConverts.containsAny("date", "time", "year").then(t -> toDateType(config, t)))
            .or(DbFieldDataType.STRING);
    }
}
