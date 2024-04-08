package jooq;

import org.jooq.DSLContext;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public class JooqSqlBuilderTest {

//    static Stream<Arguments> dslContexts() {
//        return Stream.of(
//            Arguments.of(SQLDialect.DEFAULT),
//            Arguments.of(SQLDialect.H2),
//            Arguments.of(SQLDialect.HSQLDB),
//            Arguments.of(SQLDialect.POSTGRES),
//            Arguments.of(SQLDialect.MYSQL)
//        );
//    }
//    @ParameterizedTest
//    @MethodSource("dslContexts")
//    void test_buildCreateTable(final SQLDialect dialect) {
//        final DSLContext dsl = DSL.using(dialect);
//        final String sql = dsl
//            .createTable("user")
//            .column("id",
//                DefaultDataType.getDataType(dialect, Long.class)
//                    .identity(true)
//                    .nullable(false))
//            .column("name",
//                DefaultDataType.getDataType(dialect, String.class)
//                    .nullable(false)
//                    .length(100))
//            .column("created_at",
//                DefaultDataType.getDataType(dialect, LocalDateTime.class)
//                    .nullable(false))
//            .constraint(DSL.primaryKey("id"))
//            .getSQL(ParamType.INLINED);
//        System.out.println(dialect.getName() + " => " + sql);
//
//    }
}
