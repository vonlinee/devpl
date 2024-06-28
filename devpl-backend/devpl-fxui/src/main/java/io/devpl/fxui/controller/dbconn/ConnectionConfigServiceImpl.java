package io.devpl.fxui.controller.dbconn;

import io.devpl.fxui.common.ExceptionDialog;
import io.devpl.fxui.model.ConnectionConfig;
import io.devpl.fxui.utils.AppConfig;
import io.devpl.fxui.utils.DBUtils;
import io.devpl.sdk.util.IdUtils;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

/**
 * 连接配置 Service
 */
public class ConnectionConfigServiceImpl implements ConnectionConfigService {

    @Override
    public List<ConnectionConfig> listAll() {
        String sql = "select * from rdbms_connection_info";
        try (Connection connection = AppConfig.getConnection()) {
            return DBUtils.queryBeanList(connection, sql, ConnectionConfig.class);
        } catch (Exception exception) {
            ExceptionDialog.show(exception);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean save(ConnectionConfig connectionConfig) {
        String sql = "insert into rdbms_connection_info values ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')";
        sql = sql.formatted(IdUtils.simple32UUID(), connectionConfig.getConnectionName(), connectionConfig.getConnectionUrl(), connectionConfig.getHost(), connectionConfig.getPort(), connectionConfig.getDbType()
            , connectionConfig.getDbName(), connectionConfig.getUsername(), connectionConfig.getPassword(), connectionConfig.getEncoding());
        try (Connection connection = AppConfig.getConnection()) {
            return DBUtils.insert(connection, sql) > 0;
        } catch (Exception exception) {
            ExceptionDialog.show(exception);
        }
        return false;
    }

    @Override
    public boolean updateById(ConnectionConfig connectionConfig) {
        return false;
    }
}
