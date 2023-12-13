package com.baomidou.mybatisplus.generator.query;

import java.sql.SQLException;

/**
 * @see SQLException
 */
public class SQLRuntimeException extends RuntimeException {

    public SQLRuntimeException(SQLException sqlException) {
        super(sqlException);
    }
}
