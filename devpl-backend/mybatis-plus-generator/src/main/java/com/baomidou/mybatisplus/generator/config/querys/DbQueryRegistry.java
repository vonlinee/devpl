package com.baomidou.mybatisplus.generator.config.querys;

import com.baomidou.mybatisplus.generator.config.DatabaseDialect;
import com.baomidou.mybatisplus.generator.jdbc.DBType;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author nieqiuqiu
 * @since 3.3.1
 */
public class DbQueryRegistry {

    private static final Map<DBType, DatabaseDialect> db_query_enum_map = new EnumMap<>(DBType.class);

    static {
        db_query_enum_map.put(DBType.ORACLE, new OracleQuery());
        db_query_enum_map.put(DBType.SQL_SERVER, new SqlServerQuery());
        db_query_enum_map.put(DBType.POSTGRE_SQL, new PostgreSqlQuery());
        db_query_enum_map.put(DBType.DB2, new DB2Query());
        db_query_enum_map.put(DBType.MARIADB, new MariadbQuery());
        db_query_enum_map.put(DBType.H2, new H2Query());
        db_query_enum_map.put(DBType.SQLITE, new SqliteQuery());
        db_query_enum_map.put(DBType.DM, new DMQuery());
        db_query_enum_map.put(DBType.KINGBASE_ES, new KingbaseESQuery());
        db_query_enum_map.put(DBType.MYSQL, new MySqlQuery());
        db_query_enum_map.put(DBType.GAUSS, new GaussQuery());
        db_query_enum_map.put(DBType.OSCAR, new OscarQuery());
        db_query_enum_map.put(DBType.FIREBIRD, new FirebirdQuery());
        db_query_enum_map.put(DBType.XU_GU, new XuguQuery());
        db_query_enum_map.put(DBType.CLICK_HOUSE, new ClickHouseQuery());
        db_query_enum_map.put(DBType.GBASE, new GbaseQuery());
        db_query_enum_map.put(DBType.SYBASE, new SybaseQuery());
    }

    public static DatabaseDialect getDbQuery(DBType dBType) {
        return db_query_enum_map.get(dBType);
    }
}
