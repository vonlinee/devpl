package org.apache.ddlutils.platform.axion;


import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.model.RowData;
import org.apache.ddlutils.platform.DBType;
import org.apache.ddlutils.platform.DBTypeEnum;
import org.apache.ddlutils.platform.JDBCDriverTypeEnum;
import org.apache.ddlutils.platform.PlatformImplBase;

import java.sql.*;

/**
 * The platform for the Axion database.
 */
public class AxionPlatform extends PlatformImplBase {
    /**
     * Database name of this platform.
     */
    public static final String DATABASENAME = "Axion";

    /**
     * Creates a new axion platform instance.
     */
    public AxionPlatform() {
        PlatformInfo info = getPlatformInfo();

        info.setDelimitedIdentifiersSupported(false);
        info.setSqlCommentsSupported(false);
        info.setLastIdentityValueReadable(false);
        info.addNativeTypeMapping(Types.ARRAY, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.BIT, "BOOLEAN");
        info.addNativeTypeMapping(Types.DATALINK, "VARBINARY", Types.VARBINARY);
        info.addNativeTypeMapping(Types.DISTINCT, "VARBINARY", Types.VARBINARY);
        info.addNativeTypeMapping(Types.NULL, "VARBINARY", Types.VARBINARY);
        info.addNativeTypeMapping(Types.OTHER, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.REAL, "REAL", Types.FLOAT);
        info.addNativeTypeMapping(Types.REF, "VARBINARY", Types.VARBINARY);
        info.addNativeTypeMapping(Types.STRUCT, "VARBINARY", Types.VARBINARY);
        info.addNativeTypeMapping(Types.TINYINT, "SMALLINT", Types.SMALLINT);

        setSqlBuilder(new AxionBuilder(this));
        setModelReader(new AxionModelReader(this));
    }

    @Override
    public DBType getDBType() {
        return DBTypeEnum.AXION;
    }

    @Override
    public void createDatabase(String jdbcDriverClassName, String connectionUrl, String username, String password, RowData parameters) throws DatabaseOperationException, UnsupportedOperationException {
        // Axion will create the database automatically when connecting for the first time
        if (JDBCDriverTypeEnum.AXION.getDriverClassName().equals(jdbcDriverClassName)) {
            try {
                Class.forName(jdbcDriverClassName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            try (Connection connection = DriverManager.getConnection(connectionUrl, username, password)) {
                logWarnings(connection);
            } catch (Exception ex) {
                throw new DatabaseOperationException("Error while trying to create a database", ex);
            }
        } else {
            throw new UnsupportedOperationException("Unable to create a Axion database via the driver " + jdbcDriverClassName);
        }
    }

    @Override
    protected Object extractColumnValue(ResultSet resultSet, String columnName, int columnIdx, int jdbcType) throws SQLException {
        boolean useIdx = (columnName == null);
        Object value;
        if (jdbcType == Types.BIGINT) {// The Axion JDBC driver does not support reading BIGINT values directly
            String strValue = useIdx ? resultSet.getString(columnIdx) : resultSet.getString(columnName);
            value = resultSet.wasNull() ? null : Long.valueOf(strValue);
        } else {
            value = super.extractColumnValue(resultSet, columnName, columnIdx, jdbcType);
        }
        return value;
    }
}
