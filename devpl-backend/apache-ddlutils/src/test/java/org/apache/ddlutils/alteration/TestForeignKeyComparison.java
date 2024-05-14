package org.apache.ddlutils.alteration;


import org.apache.ddlutils.model.Database;
import org.junit.jupiter.api.Assertions;

import java.sql.Types;
import java.util.List;

/**
 * Tests the model comparison.
 * <p>
 * TODO: need tests with foreign key without a name
 */
public class TestForeignKeyComparison extends TestComparisonBase {
    /**
     * Tests the addition of a single-column foreign key.
     */
    public void testAddColumnAndForeignKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='TableB'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK' type='INTEGER'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
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

        AddColumnChange colChange = (AddColumnChange) changes.get(0);
        AddForeignKeyChange fkChange = (AddForeignKeyChange) changes.get(1);

        Assertions.assertEquals("TableA", colChange.getChangedTableName());
        assertColumn("COLFK", Types.INTEGER, null, null, false, false, false, colChange.getNewColumn());
        Assertions.assertEquals("ColPK", colChange.getPreviousColumn());
        Assertions.assertNull(colChange.getNextColumn());

        Assertions.assertEquals("TableA", fkChange.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"COLFK"}, new String[]{"ColPK"}, fkChange.getNewForeignKey());
    }

    /**
     * Tests the addition of a single-column foreign key.
     */
    public void testAddColumnAndForeignKeyToIt() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK' type='INTEGER'/>
              </table>
              <table name='TableB'>
                <column name='Col' type='INTEGER'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK' type='INTEGER'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
                  <reference local='COLFK' foreign='COLPK'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Col' type='INTEGER'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(3, changes.size());

        AddColumnChange colChange = (AddColumnChange) changes.get(0);
        AddPrimaryKeyChange pkChange = (AddPrimaryKeyChange) changes.get(1);
        AddForeignKeyChange fkChange = (AddForeignKeyChange) changes.get(2);

        Assertions.assertEquals("TableB", colChange.getChangedTableName());
        assertColumn("COLPK", Types.INTEGER, null, null, false, true, false, colChange.getNewColumn());
        Assertions.assertNull(colChange.getPreviousColumn());
        Assertions.assertEquals("Col", colChange.getNextColumn());

        Assertions.assertEquals("TableB", pkChange.getChangedTableName());
        Assertions.assertEquals(1, pkChange.getPrimaryKeyColumns().length);
        Assertions.assertEquals("COLPK", pkChange.getPrimaryKeyColumns()[0]);

        Assertions.assertEquals("TableA", fkChange.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"COLFK"}, new String[]{"COLPK"}, fkChange.getNewForeignKey());
    }

    /**
     * Tests the addition of a multi-column foreign key.
     */
    public void testAddColumnsAndForeignKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='TableB'>
                <column name='ColPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK1' type='INTEGER'/>
                <column name='COLFK2' type='DOUBLE'/>
                <column name='COLFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
                  <reference local='COLFK2' foreign='COLPK1'/>
                  <reference local='COLFK1' foreign='COLPK2'/>
                  <reference local='COLFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(4, changes.size());

        AddColumnChange colChange1 = (AddColumnChange) changes.get(0);
        AddColumnChange colChange2 = (AddColumnChange) changes.get(1);
        AddColumnChange colChange3 = (AddColumnChange) changes.get(2);
        AddForeignKeyChange fkChange = (AddForeignKeyChange) changes.get(3);

        Assertions.assertEquals("TableA", colChange1.getChangedTableName());
        assertColumn("COLFK1", Types.INTEGER, null, null, false, false, false, colChange1.getNewColumn());
        Assertions.assertEquals("ColPK", colChange1.getPreviousColumn());
        Assertions.assertNull(colChange1.getNextColumn());

        Assertions.assertEquals("TableA", colChange2.getChangedTableName());
        assertColumn("COLFK2", Types.DOUBLE, null, null, false, false, false, colChange2.getNewColumn());
        Assertions.assertEquals("COLFK1", colChange2.getPreviousColumn());
        Assertions.assertNull(colChange2.getNextColumn());

        Assertions.assertEquals("TableA", colChange3.getChangedTableName());
        assertColumn("COLFK3", Types.VARCHAR, "32", null, false, false, false, colChange3.getNewColumn());
        Assertions.assertEquals("COLFK2", colChange3.getPreviousColumn());
        Assertions.assertNull(colChange3.getNextColumn());

        Assertions.assertEquals("TableA", fkChange.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"COLFK2", "COLFK1", "COLFK3"}, new String[]{"ColPK1", "ColPK2", "ColPK3"}, fkChange.getNewForeignKey());
    }

    /**
     * Tests the addition of a multi-column foreign key.
     */
    public void testAddColumnsAndForeignKeyToThem() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK1' type='INTEGER'/>
                <column name='ColFK2' type='DOUBLE'/>
                <column name='ColFK3' type='VARCHAR' size='32'/>
              </table>
              <table name='TableB'>
                <column name='ColPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK1' type='INTEGER'/>
                <column name='COLFK2' type='DOUBLE'/>
                <column name='COLFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
                  <reference local='COLFK2' foreign='COLPK1'/>
                  <reference local='COLFK1' foreign='COLPK2'/>
                  <reference local='COLFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(4, changes.size());

        AddColumnChange colChange1 = (AddColumnChange) changes.get(0);
        AddColumnChange colChange2 = (AddColumnChange) changes.get(1);
        PrimaryKeyChange pkChange = (PrimaryKeyChange) changes.get(2);
        AddForeignKeyChange fkChange = (AddForeignKeyChange) changes.get(3);

        Assertions.assertEquals("TableB", colChange1.getChangedTableName());
        assertColumn("COLPK1", Types.DOUBLE, null, null, false, true, false, colChange1.getNewColumn());
        Assertions.assertNull(colChange1.getPreviousColumn());
        Assertions.assertEquals("ColPK3", colChange1.getNextColumn());

        Assertions.assertEquals("TableB", colChange2.getChangedTableName());
        assertColumn("COLPK2", Types.INTEGER, null, null, false, true, false, colChange2.getNewColumn());
        Assertions.assertEquals("COLPK1", colChange2.getPreviousColumn());
        Assertions.assertEquals("ColPK3", colChange2.getNextColumn());

        Assertions.assertEquals("TableB", pkChange.getChangedTableName());
        Assertions.assertEquals(3, pkChange.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("COLPK1", pkChange.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("COLPK2", pkChange.getNewPrimaryKeyColumns()[1]);
        Assertions.assertEquals("ColPK3", pkChange.getNewPrimaryKeyColumns()[2]);

        Assertions.assertEquals("TableA", fkChange.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"ColFK2", "ColFK1", "ColFK3"}, new String[]{"COLPK1", "COLPK2", "ColPK3"}, fkChange.getNewForeignKey());
    }

    /**
     * Tests the addition of a multi-column foreign key.
     */
    public void testAddColumnsAndForeignKeyBetweenThem() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK2' type='DOUBLE'/>
              </table>
              <table name='TableB'>
                <column name='ColPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK1' type='INTEGER'/>
                <column name='COLFK2' type='DOUBLE'/>
                <column name='COLFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
                  <reference local='COLFK2' foreign='COLPK1'/>
                  <reference local='COLFK1' foreign='COLPK2'/>
                  <reference local='COLFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(6, changes.size());

        AddColumnChange colChange1 = (AddColumnChange) changes.get(0);
        AddColumnChange colChange2 = (AddColumnChange) changes.get(1);
        AddColumnChange colChange3 = (AddColumnChange) changes.get(2);
        AddColumnChange colChange4 = (AddColumnChange) changes.get(3);
        PrimaryKeyChange pkChange = (PrimaryKeyChange) changes.get(4);
        AddForeignKeyChange fkChange = (AddForeignKeyChange) changes.get(5);

        Assertions.assertEquals("TableA", colChange1.getChangedTableName());
        assertColumn("COLFK1", Types.INTEGER, null, null, false, false, false, colChange1.getNewColumn());
        Assertions.assertEquals("ColPK", colChange1.getPreviousColumn());
        Assertions.assertEquals("ColFK2", colChange1.getNextColumn());

        Assertions.assertEquals("TableA", colChange2.getChangedTableName());
        assertColumn("COLFK3", Types.VARCHAR, "32", null, false, false, false, colChange2.getNewColumn());
        Assertions.assertEquals("ColFK2", colChange2.getPreviousColumn());
        Assertions.assertNull(colChange2.getNextColumn());

        Assertions.assertEquals("TableB", colChange3.getChangedTableName());
        assertColumn("COLPK1", Types.DOUBLE, null, null, false, true, false, colChange3.getNewColumn());
        Assertions.assertNull(colChange3.getPreviousColumn());
        Assertions.assertEquals("ColPK3", colChange3.getNextColumn());

        Assertions.assertEquals("TableB", colChange4.getChangedTableName());
        assertColumn("COLPK2", Types.INTEGER, null, null, false, true, false, colChange4.getNewColumn());
        Assertions.assertEquals("COLPK1", colChange4.getPreviousColumn());
        Assertions.assertEquals("ColPK3", colChange4.getNextColumn());

        Assertions.assertEquals("TableB", pkChange.getChangedTableName());
        Assertions.assertEquals(3, pkChange.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("COLPK1", pkChange.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("COLPK2", pkChange.getNewPrimaryKeyColumns()[1]);
        Assertions.assertEquals("ColPK3", pkChange.getNewPrimaryKeyColumns()[2]);

        Assertions.assertEquals("TableA", fkChange.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"ColFK2", "COLFK1", "COLFK3"}, new String[]{"COLPK1", "COLPK2", "ColPK3"}, fkChange.getNewForeignKey());
    }

    /**
     * Tests the addition of a single reference foreign key.
     */
    public void testAddSingleReferenceForeignKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK' type='INTEGER'/>
              </table>
              <table name='TableB'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK' type='INTEGER'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
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

        Assertions.assertEquals(1, changes.size());

        AddForeignKeyChange change = (AddForeignKeyChange) changes.get(0);

        Assertions.assertEquals("TableA", change.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"ColFK"}, new String[]{"ColPK"}, change.getNewForeignKey());
    }

    /**
     * Tests the addition of a multi-reference foreign key.
     */
    public void testAddMultiReferenceForeignKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK1' type='INTEGER'/>
                <column name='ColFK2' type='DOUBLE'/>
                <column name='ColFK3' type='VARCHAR' size='32'/>
              </table>
              <table name='TableB'>
                <column name='ColPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK1' type='INTEGER'/>
                <column name='COLFK2' type='DOUBLE'/>
                <column name='COLFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
                  <reference local='COLFK2' foreign='COLPK1'/>
                  <reference local='COLFK1' foreign='COLPK2'/>
                  <reference local='COLFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        AddForeignKeyChange fkChange = (AddForeignKeyChange) changes.get(0);

        Assertions.assertEquals("TableA", fkChange.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"ColFK2", "ColFK1", "ColFK3"}, new String[]{"ColPK1", "ColPK2", "COLPK3"}, fkChange.getNewForeignKey());
    }

    /**
     * Tests the addition of a column to a multi-reference foreign key.
     */
    public void testAddLocalColumnToMultiReferenceForeignKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK1' type='INTEGER'/>
                <column name='ColFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TableB'>
                  <reference local='ColFK1' foreign='ColPK2'/>
                  <reference local='ColFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TableB'>
                <column name='ColPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK1' type='INTEGER'/>
                <column name='COLFK2' type='DOUBLE'/>
                <column name='COLFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
                  <reference local='COLFK2' foreign='COLPK1'/>
                  <reference local='COLFK1' foreign='COLPK2'/>
                  <reference local='COLFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(4, changes.size());

        RemoveForeignKeyChange fkChange1 = (RemoveForeignKeyChange) changes.get(0);
        AddColumnChange colChange = (AddColumnChange) changes.get(1);
        PrimaryKeyChange pkChange = (PrimaryKeyChange) changes.get(2);
        AddForeignKeyChange fkChange2 = (AddForeignKeyChange) changes.get(3);

        Assertions.assertEquals("TableA", fkChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getForeignKey(0), fkChange1.findChangedForeignKey(model1, false));

        Assertions.assertEquals("TableA", colChange.getChangedTableName());
        assertColumn("COLFK2", Types.DOUBLE, null, null, false, false, false, colChange.getNewColumn());
        Assertions.assertEquals("ColFK1", colChange.getPreviousColumn());
        Assertions.assertEquals("ColFK3", colChange.getNextColumn());

        Assertions.assertEquals("TableB", pkChange.getChangedTableName());
        Assertions.assertEquals(3, pkChange.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("ColPK1", pkChange.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("ColPK2", pkChange.getNewPrimaryKeyColumns()[1]);
        Assertions.assertEquals("COLPK3", pkChange.getNewPrimaryKeyColumns()[2]);

        Assertions.assertEquals("TableA", fkChange2.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"COLFK2", "ColFK1", "ColFK3"}, new String[]{"ColPK1", "ColPK2", "COLPK3"}, fkChange2.getNewForeignKey());
    }

    /**
     * Tests the addition of a column to a multi-reference foreign key.
     */
    public void testAddForeignColumnToMultiReferenceForeignKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK1' type='INTEGER'/>
                <column name='ColFK2' type='DOUBLE'/>
                <column name='ColFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TableB'>
                  <reference local='ColFK2' foreign='ColPK1'/>
                  <reference local='ColFK1' foreign='ColPK2'/>
                </foreign-key>
              </table>
              <table name='TableB'>
                <column name='ColPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK1' type='INTEGER'/>
                <column name='COLFK2' type='DOUBLE'/>
                <column name='COLFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
                  <reference local='COLFK2' foreign='COLPK1'/>
                  <reference local='COLFK1' foreign='COLPK2'/>
                  <reference local='COLFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(4, changes.size());

        RemoveForeignKeyChange fkChange1 = (RemoveForeignKeyChange) changes.get(0);
        AddColumnChange colChange = (AddColumnChange) changes.get(1);
        PrimaryKeyChange pkChange = (PrimaryKeyChange) changes.get(2);
        AddForeignKeyChange fkChange2 = (AddForeignKeyChange) changes.get(3);

        Assertions.assertEquals("TableA", fkChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getForeignKey(0), fkChange1.findChangedForeignKey(model1, false));

        Assertions.assertEquals("TableB", colChange.getChangedTableName());
        assertColumn("COLPK3", Types.VARCHAR, "32", null, false, true, false, colChange.getNewColumn());
        Assertions.assertEquals("ColPK2", colChange.getPreviousColumn());
        Assertions.assertNull(colChange.getNextColumn());

        Assertions.assertEquals("TableB", pkChange.getChangedTableName());
        Assertions.assertEquals(3, pkChange.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("ColPK1", pkChange.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("ColPK2", pkChange.getNewPrimaryKeyColumns()[1]);
        Assertions.assertEquals("COLPK3", pkChange.getNewPrimaryKeyColumns()[2]);

        Assertions.assertEquals("TableA", fkChange2.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"ColFK2", "ColFK1", "ColFK3"}, new String[]{"ColPK1", "ColPK2", "COLPK3"}, fkChange2.getNewForeignKey());
    }

    /**
     * Tests the addition of columns to a single-reference foreign key.
     */
    public void testAddColumnsToSingleReferenceForeignKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK2' type='DOUBLE'/>
                <foreign-key name='TESTFK' foreignTable='TableB'>
                  <reference local='ColFK2' foreign='ColPK1'/>
                </foreign-key>
              </table>
              <table name='TableB'>
                <column name='ColPK1' type='DOUBLE' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK1' type='INTEGER'/>
                <column name='COLFK2' type='DOUBLE'/>
                <column name='COLFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
                  <reference local='COLFK2' foreign='COLPK1'/>
                  <reference local='COLFK1' foreign='COLPK2'/>
                  <reference local='COLFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(7, changes.size());

        RemoveForeignKeyChange fkChange1 = (RemoveForeignKeyChange) changes.get(0);
        AddColumnChange colChange1 = (AddColumnChange) changes.get(1);
        AddColumnChange colChange2 = (AddColumnChange) changes.get(2);
        AddColumnChange colChange3 = (AddColumnChange) changes.get(3);
        AddColumnChange colChange4 = (AddColumnChange) changes.get(4);
        PrimaryKeyChange pkChange = (PrimaryKeyChange) changes.get(5);
        AddForeignKeyChange fkChange2 = (AddForeignKeyChange) changes.get(6);

        Assertions.assertEquals("TableA", fkChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getForeignKey(0), fkChange1.findChangedForeignKey(model1, false));

        Assertions.assertEquals("TableA", colChange1.getChangedTableName());
        assertColumn("COLFK1", Types.INTEGER, null, null, false, false, false, colChange1.getNewColumn());
        Assertions.assertEquals("ColPK", colChange1.getPreviousColumn());
        Assertions.assertEquals("ColFK2", colChange1.getNextColumn());

        Assertions.assertEquals("TableA", colChange2.getChangedTableName());
        assertColumn("COLFK3", Types.VARCHAR, "32", null, false, false, false, colChange2.getNewColumn());
        Assertions.assertEquals("ColFK2", colChange2.getPreviousColumn());
        Assertions.assertNull(colChange2.getNextColumn());

        Assertions.assertEquals("TableB", colChange3.getChangedTableName());
        assertColumn("COLPK2", Types.INTEGER, null, null, false, true, false, colChange3.getNewColumn());
        Assertions.assertEquals("ColPK1", colChange3.getPreviousColumn());
        Assertions.assertNull(colChange3.getNextColumn());

        Assertions.assertEquals("TableB", colChange4.getChangedTableName());
        assertColumn("COLPK3", Types.VARCHAR, "32", null, false, true, false, colChange4.getNewColumn());
        Assertions.assertEquals("COLPK2", colChange4.getPreviousColumn());
        Assertions.assertNull(colChange4.getNextColumn());

        Assertions.assertEquals("TableB", pkChange.getChangedTableName());
        Assertions.assertEquals(3, pkChange.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("ColPK1", pkChange.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("COLPK2", pkChange.getNewPrimaryKeyColumns()[1]);
        Assertions.assertEquals("COLPK3", pkChange.getNewPrimaryKeyColumns()[2]);

        Assertions.assertEquals("TableA", fkChange2.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"ColFK2", "COLFK1", "COLFK3"}, new String[]{"ColPK1", "COLPK2", "COLPK3"}, fkChange2.getNewForeignKey());
    }

    /**
     * Tests the addition of a reference to a multi-reference foreign key.
     */
    public void testAddReferenceToMultiReferenceForeignKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK1' type='INTEGER'/>
                <column name='ColFK2' type='DOUBLE'/>
                <column name='ColFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TableB'>
                  <reference local='ColFK2' foreign='COLPK1'/>
                  <reference local='ColFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TableB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK1' type='INTEGER'/>
                <column name='COLFK2' type='DOUBLE'/>
                <column name='COLFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
                  <reference local='COLFK2' foreign='COLPK1'/>
                  <reference local='COLFK1' foreign='COLPK2'/>
                  <reference local='COLFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(3, changes.size());

        RemoveForeignKeyChange fkChange1 = (RemoveForeignKeyChange) changes.get(0);
        PrimaryKeyChange pkChange = (PrimaryKeyChange) changes.get(1);
        AddForeignKeyChange fkChange2 = (AddForeignKeyChange) changes.get(2);

        Assertions.assertEquals("TableA", fkChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getForeignKey(0), fkChange1.findChangedForeignKey(model1, false));

        Assertions.assertEquals("TableB", pkChange.getChangedTableName());
        Assertions.assertEquals(3, pkChange.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("COLPK1", pkChange.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("COLPK2", pkChange.getNewPrimaryKeyColumns()[1]);
        Assertions.assertEquals("COLPK3", pkChange.getNewPrimaryKeyColumns()[2]);

        Assertions.assertEquals("TableA", fkChange2.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"ColFK2", "ColFK1", "ColFK3"}, new String[]{"COLPK1", "COLPK2", "COLPK3"}, fkChange2.getNewForeignKey());
    }

    /**
     * Tests the addition of references to a single-reference foreign key.
     */
    public void testAddReferencesToSingleReferenceForeignKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK1' type='INTEGER'/>
                <column name='ColFK2' type='DOUBLE'/>
                <column name='ColFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TableB'>
                  <reference local='ColFK2' foreign='COLPK1'/>
                </foreign-key>
              </table>
              <table name='TableB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK1' type='INTEGER'/>
                <column name='COLFK2' type='DOUBLE'/>
                <column name='COLFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
                  <reference local='COLFK2' foreign='COLPK1'/>
                  <reference local='COLFK1' foreign='COLPK2'/>
                  <reference local='COLFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(3, changes.size());

        RemoveForeignKeyChange fkChange1 = (RemoveForeignKeyChange) changes.get(0);
        PrimaryKeyChange pkChange = (PrimaryKeyChange) changes.get(1);
        AddForeignKeyChange fkChange2 = (AddForeignKeyChange) changes.get(2);

        Assertions.assertEquals("TableA", fkChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getForeignKey(0), fkChange1.findChangedForeignKey(model1, false));

        Assertions.assertEquals("TableB", pkChange.getChangedTableName());
        Assertions.assertEquals(3, pkChange.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("COLPK1", pkChange.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("COLPK2", pkChange.getNewPrimaryKeyColumns()[1]);
        Assertions.assertEquals("COLPK3", pkChange.getNewPrimaryKeyColumns()[2]);

        Assertions.assertEquals("TableA", fkChange2.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"ColFK2", "ColFK1", "ColFK3"}, new String[]{"COLPK1", "COLPK2", "COLPK3"}, fkChange2.getNewForeignKey());
    }

    /**
     * Tests that the order of the references in a foreign key is not important.
     */
    public void testForeignKeyReferenceOrder() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK1' type='INTEGER'/>
                <column name='ColFK2' type='INTEGER'/>
                <foreign-key name='TestFK' foreignTable='TableB'>
                  <reference local='ColFK1' foreign='ColPK1'/>
                  <reference local='ColFK2' foreign='ColPK2'/>
                </foreign-key>
              </table>
              <table name='TableB'>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK1' type='INTEGER'/>
                <column name='ColFK2' type='INTEGER'/>
                <foreign-key name='TestFK' foreignTable='TableB'>
                  <reference local='ColFK2' foreign='ColPK2'/>
                  <reference local='ColFK1' foreign='ColPK1'/>
                </foreign-key>
              </table>
              <table name='TableB'>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(true).getChanges(model1, model2);

        Assertions.assertTrue(changes.isEmpty());
    }

    /**
     * Tests adding a reference to a foreign key and changing the order of references.
     */
    public void testAddReferenceToForeignKeyAndChangeOrder() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK1' type='INTEGER'/>
                <column name='ColFK2' type='DOUBLE'/>
                <column name='ColFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TableB'>
                  <reference local='ColFK2' foreign='COLPK1'/>
                  <reference local='ColFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TableB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK1' type='INTEGER'/>
                <column name='COLFK2' type='DOUBLE'/>
                <column name='COLFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
                  <reference local='COLFK1' foreign='COLPK2'/>
                  <reference local='COLFK2' foreign='COLPK1'/>
                  <reference local='COLFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(3, changes.size());

        RemoveForeignKeyChange fkChange1 = (RemoveForeignKeyChange) changes.get(0);
        PrimaryKeyChange pkChange = (PrimaryKeyChange) changes.get(1);
        AddForeignKeyChange fkChange2 = (AddForeignKeyChange) changes.get(2);

        Assertions.assertEquals("TableA", fkChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getForeignKey(0), fkChange1.findChangedForeignKey(model1, false));

        Assertions.assertEquals("TableB", pkChange.getChangedTableName());
        Assertions.assertEquals(3, pkChange.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("COLPK1", pkChange.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("COLPK2", pkChange.getNewPrimaryKeyColumns()[1]);
        Assertions.assertEquals("COLPK3", pkChange.getNewPrimaryKeyColumns()[2]);

        Assertions.assertEquals("TableA", fkChange2.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"ColFK1", "ColFK2", "ColFK3"}, new String[]{"COLPK2", "COLPK1", "COLPK3"}, fkChange2.getNewForeignKey());
    }

    /**
     * Tests removing a reference from a foreign key.
     */
    public void testRemoveReferenceFromForeignKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK1' type='INTEGER'/>
                <column name='ColFK2' type='DOUBLE'/>
                <column name='ColFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TableB'>
                  <reference local='ColFK1' foreign='COLPK2'/>
                  <reference local='ColFK2' foreign='COLPK1'/>
                  <reference local='ColFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TableB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK1' type='INTEGER'/>
                <column name='COLFK2' type='DOUBLE'/>
                <column name='COLFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
                  <reference local='COLFK1' foreign='COLPK2'/>
                  <reference local='COLFK2' foreign='COLPK1'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(3, changes.size());

        RemoveForeignKeyChange fkChange1 = (RemoveForeignKeyChange) changes.get(0);
        PrimaryKeyChange pkChange = (PrimaryKeyChange) changes.get(1);
        AddForeignKeyChange fkChange2 = (AddForeignKeyChange) changes.get(2);

        Assertions.assertEquals("TableA", fkChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getForeignKey(0), fkChange1.findChangedForeignKey(model1, false));

        Assertions.assertEquals("TableB", pkChange.getChangedTableName());
        Assertions.assertEquals(2, pkChange.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("COLPK1", pkChange.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("COLPK2", pkChange.getNewPrimaryKeyColumns()[1]);

        Assertions.assertEquals("TableA", fkChange2.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"ColFK1", "ColFK2"}, new String[]{"COLPK2", "COLPK1"}, fkChange2.getNewForeignKey());
    }

    /**
     * Tests removing a reference from a foreign key and changing the order of references.
     */
    public void testRemoveReferenceFromForeignKeyAndChangeOrder() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK1' type='INTEGER'/>
                <column name='ColFK2' type='DOUBLE'/>
                <column name='ColFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TableB'>
                  <reference local='ColFK1' foreign='COLPK2'/>
                  <reference local='ColFK2' foreign='COLPK1'/>
                  <reference local='ColFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TableB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK1' type='INTEGER'/>
                <column name='COLFK2' type='DOUBLE'/>
                <column name='COLFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
                  <reference local='COLFK2' foreign='COLPK1'/>
                  <reference local='COLFK1' foreign='COLPK2'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(3, changes.size());

        RemoveForeignKeyChange fkChange1 = (RemoveForeignKeyChange) changes.get(0);
        PrimaryKeyChange pkChange = (PrimaryKeyChange) changes.get(1);
        AddForeignKeyChange fkChange2 = (AddForeignKeyChange) changes.get(2);

        Assertions.assertEquals("TableA", fkChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getForeignKey(0), fkChange1.findChangedForeignKey(model1, false));

        Assertions.assertEquals("TableB", pkChange.getChangedTableName());
        Assertions.assertEquals(2, pkChange.getNewPrimaryKeyColumns().length);
        Assertions.assertEquals("COLPK1", pkChange.getNewPrimaryKeyColumns()[0]);
        Assertions.assertEquals("COLPK2", pkChange.getNewPrimaryKeyColumns()[1]);

        Assertions.assertEquals("TableA", fkChange2.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"ColFK2", "ColFK1"}, new String[]{"COLPK1", "COLPK2"}, fkChange2.getNewForeignKey());
    }

    // TODO: drop column  from reference PK

    /**
     * Tests dropping columns used in a foreign key.
     */
    public void testDropColumnsFromForeignKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK1' type='INTEGER'/>
                <column name='ColFK2' type='DOUBLE'/>
                <column name='ColFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TableB'>
                  <reference local='ColFK1' foreign='COLPK2'/>
                  <reference local='ColFK2' foreign='COLPK1'/>
                  <reference local='ColFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TableB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK1' type='INTEGER'/>
                <column name='COLFK2' type='DOUBLE'/>
                <foreign-key name='TESTFK' foreignTable='TABLEB'>
                  <reference local='COLFK1' foreign='COLPK2'/>
                </foreign-key>
              </table>
              <table name='TABLEB'>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(5, changes.size());

        RemoveForeignKeyChange fkChange1 = (RemoveForeignKeyChange) changes.get(0);
        RemoveColumnChange colChange1 = (RemoveColumnChange) changes.get(1);
        RemoveColumnChange colChange2 = (RemoveColumnChange) changes.get(2);
        RemoveColumnChange colChange3 = (RemoveColumnChange) changes.get(3);
        AddForeignKeyChange fkChange2 = (AddForeignKeyChange) changes.get(4);

        Assertions.assertEquals("TableA", fkChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getForeignKey(0), fkChange1.findChangedForeignKey(model1, false));

        Assertions.assertEquals("TableA", colChange1.getChangedTableName());
        Assertions.assertEquals("ColFK3", colChange1.getChangedColumn());

        Assertions.assertEquals("TableB", colChange2.getChangedTableName());
        Assertions.assertEquals("COLPK1", colChange2.getChangedColumn());

        Assertions.assertEquals("TableB", colChange3.getChangedTableName());
        Assertions.assertEquals("COLPK3", colChange3.getChangedColumn());

        Assertions.assertEquals("TableA", fkChange2.getChangedTableName());
        assertForeignKey("TESTFK", "TableB", new String[]{"ColFK1"}, new String[]{"COLPK2"}, fkChange2.getNewForeignKey());
    }

    /**
     * Tests the removal of a foreign key.
     */
    public void testDropSingleReferenceForeignKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='TableB'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK' type='INTEGER'/>
                <foreign-key name='TestFK' foreignTable='TableA'>
                  <reference local='ColFK' foreign='ColPK'/>
                </foreign-key>
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
                <column name='COLFK' type='INTEGER'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        RemoveForeignKeyChange change = (RemoveForeignKeyChange) changes.get(0);

        Assertions.assertEquals("TableB", change.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableB").getForeignKey(0), change.findChangedForeignKey(model1, false));
    }


    /**
     * Tests dropping a multi-reference foreign key.
     */
    public void testDropMultiReferenceForeignKey() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK1' type='INTEGER'/>
                <column name='ColFK2' type='DOUBLE'/>
                <column name='ColFK3' type='VARCHAR' size='32'/>
                <foreign-key name='TESTFK' foreignTable='TableB'>
                  <reference local='ColFK1' foreign='COLPK2'/>
                  <reference local='ColFK2' foreign='COLPK1'/>
                  <reference local='ColFK3' foreign='COLPK3'/>
                </foreign-key>
              </table>
              <table name='TableB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLFK1' type='INTEGER'/>
                <column name='COLFK2' type='DOUBLE'/>
                <column name='COLFK3' type='VARCHAR' size='32'/>
              </table>
              <table name='TABLEB'>
                <column name='COLPK1' type='DOUBLE' primaryKey='true' required='true'/>
                <column name='COLPK2' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COLPK3' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        RemoveForeignKeyChange fkChange = (RemoveForeignKeyChange) changes.get(0);

        Assertions.assertEquals("TableA", fkChange.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getForeignKey(0), fkChange.findChangedForeignKey(model1, false));
    }

    /**
     * Tests the addition and removal of a foreign key.
     */
    public void testAddAndDropForeignKey1() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='TableB'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK' type='INTEGER'/>
                <foreign-key name='TestFK' foreignTable='TableA'>
                  <reference local='ColFK' foreign='ColPK'/>
                </foreign-key>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='TableB'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK' type='INTEGER'/>
                <foreign-key name='TESTFK' foreignTable='TableA'>
                  <reference local='ColFK' foreign='ColPK'/>
                </foreign-key>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(true).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        RemoveForeignKeyChange change1 = (RemoveForeignKeyChange) changes.get(0);
        AddForeignKeyChange change2 = (AddForeignKeyChange) changes.get(1);

        Assertions.assertEquals("TableB", change1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableB").getForeignKey(0), change1.findChangedForeignKey(model1, true));

        Assertions.assertEquals("TableB", change2.getChangedTableName());
        assertForeignKey("TESTFK", "TableA", new String[]{"ColFK"}, new String[]{"ColPK"}, change2.getNewForeignKey());
    }

    /**
     * Tests the recreation of a foreign key because of a change of the references.
     */
    public void testAddAndDropForeignKey2() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK1' type='INTEGER'/>
                <column name='ColFK2' type='INTEGER'/>
                <foreign-key name='TestFK' foreignTable='TableB'>
                  <reference local='ColFK1' foreign='ColPK1'/>
                  <reference local='ColFK2' foreign='ColPK2'/>
                </foreign-key>
              </table>
              <table name='TableB'>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColFK1' type='INTEGER'/>
                <column name='ColFK2' type='INTEGER'/>
                <foreign-key name='TestFK' foreignTable='TableB'>
                  <reference local='ColFK1' foreign='ColPK2'/>
                  <reference local='ColFK2' foreign='ColPK1'/>
                </foreign-key>
              </table>
              <table name='TableB'>
                <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='ColPK2' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(true).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        RemoveForeignKeyChange change1 = (RemoveForeignKeyChange) changes.get(0);
        AddForeignKeyChange change2 = (AddForeignKeyChange) changes.get(1);

        Assertions.assertEquals("TableA", change1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getForeignKey(0), change1.findChangedForeignKey(model1, true));

        Assertions.assertEquals("TableA", change2.getChangedTableName());
        assertForeignKey("TestFK", "TableB", new String[]{"ColFK1", "ColFK2"}, new String[]{"ColPK2", "ColPK1"}, change2.getNewForeignKey());
    }

    // TODO: foreign key change to different table
    // TODO: foreign key change to different columns (?)
}
