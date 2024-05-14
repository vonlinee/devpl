package io.devpl.codegen.jdbc;

import io.devpl.codegen.db.DBTypeEnum;
import io.devpl.codegen.db.query.AbstractQueryDatabaseMetadataReader;
import io.devpl.codegen.generator.config.JdbcConfiguration;
import org.apache.ddlutils.jdbc.meta.ColumnMetadata;
import org.apache.ddlutils.jdbc.meta.DatabaseMetadataReader;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class JdbcDatabaseMetadataReaderTest {

    public Connection getConnection() throws IOException {
        String res = """
            driver-class-name=com.mysql.jdbc.Driver
            url=jdbc:mysql://localhost:3306?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true&useAffectedRows=true&allowMultiQueries=true
            username=root
            password=123456
            """;

        Properties properties = new Properties();
        properties.load(new StringReader(res));

        JdbcConfiguration dsc = JdbcConfiguration.builder(properties).build();
        return dsc.getConnection();
    }

    @Test
    public void test1() throws IOException {
        try (Connection connection = getConnection()) {

            DatabaseMetadataReader loader = AbstractQueryDatabaseMetadataReader.getQuery(DBTypeEnum.MYSQL);
            loader.setConnection(connection);

            List<ColumnMetadata> columns = loader.getColumns(null, "devpl", "data_type_item", null);

            System.out.println(columns);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test2() throws IOException {
        try (Connection connection = getConnection()) {
            DatabaseMetadataReader loader = AbstractQueryDatabaseMetadataReader.getQuery(DBTypeEnum.MYSQL);
            loader.setConnection(connection);
            List<String> columns = loader.getDataTypes(null, null);

            System.out.println(columns);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
