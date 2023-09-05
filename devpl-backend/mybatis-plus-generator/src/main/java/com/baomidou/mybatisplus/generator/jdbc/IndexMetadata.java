package com.baomidou.mybatisplus.generator.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC index metadata
 */
public class IndexMetadata {
    private final String name;
    private final List<ColumnMetaInfo> columns = new ArrayList<>();

    IndexMetadata(ResultSet rs) throws SQLException {
        name = rs.getString("INDEX_NAME");
    }

    public String getName() {
        return name;
    }

    public void addColumn(ColumnMetaInfo column) {
        if (column != null) {
            columns.add(column);
        }
    }

    public ColumnMetaInfo[] getColumns() {
        return columns.toArray(new ColumnMetaInfo[0]);
    }

    @Override
    public String toString() {
        return "IndexMatadata(" + name + ')';
    }
}
