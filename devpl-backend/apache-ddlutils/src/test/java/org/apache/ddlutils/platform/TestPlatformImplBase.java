package org.apache.ddlutils.platform;

import org.apache.ddlutils.TestBase;
import org.apache.ddlutils.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the org.apache.ddlutils.PlatformImplBase (abstract) class.
 *
 * @version $Revision: 279421 $
 */
public class TestPlatformImplBase extends TestBase {
    /**
     * Test the toColumnValues method.
     */
    @Test
    public void testToColumnValues() {
        final String schema =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='ddlutils'>
                  <table name='TestTable'>
                    <column name='id' autoIncrement='true' type='INTEGER' primaryKey='true'/>
                    <column name='name' type='VARCHAR' size='15'/>
                  </table>
                </database>""";

        Database database = parseDatabaseFromString(schema);
        PlatformImplBase platform = new TestPlatform();
        Table table = database.getTable(0);
        TableModel clz = TableModel.of(table);
        TableRow db = new TableRow(TableModel.of(table));

        db.setColumnValue("name", "name");

        ResultSetRow map = platform.toColumnValues(clz.getProperties(), db);

        assertEquals("name",
            map.getColumnValue("name"));
        assertTrue(map.hasColumn("id"));
    }
}
