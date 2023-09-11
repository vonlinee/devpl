package org.apache.ddlutils.platform.maxdb;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;
import org.apache.ddlutils.util.ObjectMap;

import java.sql.SQLException;
import java.sql.Types;

/**
 * Reads a database model from a MaxDb database.
 * @version $Revision: $
 */
public class MaxDbModelReader extends JdbcModelReader {
    /**
     * Creates a new model reader for MaxDb databases.
     * @param platform The platform that this model reader belongs to
     */
    public MaxDbModelReader(DatabasePlatform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
        setDefaultTablePattern("%");
    }

    @Override
    protected Column readColumn(DatabaseMetaDataWrapper metaData, ObjectMap values) throws SQLException {
        Column column = super.readColumn(metaData, values);

        if (column.getDefaultValue() != null) {
            // SapDb pads the default value with spaces
            column.setDefaultValue(column.getDefaultValue().trim());
            // SapDb uses the default value for the auto-increment specification
            if (column.getDefaultValue().startsWith("DEFAULT SERIAL")) {
                column.setAutoIncrement(true);
                column.setDefaultValue(null);
            }
        }
        if (column.getJdbcTypeCode() == Types.DECIMAL) {
            // need to use COLUMN_SIZE for precision instead of NUM_PREC_RADIX
            column.setPrecisionRadix(column.getSizeAsInt());

            // We also perform back-mapping to BIGINT
            if ((column.getSizeAsInt() == 38) && (column.getScale() == 0)) {
                column.setJdbcTypeCode(Types.BIGINT);
            }
        }
        return column;
    }
}