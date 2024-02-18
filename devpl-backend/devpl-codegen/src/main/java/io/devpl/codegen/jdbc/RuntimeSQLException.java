package io.devpl.codegen.jdbc;

import java.sql.SQLException;

/**
 * 包装SQLException为RuntimeException
 *
 * @see SQLException
 */
public class RuntimeSQLException extends RuntimeException {

    public RuntimeSQLException(SQLException sqlException) {
        super(sqlException);
    }
}
