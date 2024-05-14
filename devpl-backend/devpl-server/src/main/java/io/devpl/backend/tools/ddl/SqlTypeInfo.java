package io.devpl.backend.tools.ddl;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SqlTypeInfo {
    private String sqlType;
    private String sqlTypeLength;

    public SqlTypeInfo(String sqlType, String sqlTypeLength) {
        this.sqlType = sqlType;
        this.sqlTypeLength = sqlTypeLength;
    }

    public String getSqlType() {
        return sqlType;
    }

    public String getSqlTypeLength() {
        return sqlTypeLength;
    }

}
