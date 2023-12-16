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

import io.devpl.codegen.config.GlobalConfig;
import io.devpl.codegen.config.ITypeConvert;
import io.devpl.codegen.db.ColumnJavaType;
import io.devpl.codegen.db.DbColumnType;

import static io.devpl.codegen.db.DbColumnType.*;

/**
 * SQLServer 字段类型转换
 *
 * @author hubin, hanchunlin
 * @since 2017-01-20
 */
public class SqlServerTypeConvert implements ITypeConvert {

    public static final SqlServerTypeConvert INSTANCE = new SqlServerTypeConvert();

    /**
     * 转换为日期类型
     *
     * @param config    配置信息
     * @param fieldType 类型
     * @return 返回对应的列类型
     */
    public static ColumnJavaType toDateType(GlobalConfig config, String fieldType) {
        return switch (config.getDateType()) {
            case SQL_PACK -> switch (fieldType) {
                case "date" -> DATE_SQL;
                case "time" -> TIME;
                default -> TIMESTAMP;
            };
            case TIME_PACK -> switch (fieldType) {
                case "date" -> LOCAL_DATE;
                case "time" -> LOCAL_TIME;
                default -> LOCAL_DATE_TIME;
            };
            default -> DATE;
        };
    }

    /**
     * @inheritDoc
     */
    @Override
    public ColumnJavaType processTypeConvert(GlobalConfig config, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("char", "xml", "text").then(DbColumnType.STRING))
            .test(TypeConverts.contains("bigint").then(DbColumnType.LONG))
            .test(TypeConverts.contains("int").then(DbColumnType.INTEGER))
            .test(TypeConverts.containsAny("date", "time").then(t -> toDateType(config, t)))
            .test(TypeConverts.contains("bit").then(DbColumnType.BOOLEAN))
            .test(TypeConverts.containsAny("decimal", "numeric").then(DbColumnType.DOUBLE))
            .test(TypeConverts.contains("money").then(DbColumnType.BIG_DECIMAL))
            .test(TypeConverts.containsAny("binary", "image").then(DbColumnType.BYTE_ARRAY))
            .test(TypeConverts.containsAny("float", "real").then(DbColumnType.FLOAT))
            .or(DbColumnType.STRING);
    }
}
