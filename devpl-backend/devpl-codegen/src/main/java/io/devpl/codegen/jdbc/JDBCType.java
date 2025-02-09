package io.devpl.codegen.jdbc;

import io.devpl.codegen.type.DataType;

import java.sql.SQLType;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 遵循JDBC规范定义的类型
 * <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/jdbc_42.html">JDBC4.2 Specification</a>
 *
 * @see java.sql.SQLType
 * @see java.sql.JDBCType 基本上和java.sql.JDBCType一样，增加一些数据长度等信息
 * @see java.sql.Types
 */
public enum JDBCType implements SQLType, DataType {
    /*
     * This is added to enable basic support for the
     * ARRAY data type - but a custom type handler is still required
     */
    ARRAY(Types.ARRAY),
    BIT(Types.BIT),
    TINYINT(Types.TINYINT),
    SMALLINT(Types.SMALLINT),
    INTEGER(Types.INTEGER),
    BIGINT(Types.BIGINT),
    FLOAT(Types.FLOAT),
    REAL(Types.REAL),
    DOUBLE(Types.DOUBLE),
    NUMERIC(Types.NUMERIC),
    DECIMAL(Types.DECIMAL),
    CHAR(Types.CHAR),
    VARCHAR(Types.VARCHAR),
    LONGVARCHAR(Types.LONGVARCHAR),
    DATE(Types.DATE),
    TIME(Types.TIME),
    TIMESTAMP(Types.TIMESTAMP),
    BINARY(Types.BINARY),
    VARBINARY(Types.VARBINARY),
    LONGVARBINARY(Types.LONGVARBINARY),
    NULL(Types.NULL),
    OTHER(Types.OTHER),
    BLOB(Types.BLOB),
    CLOB(Types.CLOB),
    BOOLEAN(Types.BOOLEAN),
    CURSOR(-10), // Oracle
    UNDEFINED(Integer.MIN_VALUE + 1000),
    NVARCHAR(Types.NVARCHAR), // JDK6
    NCHAR(Types.NCHAR), // JDK6
    NCLOB(Types.NCLOB), // JDK6

    /**
     * 用户定义的对象
     */
    STRUCT(Types.STRUCT),
    JAVA_OBJECT(Types.JAVA_OBJECT),
    DISTINCT(Types.DISTINCT),
    REF(Types.REF),
    DATALINK(Types.DATALINK),
    ROWID(Types.ROWID), // JDK6
    LONGNVARCHAR(Types.LONGNVARCHAR), // JDK6
    SQLXML(Types.SQLXML), // JDK6
    DATETIMEOFFSET(-155), // SQL Server 2008
    TIME_WITH_TIMEZONE(Types.TIME_WITH_TIMEZONE), // JDBC 4.2 JDK8
    TIMESTAMP_WITH_TIMEZONE(Types.TIMESTAMP_WITH_TIMEZONE); // JDBC 4.2 JDK8

    private static final Map<Integer, JDBCType> codeLookup = new HashMap<>();

    static {
        for (JDBCType type : JDBCType.values()) {
            codeLookup.put(type.typeCode, type);
        }
    }

    public final int typeCode;

    JDBCType(int typeCode) {
        this.typeCode = typeCode;
    }

    public static JDBCType forCode(int code) {
        return codeLookup.get(code);
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getVendor() {
        return "java.sql";
    }

    @Override
    public Integer getVendorTypeNumber() {
        return typeCode;
    }

    @Override
    public String getQualifier() {
        return name();
    }
}
