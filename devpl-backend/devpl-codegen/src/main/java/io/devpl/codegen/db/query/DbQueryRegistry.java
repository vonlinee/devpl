package io.devpl.codegen.db.query;

import org.apache.ddlutils.platform.BuiltinDatabaseType;
import org.apache.ddlutils.platform.DatabaseType;

import java.util.HashMap;
import java.util.Map;

public class DbQueryRegistry {

    private static final Map<DatabaseType, AbstractDbQuery> registry = new HashMap<>();

    static {
        registry.put(BuiltinDatabaseType.ORACLE, new OracleQuery());
        registry.put(BuiltinDatabaseType.SQL_SERVER, new SqlServerQuery());
        registry.put(BuiltinDatabaseType.POSTGRE_SQL, new PostgresSqlQuery());
        registry.put(BuiltinDatabaseType.DB2, new DB2Query());
        registry.put(BuiltinDatabaseType.MARIADB, new MariadbQuery());
        registry.put(BuiltinDatabaseType.H2, new H2Query());
        registry.put(BuiltinDatabaseType.SQLITE, new SqliteQuery());
        registry.put(BuiltinDatabaseType.DM, new DMQuery());
        registry.put(BuiltinDatabaseType.KINGBASE_ES, new KingbaseESQuery());
        registry.put(BuiltinDatabaseType.MYSQL, new MySqlQuery());
        registry.put(BuiltinDatabaseType.GAUSS, new GaussQuery());
        registry.put(BuiltinDatabaseType.OSCAR, new OscarQuery());
        registry.put(BuiltinDatabaseType.FIREBIRD, new FirebirdQuery());
        registry.put(BuiltinDatabaseType.XU_GU, new XuguQuery());
        registry.put(BuiltinDatabaseType.CLICK_HOUSE, new ClickHouseQuery());
        registry.put(BuiltinDatabaseType.GBASE, new GbaseQuery());
        registry.put(BuiltinDatabaseType.SYBASE, new SybaseQuery());
    }

    public static AbstractDbQuery getDbQuery(DatabaseType dBType) {
        return registry.get(dBType);
    }
}
