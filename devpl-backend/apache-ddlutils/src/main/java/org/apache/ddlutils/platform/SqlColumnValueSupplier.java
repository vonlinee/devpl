package org.apache.ddlutils.platform;

import org.apache.ddlutils.model.Column;

public interface SqlColumnValueSupplier {

    /**
     * column value in sql, literal value
     *
     * @param column column
     * @return column value expression
     */
    String getColumnValue(Column column);
}
