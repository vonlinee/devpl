package io.devpl.codegen.db.query;

import java.sql.SQLException;

/**
 * 包装SQLException为RuntimeException
 *
 * @see SQLException
 */
public class SQLRuntimeException extends RuntimeException {

    public SQLRuntimeException(SQLException sqlException) {
        super(sqlException);
    }
}
