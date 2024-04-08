package org.apache.ddlutils.platform.sybase;


import org.apache.ddlutils.platform.DBTypeEnum;

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
        return DBTypeEnum.SYBASE_ASE15.getName();
    }
}
