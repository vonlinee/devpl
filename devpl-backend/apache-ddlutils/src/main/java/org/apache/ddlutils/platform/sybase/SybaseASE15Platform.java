package org.apache.ddlutils.platform.sybase;


import org.apache.ddlutils.platform.BuiltinDatabaseType;

/**
 * The platform implementation for Sybase ASE 15 and above.
 *
 * @version $Revision:  $
 */
public class SybaseASE15Platform extends SybasePlatform {

    /**
     * Creates a new platform instance.
     */
    public SybaseASE15Platform() {
        super();
    }

    @Override
    public String getName() {
        return BuiltinDatabaseType.SYBASE_ASE15.getName();
    }
}
