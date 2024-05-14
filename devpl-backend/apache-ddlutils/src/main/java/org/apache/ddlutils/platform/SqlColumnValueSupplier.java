package org.apache.ddlutils.platform;

import org.apache.ddlutils.model.Column;

public interface SqlColumnValueSupplier {

    String getColumnValue(Column column);
}
