package io.devpl.codegen.db.query;

import io.devpl.codegen.db.DBTypeEnum;

import java.util.EnumMap;

public class DbQueryRegistry {

    private static final EnumMap<DBTypeEnum, AbstractDbQuery> registry = new EnumMap<>(DBTypeEnum.class);

    static {
        registry.put(DBTypeEnum.ORACLE, new OracleQuery());
        registry.put(DBTypeEnum.SQL_SERVER, new SqlServerQuery());
        registry.put(DBTypeEnum.POSTGRE_SQL, new PostgresSqlQuery());
        registry.put(DBTypeEnum.DB2, new DB2Query());
        registry.put(DBTypeEnum.MARIADB, new MariadbQuery());
        registry.put(DBTypeEnum.H2, new H2Query());
        registry.put(DBTypeEnum.SQLITE, new SqliteQuery());
        registry.put(DBTypeEnum.DM, new DMQuery());
        registry.put(DBTypeEnum.KINGBASE_ES, new KingbaseESQuery());
        registry.put(DBTypeEnum.MYSQL, new MySqlQuery());
        registry.put(DBTypeEnum.GAUSS, new GaussQuery());
        registry.put(DBTypeEnum.OSCAR, new OscarQuery());
        registry.put(DBTypeEnum.FIREBIRD, new FirebirdQuery());
        registry.put(DBTypeEnum.XU_GU, new XuguQuery());
        registry.put(DBTypeEnum.CLICK_HOUSE, new ClickHouseQuery());
        registry.put(DBTypeEnum.GBASE, new GbaseQuery());
        registry.put(DBTypeEnum.SYBASE, new SybaseQuery());
    }

    public static AbstractDbQuery getDbQuery(DBTypeEnum dBType) {
        return registry.get(dBType);
    }
}
