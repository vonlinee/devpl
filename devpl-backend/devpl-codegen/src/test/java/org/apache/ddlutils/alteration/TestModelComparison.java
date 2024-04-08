package org.apache.ddlutils.alteration;


import org.apache.ddlutils.model.Database;
import org.junit.jupiter.api.Assertions;

import java.sql.Types;
import java.util.List;

/**
 * Tests the comparison on the model level.
 */
public class TestModelComparison extends TestComparisonBase {
    /**
     * Tests the addition of a table.
     */
    public void testAddTable() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='TABLEB'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        AddTableChange change = (AddTableChange) changes.get(0);

        assertTable("TABLEB", null, 1, 0, 0, change.getNewTable());
        assertColumn("COLPK", Types.INTEGER, null, null, true, true, false, change.getNewTable().getColumn(0));
    }

    /**
     * Tests the addition of a table with an index.
     */
    public void testAddTableWithIndex() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='TABLEB'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLA' type='DOUBLE'/>
                <index name='TestIndex'>
                  <index-column name='COLA'/>
                </index>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        AddTableChange change = (AddTableChange) changes.get(0);

        assertTable("TABLEB", null, 2, 0, 1, change.getNewTable());
        assertColumn("COLPK", Types.INTEGER, null, null, true, true, false, change.getNewTable().getColumn(0));
        assertColumn("COLA", Types.DOUBLE, null, null, false, false, false, change.getNewTable().getColumn(1));
        assertIndex("TestIndex", false, new String[]{"COLA"}, change.getNewTable().getIndex(0));
    }

    /**
     * Tests the addition of a table and a foreign key to it.
     */
    public void testAddTableAndForeignKeyToIt() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK' type='INTEGER'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK' type='INTEGER'/>
                <foreign-key name='TESTFKB' foreignTable='TABLEB'>
                  <reference local='COLFK' foreign='COLPK'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        AddTableChange tableChange = (AddTableChange) changes.get(0);
        AddForeignKeyChange fkChange = (AddForeignKeyChange) changes.get(1);

        assertTable("TABLEB", null, 1, 0, 0, tableChange.getNewTable());
        assertColumn("COLPK", Types.INTEGER, null, null, true, true, false, tableChange.getNewTable().getColumn(0));

        Assertions.assertEquals("TABLEA", fkChange.getChangedTableName());
        assertForeignKey("TESTFKB", "TABLEB", new String[]{"COLFK"}, new String[]{"COLPK"}, fkChange.getNewForeignKey());
    }

    /**
     * Tests the addition of two tables with foreign keys to each other .
     */
    public void testAddTablesWithForeignKeys() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK' type='INTEGER'/>
                <foreign-key name='TESTFKB' foreignTable='TABLEB'>
                  <reference local='COLFK' foreign='COLPK'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK' type='INTEGER'/>
                <foreign-key name='TESTFKA' foreignTable='TABLEA'>
                  <reference local='COLFK' foreign='COLPK'/>
                </foreign-key>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(4, changes.size());

        AddTableChange tableChange1 = (AddTableChange) changes.get(0);
        AddTableChange tableChange2 = (AddTableChange) changes.get(1);
        AddForeignKeyChange fkChange1 = (AddForeignKeyChange) changes.get(2);
        AddForeignKeyChange fkChange2 = (AddForeignKeyChange) changes.get(3);

        assertTable("TABLEA", null, 2, 0, 0, tableChange1.getNewTable());
        assertColumn("COLPK", Types.INTEGER, null, null, true, true, false, tableChange1.getNewTable().getColumn(0));
        assertColumn("COLFK", Types.INTEGER, null, null, false, false, false, tableChange1.getNewTable().getColumn(1));

        assertTable("TABLEB", null, 2, 0, 0, tableChange2.getNewTable());
        assertColumn("COLPK", Types.INTEGER, null, null, true, true, false, tableChange2.getNewTable().getColumn(0));
        assertColumn("COLFK", Types.INTEGER, null, null, false, false, false, tableChange2.getNewTable().getColumn(1));

        Assertions.assertEquals("TABLEA", fkChange1.getChangedTableName());
        assertForeignKey("TESTFKB", "TABLEB", new String[]{"COLFK"}, new String[]{"COLPK"}, fkChange1.getNewForeignKey());

        Assertions.assertEquals("TABLEB", fkChange2.getChangedTableName());
        assertForeignKey("TESTFKA", "TABLEA", new String[]{"COLFK"}, new String[]{"COLPK"}, fkChange2.getNewForeignKey());
    }

    /**
     * Tests the removal of a table.
     */
    public void testDropTable() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='TableB'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEB'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        RemoveTableChange change = (RemoveTableChange) changes.get(0);

        Assertions.assertEquals("TableA", change.getChangedTableName());
    }

    /**
     * Tests the removal of a table with an index.
     */
    public void testDropTableWithIndex() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColA' type='INTEGER'/>
                <index name='TestIndex'>
                  <index-column name='ColA'/>
                </index>
              </table>
              <table name='TableB'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEB'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        RemoveTableChange change = (RemoveTableChange) changes.get(0);

        Assertions.assertEquals("TableA", change.getChangedTableName());
    }

    /**
     * Tests the removal of a table with a foreign key.
     */
    public void testDropTableWithForeignKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK' type='INTEGER'/>
                <foreign-key name='TESTFKB' foreignTable='TableB'>
                  <reference local='COLFK' foreign='COLPK'/>
                </foreign-key>
              </table>
              <table name='TableB'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEB'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        RemoveForeignKeyChange fkChange = (RemoveForeignKeyChange) changes.get(0);
        RemoveTableChange tableChange = (RemoveTableChange) changes.get(1);

        Assertions.assertEquals("TableA", fkChange.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getForeignKey(0), fkChange.findChangedForeignKey(model1, false));

        Assertions.assertEquals("TableA", tableChange.getChangedTableName());
    }

    /**
     * Tests the removal of a table and a foreign key to it.
     */
    public void testDropTableAndForeignKeyToIt() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='TableB'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK' type='INTEGER'/>
                <foreign-key name='TESTFKA' foreignTable='TableA'>
                  <reference local='COLFK' foreign='ColPK'/>
                </foreign-key>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEB'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK' type='INTEGER'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        RemoveForeignKeyChange fkChange = (RemoveForeignKeyChange) changes.get(0);
        RemoveTableChange tableChange = (RemoveTableChange) changes.get(1);

        Assertions.assertEquals("TableB", fkChange.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableB").getForeignKey(0), fkChange.findChangedForeignKey(model1, false));

        Assertions.assertEquals("TableA", tableChange.getChangedTableName());
    }

    /**
     * Tests the addition and removal of a table.
     */
    public void testAddAndDropTable() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(true).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        RemoveTableChange change1 = (RemoveTableChange) changes.get(0);
        AddTableChange change2 = (AddTableChange) changes.get(1);

        Assertions.assertEquals("TableA", change1.getChangedTableName());

        assertTable("TABLEA", null, 1, 0, 0, change2.getNewTable());
        assertColumn("COLPK", Types.INTEGER, null, null, true, true, false, change2.getNewTable().getColumn(0));
    }
}
