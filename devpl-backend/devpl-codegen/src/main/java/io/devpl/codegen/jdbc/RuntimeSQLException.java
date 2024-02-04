package io.devpl.codegen.jdbc;

import java.io.Serial;
import java.sql.SQLException;

/**
 * 包装SQLException为RuntimeException
 *
 * @see SQLException
 */
public class RuntimeSQLException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5224696788505678598L;

    public RuntimeSQLException(SQLException sqlException) {
        super(sqlException);
    }
}
