package io.devpl.codegen.jdbc;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

public abstract class ConnectionHolder {

    @Nullable
    private Connection connection;

    private Supplier<Connection> connectionSupplier;

    @Nullable
    public final Connection getConnection() {
        return connection;
    }

    public final Connection getUsableConnection() throws SQLException {
        if (connection == null) {
            if (connectionSupplier == null) {
                throw new SQLException("cannot get connection cause connection is null and no supplier");
            }
            return connectionSupplier.get();
        } else if (connection.isClosed()) {
            if (connectionSupplier == null) {
                throw new SQLException("cannot get connection cause connection is closed and no supplier");
            }
            return connectionSupplier.get();
        }
        return connection;
    }

    public final void setConnectionSupplier(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
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
