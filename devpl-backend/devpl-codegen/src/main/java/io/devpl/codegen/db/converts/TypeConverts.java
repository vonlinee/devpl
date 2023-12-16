package io.devpl.codegen.db.converts;

import io.devpl.codegen.config.ITypeConvert;
import io.devpl.codegen.util.select.BranchBuilder;
import io.devpl.codegen.util.select.Selector;
import io.devpl.codegen.db.ColumnJavaType;
import io.devpl.codegen.db.DBType;

/**
 * 该注册器负责注册并查询类型注册器
 */
public class TypeConverts {

    /**
     * 查询数据库类型对应的类型转换器
     *
     * @param dbType 数据库类型
     * @return 返回转换器
     */
    public static ITypeConvert getTypeConvert(DBType dbType) {
        return switch (dbType) {
            case ORACLE -> OracleTypeConvert.INSTANCE;
            case DB2 -> DB2TypeConvert.INSTANCE;
            case DM, GAUSS -> DmTypeConvert.INSTANCE;
            case KINGBASE_ES -> KingbaseESTypeConvert.INSTANCE;
            case OSCAR -> OscarTypeConvert.INSTANCE;
            case MYSQL, MARIADB -> MySqlTypeConvert.INSTANCE;
            case POSTGRE_SQL -> PostgreSqlTypeConvert.INSTANCE;
            case SQLITE -> SqliteTypeConvert.INSTANCE;
            case SQL_SERVER -> SqlServerTypeConvert.INSTANCE;
            case FIREBIRD -> FirebirdTypeConvert.INSTANCE;
            case CLICK_HOUSE -> ClickHouseTypeConvert.INSTANCE;
            default -> null;
        };
    }

    /**
     * 使用指定参数构建一个选择器
     *
     * @param param 参数
     * @return 返回选择器
     */
    static Selector<String, ColumnJavaType> use(String param) {
        return new Selector<>(param.toLowerCase());
    }

    /**
     * 这个分支构建器用于构建用于支持 {@link String#contains(CharSequence)} 的分支
     *
     * @param value 分支的值
     * @return 返回分支构建器
     * @see #containsAny(CharSequence...)
     */
    static BranchBuilder<String, ColumnJavaType> contains(CharSequence value) {
        return BranchBuilder.of(s -> s.contains(value));
    }

    /**
     * @see #contains(CharSequence)
     */
    static BranchBuilder<String, ColumnJavaType> containsAny(CharSequence... values) {
        return BranchBuilder.of(s -> {
            for (CharSequence value : values) {
                if (s.contains(value)) {
                    return true;
                }
            }
            return false;
        });
    }
}
