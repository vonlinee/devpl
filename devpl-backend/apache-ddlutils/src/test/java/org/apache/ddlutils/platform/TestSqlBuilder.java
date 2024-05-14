package org.apache.ddlutils.platform;

import org.apache.ddlutils.TestBase;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test the base SqlBuilder class.
 */
public class TestSqlBuilder extends TestBase {
    /**
     * Tests the {@link SqlBuilder#getUpdateSql(Table, Map, boolean)} method.
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
        Map<String, Object> map = new HashMap<>();

        map.put("name", "ddlutils");
        map.put("id", 0);

        platform.setDelimitedIdentifierModeOn(true);

        String sql = sqlBuilder.getUpdateSql(database.getTable(0), map, false);

        assertEquals("UPDATE \"TestTable\" SET \"name\" = 'ddlutils' WHERE \"id\" = '0'", sql);
    }

    /**
     * Tests the {@link SqlBuilder#getUpdateSql(Table, Map, Map, boolean)} method.
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
        Map<String, Object> oldMap = new HashMap<>();
        Map<String, Object> newMap = new HashMap<>();

        oldMap.put("id", 0);

        newMap.put("name", "ddlutils");
        newMap.put("id", 1);

        platform.setDelimitedIdentifierModeOn(true);

        String sql = sqlBuilder.getUpdateSql(database.getTable(0), oldMap, newMap, false);

        assertEquals("UPDATE \"TestTable\" SET \"id\" = '1', \"name\" = 'ddlutils' WHERE \"id\" = '0'", sql);
    }
}
