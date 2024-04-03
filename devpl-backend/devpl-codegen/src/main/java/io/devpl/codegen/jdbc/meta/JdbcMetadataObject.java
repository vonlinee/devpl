package io.devpl.codegen.jdbc.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface JdbcMetadataObject {

    void initialize(ResultSet resultSet) throws SQLException;
}
