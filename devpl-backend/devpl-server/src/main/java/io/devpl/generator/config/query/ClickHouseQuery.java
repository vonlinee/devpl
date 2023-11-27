package io.devpl.generator.config.query;

import com.baomidou.mybatisplus.generator.jdbc.DBType;
import io.devpl.sdk.util.StringUtils;

/**
 * ClickHouse 表数据查询
 * @author ratelfu
 * @since 2021-03-10
 */
public class ClickHouseQuery implements AbstractQuery {

    @Override
    public String getTableFieldsQuerySql() {
        return "select * from system.columns where table='%s'";
    }

    @Override
    public DBType dbType() {
        return DBType.CLICK_HOUSE;
    }

    @Override
    public String getTableQuerySql(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM system.tables WHERE 1=1 ");

        // 表名查询
        if (StringUtils.hasText(tableName)) {
            sql.append("and name = '").append(tableName).append("' ");
        }
        return sql.toString();
    }

    @Override
    public String getTableNameResultSetColumnName() {
        return "name";
    }

    @Override
    public String tableComment() {
        return "comment";
    }

    @Override
    public String fieldName() {
        return "name";
    }


    @Override
    public String fieldType() {
        return "type";
    }


    @Override
    public String fieldComment() {
        return "comment";
    }


    @Override
    public String fieldKey() {
        return "is_in_primary_key";
    }


}
