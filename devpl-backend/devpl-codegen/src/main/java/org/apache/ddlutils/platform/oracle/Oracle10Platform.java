package org.apache.ddlutils.platform.oracle;


/**
 * The platform for Oracle 10.
 *
 * @version $Revision: 231306 $
 */
public class Oracle10Platform extends Oracle9Platform {
    /**
     * Database name of this platform.
     */
    public static final String DATABASENAME = "Oracle10";

    /**
     * Creates a new platform instance.
     */
    public Oracle10Platform() {
        super();
        setSqlBuilder(new Oracle10Builder(this));
        setModelReader(new Oracle10ModelReader(this));
    }

    @Override
    public String getName() {
        return DATABASENAME;
    }
}
