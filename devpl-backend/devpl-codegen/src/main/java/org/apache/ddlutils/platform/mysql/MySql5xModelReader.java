package org.apache.ddlutils.platform.mysql;


import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.util.CollectionUtils;
import org.apache.ddlutils.util.ContextMap;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Reads a database model from a MySql 5 database.
 */
public class MySql5xModelReader extends MySqlModelReader {
    /**
     * Creates a new model reader for MySql 5 databases.
     *
     * @param platform The platform that this model reader belongs to
     */
    public MySql5xModelReader(Platform platform) {
        super(platform);
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, ContextMap values) throws SQLException {
        Column column = super.readColumn(metaData, values);
        // make sure the default-value is null when an empty is returned.
        if ("".equals(column.getDefaultValue())) {
            column.setDefaultValue(null);
        }
        return column;
    }

    /**
     * mysql数据库,在8.0之前 nullCatalogMeansCurrent默认是true，
     * 在8.0后默认是false。如果值为false，会获取数据库连接地址中所有数据信息，而不是指定数据库的信息。具体示例如下：
     * 如果需要获取指定数据库信息nullCatalogMeansCurrent=true
     * nullCatalogMeansCurrent表示如果catalog为null，则使用连接字符串中的数据库名称(如果有的话)
     *
     * @param catalog       The catalog to access in the database; use <code>null</code> for the default value
     * @param schemaPattern The schema(s) to access in the database; use <code>null</code> for the default value
     * @param tableTypes    The table types to process; use <code>null</code> or an empty list for the default ones
     * @return 表信息
     * @throws SQLException 数据库操作异常
     */
    @Override
    protected Collection<Table> readTables(String catalog, String schemaPattern, String[] tableTypes) throws SQLException {
        Collection<Table> tables = super.readTables(catalog, schemaPattern, tableTypes);

        if (CollectionUtils.isEmpty(tables)) {
            return tables;
        }

        StringBuilder sb = new StringBuilder();
        Set<String> wrappedDbNames = CollectionUtils.toSet(tables, table -> {
            sb.setLength(0);
            sb.append("'").append(table.getCatalog()).append("'");
            return sb.toString();
        });

        String schemaNameInCondition = wrappedDbNames.stream().collect(Collectors.joining(",", "(", ")"));

        /*
          MySQL 的驱动源程序 在获取表元数据的时候已经将表注释长度设置成 0 了
          所有通过调用 DatabaseMetaData 类的 getTables()方法获取到的 ResultSet 对象中都没有表注释信息
         */
        Map<String, String> tableRemarkMap = getTableRemarks(schemaNameInCondition);

        if (!CollectionUtils.isEmpty(tableRemarkMap)) {
            for (Table table : tables) {
                table.setDescription(tableRemarkMap.get(table.getName()));
            }
        }
        return tables;
    }

    @NotNull
    private Map<String, String> getTableRemarks(String schemaNameInCondition) throws SQLException {
        final String sql = "SELECT TABLE_NAME, TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA IN %s".formatted(schemaNameInCondition);
        Map<String, String> tableRemarkMap = new HashMap<>();
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    String tableName = rs.getString(1);
                    String tableRemark = rs.getString(2);
                    tableRemarkMap.put(tableName, tableRemark);
                }
            }
        }
        return tableRemarkMap;
    }
}
