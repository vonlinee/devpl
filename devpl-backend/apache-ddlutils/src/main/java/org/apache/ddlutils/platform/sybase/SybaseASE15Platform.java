package org.apache.ddlutils.platform.sybase;


import org.apache.ddlutils.platform.BuiltinDBType;

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
        return BuiltinDBType.SYBASE_ASE15.getName();
    }
}
