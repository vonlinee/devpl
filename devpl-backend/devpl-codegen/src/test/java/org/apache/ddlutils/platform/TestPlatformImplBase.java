package org.apache.ddlutils.platform;


import org.apache.ddlutils.TestBase;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TableClass;
import org.apache.ddlutils.model.TableObject;

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
        TableClass clz = TableClass.newInstance(table);
        TableObject db = new TableObject(TableClass.newInstance(table));

        db.setColumnValue("name", "name");

        Map<String, Object> map = platform.toColumnValues(clz.getProperties(), db);

        assertEquals("name",
            map.get("name"));
        assertTrue(map.containsKey("id"));
    }
}
