package io.devpl.codegen.jdbc;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionHolder {

    @Nullable
    private Connection connection;

    @Nullable
    public final Connection getConnection() {
        return connection;
    }

    public final void setConnection(@Nullable Connection connection) {
        this.connection = connection;
    }

    public final boolean isClosed() throws SQLException {
        if (connection == null) {
            throw new SQLException("connection is null");
        }
        return connection.isClosed();
    }

    public void releaseConnection() {
    }
}
