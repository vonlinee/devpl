package org.apache.ddlutils.alteration;

import org.apache.ddlutils.model.Database;
import org.junit.jupiter.api.Assertions;

import java.sql.Types;
import java.util.List;

/**
 * Tests the model comparison of primary keys.
 */
public class TestPrimaryKeyComparison extends TestComparisonBase {
    /**
     * Tests the addition of a column that is the primary key.
     */
    public void testAddPrimaryKeyColumn() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK2' type='INTEGER' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(true).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        AddColumnChange colChange = (AddColumnChange) changes.get(0);
        AddPrimaryKeyChange pkChange = (AddPrimaryKeyChange) changes.get(1);

        Assertions.assertEquals("TableA", colChange.getChangedTableName());
        assertColumn("ColPK1", Types.INTEGER, null, null, false, true, false, colChange.getNewColumn());
        Assertions.assertNull(colChange.getPreviousColumn());
        Assertions.assertEquals("ColPK2", colChange.getNextColumn());

        Assertions.assertEquals("TableA", pkChange.getChangedTableName());
        Assertions.assertEquals(1, pkChange.getPrimaryKeyColumns().length);
        Assertions.assertEquals("ColPK1", pkChange.getPrimaryKeyColumns()[0]);
    }

    /**
     * Tests the addition of a single-column primary key.
     */
    public void testMakeColumnPrimaryKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        AddPrimaryKeyChange change = (AddPrimaryKeyChange) changes.get(0);

        Assertions.assertEquals("TableA", change.getChangedTableName());
        Assertions.assertEquals(1, change.getPrimaryKeyColumns().length);
        Assertions.assertEquals("ColPK", change.getPrimaryKeyColumns()[0]);
    }

    /**
     * Tests the addition of a column to the primary key.
     */
    public void testAddColumnToPrimaryKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(true).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        PrimaryKeyChange change = (PrimaryKeyChange) changes.get(0);

        Assertions.assertEquals("TableA", change.getChangedTableName());
        Assertions.assertEquals(2, change.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("ColPK1", change.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("ColPK2", change.getNewPrimaryKeyColumns()[1]);
    }

    /**
     * Tests changing the order of columns in the primary key.
     */
    public void testChangeColumnOrderInPrimaryKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK3' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK3' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(true).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        PrimaryKeyChange pkChange = (PrimaryKeyChange) changes.get(0);
        ColumnOrderChange colChange = (ColumnOrderChange) changes.get(1);

        Assertions.assertEquals("TableA", pkChange.getChangedTableName());
        Assertions.assertEquals(3, pkChange.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("ColPK2", pkChange.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("ColPK3", pkChange.getNewPrimaryKeyColumns()[1]);
        Assertions.assertEquals("ColPK1", pkChange.getNewPrimaryKeyColumns()[2]);

        Assertions.assertEquals("TableA", colChange.getChangedTableName());
        Assertions.assertEquals(2, colChange.getNewPosition("ColPK1", true));
        Assertions.assertEquals(0, colChange.getNewPosition("ColPK2", true));
        Assertions.assertEquals(1, colChange.getNewPosition("ColPK3", true));
    }

    /**
     * Tests adding a column to and changing the order of columns in the primary key.
     */
    public void testAddColumnAndChangeColumnOrderInPrimaryKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK3' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK3' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(true).getChanges(model1, model2);

        Assertions.assertEquals(4, changes.size());

        PrimaryKeyChange pkChange1 = (PrimaryKeyChange) changes.get(0);
        ColumnOrderChange colChange1 = (ColumnOrderChange) changes.get(1);
        AddColumnChange colChange2 = (AddColumnChange) changes.get(2);
        PrimaryKeyChange pkChange2 = (PrimaryKeyChange) changes.get(3);

        Assertions.assertEquals("TableA", pkChange1.getChangedTableName());
        Assertions.assertEquals(2, pkChange1.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("ColPK3", pkChange1.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("ColPK1", pkChange1.getNewPrimaryKeyColumns()[1]);

        Assertions.assertEquals("TableA", colChange1.getChangedTableName());
        Assertions.assertEquals(1, colChange1.getNewPosition("ColPK1", true));
        Assertions.assertEquals(-1, colChange1.getNewPosition("ColPK2", true));
        Assertions.assertEquals(0, colChange1.getNewPosition("ColPK3", true));

        Assertions.assertEquals("TableA", colChange2.getChangedTableName());
        assertColumn("ColPK2", Types.INTEGER, null, null, false, true, false, colChange2.getNewColumn());
        Assertions.assertNull(colChange2.getPreviousColumn());
        Assertions.assertEquals("ColPK3", colChange2.getNextColumn());

        Assertions.assertEquals("TableA", pkChange2.getChangedTableName());
        Assertions.assertEquals(3, pkChange2.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("ColPK2", pkChange2.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("ColPK3", pkChange2.getNewPrimaryKeyColumns()[1]);
        Assertions.assertEquals("ColPK1", pkChange2.getNewPrimaryKeyColumns()[2]);
    }

    /**
     * Tests removing a column from and changing the order of columns in the primary key.
     */
    public void testRemoveColumnAndChangeColumnOrderInPrimaryKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK3' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK3' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(true).getChanges(model1, model2);

        Assertions.assertEquals(3, changes.size());

        RemoveColumnChange colChange1 = (RemoveColumnChange) changes.get(0);
        PrimaryKeyChange pkChange = (PrimaryKeyChange) changes.get(1);
        ColumnOrderChange colChange2 = (ColumnOrderChange) changes.get(2);

        Assertions.assertEquals("TableA", colChange1.getChangedTableName());
        Assertions.assertEquals("ColPK2", colChange1.getChangedColumn());

        Assertions.assertEquals("TableA", pkChange.getChangedTableName());
        Assertions.assertEquals(2, pkChange.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("ColPK3", pkChange.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("ColPK1", pkChange.getNewPrimaryKeyColumns()[1]);

        Assertions.assertEquals("TableA", colChange2.getChangedTableName());
        Assertions.assertEquals(1, colChange2.getNewPosition("ColPK1", true));
        Assertions.assertEquals(-1, colChange2.getNewPosition("ColPK2", true));
        Assertions.assertEquals(0, colChange2.getNewPosition("ColPK3", true));
    }

    // TODO: remove, add & reorder PK columns

    /**
     * Tests the removal of a column from the primary key.
     */
    public void testMakeColumnNotPrimaryKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK1' type='INTEGER' required='true'/>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        PrimaryKeyChange change = (PrimaryKeyChange) changes.get(0);

        Assertions.assertEquals("TableA", change.getChangedTableName());
        Assertions.assertEquals(1, change.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("ColPK2", change.getNewPrimaryKeyColumns()[0]);
    }


    /**
     * Tests removing the column that is the primary key.
     */
    public void testDropPrimaryKeyColumn1() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Col' type='INTEGER'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COL' type='INTEGER'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        RemoveColumnChange colChange = (RemoveColumnChange) changes.get(0);

        Assertions.assertEquals("TableA", colChange.getChangedTableName());
        Assertions.assertEquals("ColPK", colChange.getChangedColumn());
    }

    /**
     * Tests dropping a column that is part of the primary key.
     */
    public void testDropPrimaryKeyColumn2() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK3' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK3' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(true).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        RemoveColumnChange colChange = (RemoveColumnChange) changes.get(0);

        Assertions.assertEquals("TableA", colChange.getChangedTableName());
        Assertions.assertEquals("ColPK2", colChange.getChangedColumn());
    }

    /**
     * Tests the removal of a primary key.
     */
    public void testRemovePrimaryKey1() {
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
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        RemovePrimaryKeyChange change = (RemovePrimaryKeyChange) changes.get(0);

        Assertions.assertEquals("TableA", change.getChangedTableName());
    }

    /**
     * Tests removing a multi-column primary key.
     */
    public void testRemovePrimaryKey2() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK3' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK1' type='INTEGER' required='true'/>
                <column name='COLPK2' type='INTEGER' required='true'/>
                <column name='COLPK3' type='INTEGER' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        RemovePrimaryKeyChange pkChange = (RemovePrimaryKeyChange) changes.get(0);

        Assertions.assertEquals("TableA", pkChange.getChangedTableName());
    }

    /**
     * Tests changing the columns of a primary key.
     */
    public void testChangePrimaryKeyColumns() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' required='true'/>
                <column name='ColPK3' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK4' type='INTEGER' required='true'/>
                <column name='ColPK5' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='ColPK1' type='INTEGER' required='true'/>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK3' type='INTEGER' required='true'/>
                <column name='ColPK4' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK5' type='INTEGER' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        PrimaryKeyChange pkChange = (PrimaryKeyChange) changes.get(0);

        Assertions.assertEquals("TableA", pkChange.getChangedTableName());
        Assertions.assertEquals(2, pkChange.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("ColPK2", pkChange.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("ColPK4", pkChange.getNewPrimaryKeyColumns()[1]);
    }
}
