package org.apache.ddlutils.platform.cloudscape;

import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.GenericDatabasePlatform;

import java.sql.Types;

/**
 * The Cloudscape platform implementation.
 * @version $Revision: 231306 $
 */
public class CloudscapePlatform extends GenericDatabasePlatform {
    /**
     * Database name of this platform.
     */
    public static final String DATABASENAME = "Cloudscape";
    /**
     * A subprotocol used by the DB2 network driver.
     */
    public static final String JDBC_SUBPROTOCOL_1 = "db2j:net";
    /**
     * A subprotocol used by the DB2 network driver.
     */
    public static final String JDBC_SUBPROTOCOL_2 = "cloudscape:net";

    /**
     * Creates a new platform instance.
     */
    public CloudscapePlatform() {
        PlatformInfo info = getPlatformInfo();

        info.setMaxIdentifierLength(128);
        info.setSystemForeignKeyIndicesAlwaysNonUnique(true);
        info.setPrimaryKeyColumnAutomaticallyRequired(true);
        info.setIdentityColumnAutomaticallyRequired(true);
        info.setMultipleIdentityColumnsSupported(false);

        // BINARY and VARBINARY will also be handled by CloudscapeBuilder.getSqlType
        info.addNativeTypeMapping(Types.ARRAY, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.BINARY, "CHAR {0} FOR BIT DATA");
        info.addNativeTypeMapping(Types.BIT, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.BOOLEAN, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.DATALINK, "LONG VARCHAR FOR BIT DATA", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.DISTINCT, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.DOUBLE, "DOUBLE PRECISION");
        info.addNativeTypeMapping(Types.FLOAT, "DOUBLE PRECISION", Types.DOUBLE);
        info.addNativeTypeMapping(Types.JAVA_OBJECT, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.LONGVARBINARY, "LONG VARCHAR FOR BIT DATA");
        info.addNativeTypeMapping(Types.LONGVARCHAR, "LONG VARCHAR");
        info.addNativeTypeMapping(Types.NULL, "LONG VARCHAR FOR BIT DATA", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.OTHER, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.REF, "LONG VARCHAR FOR BIT DATA", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.STRUCT, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.TINYINT, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.VARBINARY, "VARCHAR {0} FOR BIT DATA");

        info.setDefaultSize(Types.BINARY, 254);
        info.setDefaultSize(Types.CHAR, 254);
        info.setDefaultSize(Types.VARBINARY, 254);
        info.setDefaultSize(Types.VARCHAR, 254);

        setSqlBuilder(new CloudscapeBuilder(this));
    }

    @Override
    public String getName() {
        return DATABASENAME;
    }
}
