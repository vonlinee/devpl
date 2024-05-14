package org.apache.ddlutils.platform.oracle;

import java.sql.Types;

/**
 * The platform for Oracle 9.
 */
public class Oracle9Platform extends Oracle8Platform {
    /**
     * Database name of this platform.
     */
    public static final String DATABASENAME = "Oracle9";

    /**
     * Creates a new platform instance.
     */
    public Oracle9Platform() {
        super();
        getPlatformInfo().addNativeTypeMapping(Types.TIMESTAMP, "TIMESTAMP");
    }

    @Override
    public String getName() {
        return DATABASENAME;
    }
}
