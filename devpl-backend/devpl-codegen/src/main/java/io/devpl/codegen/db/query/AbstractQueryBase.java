package io.devpl.codegen.db.query;

import io.devpl.codegen.jdbc.ConnectionHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractQueryBase extends ConnectionHolder implements AbstractQuery {

    final <R> R query(CharSequence sql, Function<ResultSet, R> consumer) throws SQLException {
        Connection connection = getUsableConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(sql))) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return consumer.apply(resultSet);
            }
        }
    }

    final <R> R query(CharSequence sql, BiFunction<Connection, ResultSet, R> consumer) throws SQLException {
        Connection connection = getUsableConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(sql))) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return consumer.apply(connection, resultSet);
            }
        }
    }
}
