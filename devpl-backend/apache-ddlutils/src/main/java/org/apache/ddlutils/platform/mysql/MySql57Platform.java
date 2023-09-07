package org.apache.ddlutils.platform.mysql;

import org.apache.ddlutils.PlatformInfo;

public class MySql57Platform extends MySqlPlatform {

    /**
     * Database name of this platform.
     */
    public static final String DATABASENAME = "MySQL57";

    /**
     * Creates a new platform instance.
     */
    public MySql57Platform() {
        super();

        PlatformInfo info = getPlatformInfo();

        // MySql 5.0 returns an empty string for default values for pk columns
        // which is different from the MySql 4 behaviour
        info.setSyntheticDefaultValueForRequiredReturned(false);

        setSqlBuilder(new MySql50Builder(this));
        setModelReader(new MySql50ModelReader(this));
    }

    @Override
    public String getName() {
        return DATABASENAME;
    }
}
