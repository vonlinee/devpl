package io.devpl.backend.domain.bo;

import io.devpl.codegen.db.DBTypeEnum;
import lombok.Data;

import java.util.Objects;

/**
 * 表导入信息
 */
@Data
public class TableImportInfo {

    /**
     * 数据源 ID
     */
    private Long dataSourceId;

    /**
     * 数据库名称
     * 英文字母，合法的数据库名称
     */
    private String databaseName;

    /**
     * 表名
     * 英文字母，合法的表名称
     */
    private String tableName;

    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 数据库类型
     */
    private DBTypeEnum dbType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableImportInfo that = (TableImportInfo) o;
        return Objects.equals(dataSourceId, that.dataSourceId) && Objects.equals(databaseName, that.databaseName) && Objects.equals(tableName, that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataSourceId, databaseName, tableName);
    }
}
