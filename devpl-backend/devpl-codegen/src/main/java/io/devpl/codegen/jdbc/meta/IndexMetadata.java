package io.devpl.codegen.jdbc.meta;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JDBC index metadata
 *
 * @see java.sql.DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)
 */
@Data
public class IndexMetadata {

    /**
     * TABLE_CAT String => table catalog (may be null)
     **/
    private String tableCatalog;
    /**
     * TABLE_SCHEM String => table schema (maybe null)
     **/
    private String tableSchema;
    /**
     * TABLE_NAME String => table name
     **/
    private String tableName;
    /**
     * NON_UNIQUE boolean => Can index values be non-unique. false when TYPE is tableIndexStatistic
     **/
    private boolean nonUnique;
    /**
     * INDEX_QUALIFIER String => index catalog (may be null); null when TYPE is tableIndexStatistic
     **/
    private String indexQualifier;
    /**
     * INDEX_NAME String => index name; null when TYPE is tableIndexStatistic
     **/
    private String indexName;
    /**
     * TYPE short => index type:
     * <li>tableIndexStatistic - this identifies table statistics that are returned in conjunction with a table's index descriptions</li>
     * <li>tableIndexClustered - this is a clustered index</li>
     * <li>tableIndexHashed - this is a hashed index</li>
     * <li>tableIndexOther - this is some other style of index</li>
     **/
    private short type;
    /**
     * ORDINAL_POSITION short => column sequence number within index; zero when TYPE is tableIndexStatistic
     **/
    private short ordinalPosition;
    /**
     * COLUMN_NAME String => column name; null when TYPE is tableIndexStatistic
     **/
    private String columnName;
    /**
     * ASC_OR_DESC String => column sort sequence, "A" => ascending, "D" => descending, may be null if sort sequence is not supported; null when TYPE is tableIndexStatistic
     **/
    private String ascOrDesc;
    /**
     * CARDINALITY long => When TYPE is tableIndexStatistic, then this is the number of rows in the table; otherwise, it is the number of unique values in the index.
     **/
    private long cardinality;
    /**
     * PAGES long => When TYPE is tableIndexStatistic then this is the number of pages used for the table, otherwise it is the number of pages used for the current index.
     **/
    private long pages;
    /**
     * FILTER_CONDITION String => Filter condition, if any. (maybe null)
     **/
    private String filterCondition;

    public void initialize(ResultSet resultSet) throws SQLException {
        this.tableCatalog = resultSet.getString(1);
        this.tableSchema = resultSet.getString(2);
        this.tableName = resultSet.getString(3);
        this.nonUnique = resultSet.getBoolean(4);
        this.indexQualifier = resultSet.getString(5);
        this.indexName = resultSet.getString(6);
        this.type = resultSet.getShort(7);
        this.ordinalPosition = resultSet.getShort(8);
        this.columnName = resultSet.getString(9);
        this.ascOrDesc = resultSet.getString(10);
        this.cardinality = resultSet.getShort(11);
        this.pages = resultSet.getShort(12);
        this.filterCondition = resultSet.getString(13);
    }
}
