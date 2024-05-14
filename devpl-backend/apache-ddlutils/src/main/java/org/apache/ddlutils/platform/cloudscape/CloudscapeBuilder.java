package org.apache.ddlutils.platform.cloudscape;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.SqlBuilder;

import java.io.IOException;

/**
 * The SQL Builder for Cloudscape.
 */
public class CloudscapeBuilder extends SqlBuilder {
    /**
     * Creates a new builder instance.
     *
     * @param platform The plaftform this builder belongs to
     */
    public CloudscapeBuilder(Platform platform) {
        super(platform);
        addEscapedCharSequence("'", "''");
    }

    @Override
    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException {
        print("GENERATED ALWAYS AS IDENTITY");
    }

    @Override
    public String getSelectLastIdentityValues(Table table) {
        return "VALUES IDENTITY_VAL_LOCAL()";
    }
}
