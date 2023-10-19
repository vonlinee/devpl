package io.devpl.fxui.controller.dbconn;

import io.devpl.sdk.util.IdUtils;
import io.devpl.fxui.common.ExceptionDialog;
import io.devpl.tookit.fxui.model.ConnectionConfig;
import io.devpl.fxui.utils.AppConfig;
import io.devpl.fxui.utils.DBUtils;

import java.sql.Connection;

/**
 * 连接配置 Service
 */
public class ConnectionConfigServiceImpl implements ConnectionConfigService {

    @Override
    public boolean save(ConnectionConfig connectionConfig) {
        String sql = "insert into connection_config values ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')";
        sql = sql.formatted(IdUtils.simpleULID(), connectionConfig.getConnectionName(), connectionConfig.getConnectionUrl(), connectionConfig.getHost(), connectionConfig.getPort(), connectionConfig.getDbType()
                , connectionConfig.getDbName(), connectionConfig.getUsername(), connectionConfig.getPassword(), connectionConfig.getEncoding());
        try (Connection connection = AppConfig.getConnection()) {
            return DBUtils.insert(connection, sql) > 0;
        } catch (Exception exception) {
            ExceptionDialog.report(exception);
        }
        return false;
    }
}
