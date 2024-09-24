package org.apache.ddlutils.platform.mysql;

import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.DBType;
import org.apache.ddlutils.platform.BuiltinDBType;

/**
 * The platform implementation for MySQL 5 and above.
 */
public class MySql5xPlatform extends MySqlPlatform {

    /**
     * Creates a new platform instance.
     */
    public MySql5xPlatform() {
        super();
        PlatformInfo info = getPlatformInfo();

        // MySql 5.0 returns an empty string for default values for pk columns
        // which is different from the MySql 4 behaviour
        info.setSyntheticDefaultValueForRequiredReturned(false);

        setSqlBuilder(new MySql5xBuilder(this));
        setModelReader(new MySql5xModelReader(this));

        setDelimitedIdentifierModeOn(true);
    }

    @Override
    public DBType getDBType() {
        return BuiltinDBType.MYSQL;
    }
}
