package io.devpl.codegen.jdbc;

import java.sql.Connection;

public abstract class ConnectionHolder {

    protected Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
