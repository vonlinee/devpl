package org.apache.ddlutils.platform.derby;


import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.alteration.AddColumnChange;
import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.alteration.TableDefinitionChangesPredicate;
import org.apache.ddlutils.model.CascadeActionEnum;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.DBTypeEnum;
import org.apache.ddlutils.platform.DefaultTableDefinitionChangesPredicate;
import org.apache.ddlutils.platform.JDBCDriverTypeEnum;
import org.apache.ddlutils.platform.cloudscape.CloudscapePlatform;
import org.apache.ddlutils.util.PojoMap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

/**
 * The platform implementation for Derby.
 */
public class DerbyPlatform extends CloudscapePlatform {

    /**
     * Creates a new Derby platform instance.
     */
    public DerbyPlatform() {
        super();

        PlatformInfo info = getPlatformInfo();

        info.addNativeTypeMapping(Types.DOUBLE, "DOUBLE");
        info.addNativeTypeMapping(Types.FLOAT, "DOUBLE", Types.DOUBLE);
        info.setSupportedOnUpdateActions(new CascadeActionEnum[]{CascadeActionEnum.NONE, CascadeActionEnum.RESTRICT});
        info.setDefaultOnUpdateAction(CascadeActionEnum.NONE);
        info.addEquivalentOnUpdateActions(CascadeActionEnum.NONE, CascadeActionEnum.RESTRICT);
        info.setSupportedOnDeleteActions(new CascadeActionEnum[]{CascadeActionEnum.NONE, CascadeActionEnum.RESTRICT,
            CascadeActionEnum.CASCADE, CascadeActionEnum.SET_NULL});
        info.setDefaultOnDeleteAction(CascadeActionEnum.NONE);

        setSqlBuilder(new DerbyBuilder(this));
        setModelReader(new DerbyModelReader(this));
    }

    @Override
    public String getName() {
        return DBTypeEnum.DERBY.getName();
    }

    @Override
    public void createDatabase(String jdbcDriverClassName, String connectionUrl, String username, String password, PojoMap parameters) throws DatabaseOperationException, UnsupportedOperationException {
        // For Derby, you create databases by simply appending ";create=true" to the connection url
        if (JDBCDriverTypeEnum.DERBY.getDriverClassName().equals(jdbcDriverClassName) ||
            JDBCDriverTypeEnum.DERBY_EMBED.getDriverClassName().equals(jdbcDriverClassName)) {
            StringBuilder creationUrl = new StringBuilder();
            Connection connection = null;

            creationUrl.append(connectionUrl);
            creationUrl.append(";create=true");
            if ((parameters != null) && !parameters.isEmpty()) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    // no need to specify create twice (and create=false wouldn't help anyway)
                    if (!"create".equalsIgnoreCase(entry.getKey())) {
                        creationUrl.append(";");
                        creationUrl.append(entry.getKey());
                        creationUrl.append("=");
                        if (entry.getValue() != null) {
                            creationUrl.append(entry.getValue());
                        }
                    }
                }
            }
            if (getLog().isDebugEnabled()) {
                getLog().debug("About to create database using this URL: " + creationUrl);
            }
            try {
                Class.forName(jdbcDriverClassName);

                connection = DriverManager.getConnection(creationUrl.toString(), username, password);
                logWarnings(connection);
            } catch (Exception ex) {
                throw new DatabaseOperationException("Error while trying to create a database", ex);
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ignored) {
                    }
                }
            }
        } else {
            throw new UnsupportedOperationException("Unable to create a Derby database via the driver " + jdbcDriverClassName);
        }
    }

    @Override
    protected TableDefinitionChangesPredicate getTableDefinitionChangesPredicate() {
        return new DefaultTableDefinitionChangesPredicate() {
            @Override
            protected boolean isSupported(Table intermediateTable, TableChange change) {
                // Derby cannot add IDENTITY columns
                if ((change instanceof AddColumnChange) &&
                    ((AddColumnChange) change).getNewColumn().isAutoIncrement()) {
                    return false;
                } else {
                    return super.isSupported(intermediateTable, change);
                }
            }
        };
    }
}
