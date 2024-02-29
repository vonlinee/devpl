package io.devpl.backend.tools.ddl;

public class ConvertBean {
    private String sqlType;
    private String sqlTypeLength;

    public ConvertBean(String sqlType, String sqlTypeLength) {
        this.sqlType = sqlType;
        this.sqlTypeLength = sqlTypeLength;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getSqlTypeLength() {
        return sqlTypeLength;
    }

    public void setSqlTypeLength(String sqlTypeLength) {
        this.sqlTypeLength = sqlTypeLength;
    }
}
