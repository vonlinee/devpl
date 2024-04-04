package io.devpl.codegen.db.query;

import io.devpl.codegen.db.DBType;

import java.util.EnumMap;

public class DbQueryRegistry {

    private static final EnumMap<DBType, AbstractDbQuery> registry = new EnumMap<>(DBType.class);

    static {
        registry.put(DBType.ORACLE, new OracleQuery());
        registry.put(DBType.SQL_SERVER, new SqlServerQuery());
        registry.put(DBType.POSTGRE_SQL, new PostgresSqlQuery());
        registry.put(DBType.DB2, new DB2Query());
        registry.put(DBType.MARIADB, new MariadbQuery());
        registry.put(DBType.H2, new H2Query());
        registry.put(DBType.SQLITE, new SqliteQuery());
        registry.put(DBType.DM, new DMQuery());
        registry.put(DBType.KINGBASE_ES, new KingbaseESQuery());
        registry.put(DBType.MYSQL, new MySqlQuery());
        registry.put(DBType.GAUSS, new GaussQuery());
        registry.put(DBType.OSCAR, new OscarQuery());
        registry.put(DBType.FIREBIRD, new FirebirdQuery());
        registry.put(DBType.XU_GU, new XuguQuery());
        registry.put(DBType.CLICK_HOUSE, new ClickHouseQuery());
        registry.put(DBType.GBASE, new GbaseQuery());
        registry.put(DBType.SYBASE, new SybaseQuery());
    }

    public static AbstractDbQuery getDbQuery(DBType dBType) {
        return registry.get(dBType);
    }
}
