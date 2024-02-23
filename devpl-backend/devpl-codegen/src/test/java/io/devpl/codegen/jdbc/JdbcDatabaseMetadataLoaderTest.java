package io.devpl.codegen.jdbc;

import io.devpl.codegen.config.DataSourceConfig;
import io.devpl.codegen.db.DBType;
import io.devpl.codegen.db.query.AbstractQueryDatabaseMetadataLoader;
import io.devpl.codegen.jdbc.meta.ColumnMetadata;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class JdbcDatabaseMetadataLoaderTest {

    @Test
    public void test1() throws IOException {

        String res = """
            driver-class-name=com.mysql.jdbc.Driver
            url=jdbc:mysql://localhost:3306?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true&useAffectedRows=true&allowMultiQueries=true
            username=root
            password=123456
            """;

        Properties properties = new Properties();
        properties.load(new StringReader(res));

        DataSourceConfig dsc = DataSourceConfig.builder(properties).build();

        try (Connection connection = dsc.getConnection()){

            AbstractQueryDatabaseMetadataLoader loader = new AbstractQueryDatabaseMetadataLoader(connection, DBType.MYSQL);

            List<ColumnMetadata> columns = loader.getColumns(null, "devpl", null, null);

            System.out.println(columns);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
