package io.devpl.codegen.generator.config;

import io.devpl.codegen.util.Messages;
import io.devpl.codegen.util.StringUtils;

import java.util.List;

/**
 * This class specifies that a key is auto-generated, either as an identity
 * column (post insert), or as some other query like a sequences (pre insert).
 */
public class GeneratedKey {

    private final String column;

    private final String runtimeSqlStatement;

    private final boolean isIdentity;

    private final String type;

    public GeneratedKey(String column, String configuredSqlStatement,
                        boolean isIdentity, String type) {
        super();
        this.column = column;
        this.type = type;
        this.isIdentity = isIdentity;

        DatabaseDialects dialect = DatabaseDialects
            .getDatabaseDialect(configuredSqlStatement);
        if (dialect == null) {
            this.runtimeSqlStatement = configuredSqlStatement;
        } else {
            this.runtimeSqlStatement = dialect.getIdentityRetrievalStatement();
        }
    }

    public String getColumn() {
        return column;
    }

    public boolean isIdentity() {
        return isIdentity;
    }

    public String getRuntimeSqlStatement() {
        return runtimeSqlStatement;
    }

    public String getMyBatis3Order() {
        return isIdentity ? "AFTER" : "BEFORE";  //$NON-NLS-2$
    }

    public void validate(List<String> errors, String tableName) {
        if (!StringUtils.hasText(runtimeSqlStatement)) {
            errors.add(Messages.getString("ValidationError.7",
                tableName));
        }

        if (StringUtils.hasText(type)
            && !"pre".equals(type)
            && !"post".equals(type)) {  //$NON-NLS-2$
            errors.add(Messages.getString("ValidationError.15", tableName));
        }

        if ("pre".equals(type) && isIdentity) {
            errors.add(Messages.getString("ValidationError.23",
                tableName));
        }

        if ("post".equals(type) && !isIdentity) {
            errors.add(Messages.getString("ValidationError.24",
                tableName));
        }
    }

    public boolean isJdbcStandard() {
        return "JDBC".equals(runtimeSqlStatement);
    }
}
