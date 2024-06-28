package org.apache.ddlutils.platform;

import org.apache.ddlutils.TestBase;
import org.apache.ddlutils.jdbc.PooledDataSourceWrapper;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.platform.mysql.MySql5xModelReader;
import org.apache.ddlutils.platform.mysql.MySql5xPlatform;
import org.apache.ddlutils.util.ContextMap;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test the base SqlBuilder class.
 */
public class TestSqlBuilder extends TestBase {
    /**
     * Tests the {@link SqlBuilder#getUpdateSql(Table, ResultSetRow, ResultSetRow, boolean)}
     */
    @Test
    public void testUpdateSql1() {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='ddlutils'>
              <table name='TestTable'>
                <column name='id' autoIncrement='true' type='INTEGER' primaryKey='true'/>
                <column name='name' type='VARCHAR' size='15'/>
              </table>
            </database>""";

        TestPlatform platform = new TestPlatform();
        SqlBuilder sqlBuilder = platform.getSqlBuilder();
        Database database = parseDatabaseFromString(modelXml);
        ResultSetRow map = new ResultSetRow();

        map.addColumn("name", "ddlutils");
        map.addColumn("id", 0);

        platform.setDelimitedIdentifierModeOn(true);

        String sql = sqlBuilder.getUpdateSql(database.getTable(0), map, false);

        assertEquals("UPDATE \"TestTable\" SET \"name\" = 'ddlutils' WHERE \"id\" = '0'", sql);
    }

    /**
     * Tests the {@link SqlBuilder#getUpdateSql(Table, ResultSetRow, ResultSetRow, boolean)}
     */
    @Test
    public void testUpdateSql2() {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='ddlutils'>
              <table name='TestTable'>
                <column name='id' autoIncrement='true' type='INTEGER' primaryKey='true'/>
                <column name='name' type='VARCHAR' size='15'/>
              </table>
            </database>""";

        TestPlatform platform = new TestPlatform();
        SqlBuilder sqlBuilder = platform.getSqlBuilder();
        Database database = parseDatabaseFromString(modelXml);
        ResultSetRow oldMap = new ResultSetRow();
        ResultSetRow newMap = new ResultSetRow();

        oldMap.addColumn("id", 0);

        newMap.addColumn("name", "ddlutils");
        newMap.addColumn("id", 1);

        platform.setDelimitedIdentifierModeOn(true);

        String sql = sqlBuilder.getUpdateSql(database.getTable(0), oldMap, newMap, false);

        assertEquals("UPDATE \"TestTable\" SET \"id\" = '1', \"name\" = 'ddlutils' WHERE \"id\" = '0'", sql);
    }

    @Test
    public void testInsertSqlWithRealTable() {
        MySql5xPlatform platform = new MySql5xPlatform();
        MySql5xModelReader reader = new MySql5xModelReader(platform);

        try (PooledDataSourceWrapper dataSource = new PooledDataSourceWrapper()) {
            dataSource.setUrl("jdbc:mysql://localhost:3306/devpl?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true");
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUsername("root");
            dataSource.setPassword("123456");

            platform.setDataSource(dataSource);

            try (Connection connection = dataSource.getConnection()) {
                Database database = reader.getDatabase(connection, "devpl");

                SqlBuilder sqlBuilder = platform.getSqlBuilder();

                System.out.println("sql builder => " + sqlBuilder.getClass());
                StringWriter stringWriter = new StringWriter();

                sqlBuilder.setWriter(stringWriter);

                Table table = database.getTable(0);

                getLog().info("table => {}", table.getName());

                ContextMap parameters = new ContextMap();
                sqlBuilder.createTable(database, table, parameters);

                TableRow row = TableModel.of(table).createRow();

                ResultSetRow columnValueMap = row.getRowData();
                columnValueMap.setColumnValue("id", "ssdd");
                String insertSql = sqlBuilder.getInsertSql(table, columnValueMap, true, true);
                System.out.println(insertSql);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
