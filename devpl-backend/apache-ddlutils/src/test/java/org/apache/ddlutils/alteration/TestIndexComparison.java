package org.apache.ddlutils.alteration;


import org.apache.ddlutils.model.Database;
import org.junit.jupiter.api.Assertions;

import java.sql.Types;
import java.util.List;

/**
 * Tests the model comparison.
 * <p>
 * TODO: need tests with indexes without a name
 */
public class TestIndexComparison extends TestComparisonBase {
    /**
     * Tests the addition of an index with one column.
     */
    public void testAddSingleColumnIndex1() {
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
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL' type='INTEGER'/>
                <index name='TESTINDEX'>
                  <index-column name='COL'/>
                </index>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        AddIndexChange change = (AddIndexChange) changes.get(0);

        Assertions.assertEquals("TableA", change.getChangedTableName());
        assertIndex("TESTINDEX", false, new String[]{"Col"}, change.getNewIndex());
    }

    /**
     * Tests the addition of an index with one column.
     */
    public void testAddSingleColumnIndex2() {
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
                <column name='COL' type='INTEGER'/>
                <index name='TESTINDEX'>
                  <index-column name='COL'/>
                </index>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        AddColumnChange colChange = (AddColumnChange) changes.get(0);
        AddIndexChange indexChange = (AddIndexChange) changes.get(1);

        Assertions.assertEquals("TableA", colChange.getChangedTableName());
        assertColumn("COL", Types.INTEGER, null, null, false, false, false, colChange.getNewColumn());
        Assertions.assertEquals("ColPK", colChange.getPreviousColumn());
        Assertions.assertNull(colChange.getNextColumn());

        Assertions.assertEquals("TableA", indexChange.getChangedTableName());
        assertIndex("TESTINDEX", false, new String[]{"COL"}, indexChange.getNewIndex());
    }

    /**
     * Tests the addition of an index with multiple columns.
     */
    public void testAddMultiColumnIndex1() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Col1' type='INTEGER'/>
                <column name='Col2' type='DOUBLE'/>
                <column name='Col3' type='VARCHAR' size='32'/>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL2' type='DOUBLE'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <unique name='TESTINDEX'>
                  <unique-column name='COL3'/>
                  <unique-column name='COL1'/>
                  <unique-column name='COL2'/>
                </unique>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        AddIndexChange change = (AddIndexChange) changes.get(0);

        Assertions.assertEquals("TableA", change.getChangedTableName());
        assertIndex("TESTINDEX", true, new String[]{"Col3", "Col1", "Col2"}, change.getNewIndex());
    }

    /**
     * Tests the addition of an index with multiple columns.
     */
    public void testAddMultiColumnIndex2() {
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
                <column name='COL1' type='INTEGER'/>
                <column name='COL2' type='DOUBLE'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <unique name='TESTINDEX'>
                  <unique-column name='COL3'/>
                  <unique-column name='COL1'/>
                  <unique-column name='COL2'/>
                </unique>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(4, changes.size());

        AddColumnChange colChange1 = (AddColumnChange) changes.get(0);
        AddColumnChange colChange2 = (AddColumnChange) changes.get(1);
        AddColumnChange colChange3 = (AddColumnChange) changes.get(2);
        AddIndexChange indexChange = (AddIndexChange) changes.get(3);

        Assertions.assertEquals("TableA", colChange1.getChangedTableName());
        assertColumn("COL1", Types.INTEGER, null, null, false, false, false, colChange1.getNewColumn());
        Assertions.assertEquals("ColPK", colChange1.getPreviousColumn());
        Assertions.assertNull(colChange1.getNextColumn());

        Assertions.assertEquals("TableA", colChange2.getChangedTableName());
        assertColumn("COL2", Types.DOUBLE, null, null, false, false, false, colChange2.getNewColumn());
        Assertions.assertEquals("COL1", colChange2.getPreviousColumn());
        Assertions.assertNull(colChange2.getNextColumn());

        Assertions.assertEquals("TableA", colChange3.getChangedTableName());
        assertColumn("COL3", Types.VARCHAR, "32", null, false, false, false, colChange3.getNewColumn());
        Assertions.assertEquals("COL2", colChange3.getPreviousColumn());
        Assertions.assertNull(colChange3.getNextColumn());

        Assertions.assertEquals("TableA", indexChange.getChangedTableName());
        assertIndex("TESTINDEX", true, new String[]{"COL3", "COL1", "COL2"}, indexChange.getNewIndex());
    }

    /**
     * Tests the addition of a column into an existing index with multiple columns.
     */
    public void testAddNewColumnToMultiColumnIndex() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <unique name='TESTINDEX'>
                  <unique-column name='COL3'/>
                  <unique-column name='COL1'/>
                </unique>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL2' type='DOUBLE'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <unique name='TESTINDEX'>
                  <unique-column name='COL3'/>
                  <unique-column name='COL1'/>
                  <unique-column name='COL2'/>
                </unique>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(3, changes.size());

        RemoveIndexChange indexChange1 = (RemoveIndexChange) changes.get(0);
        AddColumnChange colChange = (AddColumnChange) changes.get(1);
        AddIndexChange indexChange2 = (AddIndexChange) changes.get(2);

        Assertions.assertEquals("TableA", indexChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getIndex(0), indexChange1.findChangedIndex(model1, false));

        Assertions.assertEquals("TableA", colChange.getChangedTableName());
        assertColumn("COL2", Types.DOUBLE, null, null, false, false, false, colChange.getNewColumn());
        Assertions.assertEquals("COL1", colChange.getPreviousColumn());
        Assertions.assertEquals("COL3", colChange.getNextColumn());

        Assertions.assertEquals("TableA", indexChange2.getChangedTableName());
        assertIndex("TESTINDEX", true, new String[]{"COL3", "COL1", "COL2"}, indexChange2.getNewIndex());
    }

    /**
     * Tests the addition of columns to an existing index with a single column.
     */
    public void testAddNewColumnsToSingleColumnIndex() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <unique name='TESTINDEX'>
                  <unique-column name='COL3'/>
                </unique>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL2' type='DOUBLE'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <unique name='TESTINDEX'>
                  <unique-column name='COL1'/>
                  <unique-column name='COL3'/>
                  <unique-column name='COL2'/>
                </unique>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(4, changes.size());

        RemoveIndexChange indexChange1 = (RemoveIndexChange) changes.get(0);
        AddColumnChange colChange1 = (AddColumnChange) changes.get(1);
        AddColumnChange colChange2 = (AddColumnChange) changes.get(2);
        AddIndexChange indexChange2 = (AddIndexChange) changes.get(3);

        Assertions.assertEquals("TableA", indexChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getIndex(0), indexChange1.findChangedIndex(model1, false));

        Assertions.assertEquals("TableA", colChange1.getChangedTableName());
        assertColumn("COL1", Types.INTEGER, null, null, false, false, false, colChange1.getNewColumn());
        Assertions.assertEquals("ColPK", colChange1.getPreviousColumn());
        Assertions.assertEquals("COL3", colChange1.getNextColumn());

        Assertions.assertEquals("TableA", colChange2.getChangedTableName());
        assertColumn("COL2", Types.DOUBLE, null, null, false, false, false, colChange2.getNewColumn());
        Assertions.assertEquals("COL1", colChange2.getPreviousColumn());
        Assertions.assertEquals("COL3", colChange2.getNextColumn());

        Assertions.assertEquals("TableA", indexChange2.getChangedTableName());
        assertIndex("TESTINDEX", true, new String[]{"COL1", "COL3", "COL2"}, indexChange2.getNewIndex());
    }

    /**
     * Tests the addition of a column to an index with multiple columns.
     */
    public void testAddColumnToMultiColumnIndex() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL2' type='DOUBLE'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <index name='TESTINDEX'>
                  <index-column name='COL3'/>
                  <index-column name='COL2'/>
                </index>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL2' type='DOUBLE'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <index name='TESTINDEX'>
                  <index-column name='COL3'/>
                  <index-column name='COL1'/>
                  <index-column name='COL2'/>
                </index>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        RemoveIndexChange indexChange1 = (RemoveIndexChange) changes.get(0);
        AddIndexChange indexChange2 = (AddIndexChange) changes.get(1);

        Assertions.assertEquals("TableA", indexChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getIndex(0), indexChange1.findChangedIndex(model1, false));

        Assertions.assertEquals("TableA", indexChange2.getChangedTableName());
        assertIndex("TESTINDEX", false, new String[]{"COL3", "COL1", "COL2"}, indexChange2.getNewIndex());
    }

    /**
     * Tests the addition of columns to an index with a single column.
     */
    public void testAddColumnsToSingleColumnIndex() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL2' type='DOUBLE'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <unique name='TESTINDEX'>
                  <unique-column name='COL1'/>
                </unique>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL2' type='DOUBLE'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <unique name='TESTINDEX'>
                  <unique-column name='COL3'/>
                  <unique-column name='COL1'/>
                  <unique-column name='COL2'/>
                </unique>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        RemoveIndexChange indexChange1 = (RemoveIndexChange) changes.get(0);
        AddIndexChange indexChange2 = (AddIndexChange) changes.get(1);

        Assertions.assertEquals("TableA", indexChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getIndex(0), indexChange1.findChangedIndex(model1, false));

        Assertions.assertEquals("TableA", indexChange2.getChangedTableName());
        assertIndex("TESTINDEX", true, new String[]{"COL3", "COL1", "COL2"}, indexChange2.getNewIndex());
    }

    /**
     * Tests the addition and removal of an index because of the change of column order.
     */
    public void testChangeIndexColumnOrder() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Col1' type='INTEGER'/>
                <column name='Col2' type='DOUBLE'/>
                <unique name='TestIndex'>
                  <unique-column name='Col1'/>
                  <unique-column name='Col2'/>
                </unique>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Col1' type='INTEGER'/>
                <column name='Col2' type='DOUBLE'/>
                <unique name='TestIndex'>
                  <unique-column name='Col2'/>
                  <unique-column name='Col1'/>
                </unique>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        RemoveIndexChange change1 = (RemoveIndexChange) changes.get(0);
        AddIndexChange change2 = (AddIndexChange) changes.get(1);

        Assertions.assertEquals("TableA", change1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getIndex(0), change1.findChangedIndex(model1, false));

        Assertions.assertEquals("TableA", change2.getChangedTableName());
        assertIndex("TestIndex", true, new String[]{"Col2", "Col1"}, change2.getNewIndex());
    }

    /**
     * Tests the recreation of an index because of the addition of an index column.
     */
    public void testAddIndexColumn() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Col1' type='INTEGER'/>
                <column name='Col2' type='DOUBLE'/>
                <index name='TestIndex'>
                  <index-column name='Col1'/>
                </index>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Col1' type='INTEGER'/>
                <column name='Col2' type='DOUBLE'/>
                <index name='TestIndex'>
                  <index-column name='Col1'/>
                  <index-column name='Col2'/>
                </index>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        RemoveIndexChange change1 = (RemoveIndexChange) changes.get(0);
        AddIndexChange change2 = (AddIndexChange) changes.get(1);

        Assertions.assertEquals("TableA", change1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getIndex(0), change1.findChangedIndex(model1, false));

        Assertions.assertEquals("TableA", change2.getChangedTableName());
        assertIndex("TestIndex", false, new String[]{"Col1", "Col2"}, change2.getNewIndex());
    }

    /**
     * Tests the addition and removal of an index because of the removal of an index column.
     */
    public void testRemoveIndexColumn() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Col1' type='INTEGER'/>
                <column name='Col2' type='DOUBLE'/>
                <index name='TestIndex'>
                  <index-column name='Col1'/>
                  <index-column name='Col2'/>
                </index>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Col1' type='INTEGER'/>
                <column name='Col2' type='DOUBLE'/>
                <index name='TestIndex'>
                  <index-column name='Col1'/>
                </index>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        RemoveIndexChange change1 = (RemoveIndexChange) changes.get(0);
        AddIndexChange change2 = (AddIndexChange) changes.get(1);

        Assertions.assertEquals("TableA", change1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getIndex(0), change1.findChangedIndex(model1, false));

        Assertions.assertEquals("TableA", change2.getChangedTableName());
        assertIndex("TestIndex", false, new String[]{"Col1"}, change2.getNewIndex());
    }

    /**
     * Tests changing the type of index.
     */
    public void testChangeIndexType() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Col1' type='INTEGER'/>
                <column name='Col2' type='DOUBLE'/>
                <index name='TestIndex'>
                  <index-column name='Col1'/>
                  <index-column name='Col2'/>
                </index>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Col1' type='INTEGER'/>
                <column name='Col2' type='DOUBLE'/>
                <unique name='TestIndex'>
                  <unique-column name='Col1'/>
                  <unique-column name='Col2'/>
                </unique>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        RemoveIndexChange change1 = (RemoveIndexChange) changes.get(0);
        AddIndexChange change2 = (AddIndexChange) changes.get(1);

        Assertions.assertEquals("TableA", change1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getIndex(0), change1.findChangedIndex(model1, false));

        Assertions.assertEquals("TableA", change2.getChangedTableName());
        assertIndex("TestIndex", true, new String[]{"Col1", "Col2"}, change2.getNewIndex());
    }

    /**
     * Tests the removal of a column that is the single column in an index.
     */
    public void testDropColumnFromSingleColumnIndex() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <index name='TESTINDEX'>
                  <index-column name='COL1'/>
                </index>
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
        List changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        RemoveIndexChange indexChange = (RemoveIndexChange) changes.get(0);
        RemoveColumnChange colChange = (RemoveColumnChange) changes.get(1);

        Assertions.assertEquals("TableA", indexChange.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getIndex(0), indexChange.findChangedIndex(model1, false));

        Assertions.assertEquals("TableA", colChange.getChangedTableName());
        Assertions.assertEquals("COL1", colChange.getChangedColumn());
    }

    /**
     * Tests the removal of a column that is part of an index.
     */
    public void testDropColumnFromMultiColumnIndex() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL2' type='DOUBLE'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <unique name='TESTINDEX'>
                  <unique-column name='COL3'/>
                  <unique-column name='COL2'/>
                  <unique-column name='COL1'/>
                </unique>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <unique name='TESTINDEX'>
                  <unique-column name='COL3'/>
                  <unique-column name='COL1'/>
                </unique>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(3, changes.size());

        RemoveIndexChange indexChange1 = (RemoveIndexChange) changes.get(0);
        RemoveColumnChange colChange = (RemoveColumnChange) changes.get(1);
        AddIndexChange indexChange2 = (AddIndexChange) changes.get(2);

        Assertions.assertEquals("TableA", indexChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getIndex(0), indexChange1.findChangedIndex(model1, false));

        Assertions.assertEquals("TableA", colChange.getChangedTableName());
        Assertions.assertEquals("COL2", colChange.getChangedColumn());

        Assertions.assertEquals("TableA", indexChange2.getChangedTableName());
        assertIndex("TESTINDEX", true, new String[]{"COL3", "COL1"}, indexChange2.getNewIndex());
    }

    /**
     * Tests the addition of a column and changing the order of the columns in an index.
     */
    public void testAddColumnAndChangeIndexColumnOrder() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <unique name='TESTINDEX'>
                  <unique-column name='COL3'/>
                  <unique-column name='COL1'/>
                </unique>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL2' type='DOUBLE'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <unique name='TESTINDEX'>
                  <unique-column name='COL1'/>
                  <unique-column name='COL2'/>
                  <unique-column name='COL3'/>
                </unique>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(3, changes.size());

        RemoveIndexChange indexChange1 = (RemoveIndexChange) changes.get(0);
        AddColumnChange colChange = (AddColumnChange) changes.get(1);
        AddIndexChange indexChange2 = (AddIndexChange) changes.get(2);

        Assertions.assertEquals("TableA", indexChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getIndex(0), indexChange1.findChangedIndex(model1, false));

        Assertions.assertEquals("TableA", colChange.getChangedTableName());
        assertColumn("COL2", Types.DOUBLE, null, null, false, false, false, colChange.getNewColumn());
        Assertions.assertEquals("COL1", colChange.getPreviousColumn());
        Assertions.assertEquals("COL3", colChange.getNextColumn());

        Assertions.assertEquals("TableA", indexChange2.getChangedTableName());
        assertIndex("TESTINDEX", true, new String[]{"COL1", "COL2", "COL3"}, indexChange2.getNewIndex());
    }

    /**
     * Tests the removal of a column and changing the order of the columns in an index.
     */
    public void testDropColumnAndChangeIndexColumnOrder() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL2' type='DOUBLE'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <unique name='TESTINDEX'>
                  <unique-column name='COL3'/>
                  <unique-column name='COL2'/>
                  <unique-column name='COL1'/>
                </unique>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <unique name='TESTINDEX'>
                  <unique-column name='COL1'/>
                  <unique-column name='COL3'/>
                </unique>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(3, changes.size());

        RemoveIndexChange indexChange1 = (RemoveIndexChange) changes.get(0);
        RemoveColumnChange colChange = (RemoveColumnChange) changes.get(1);
        AddIndexChange indexChange2 = (AddIndexChange) changes.get(2);

        Assertions.assertEquals("TableA", indexChange1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getIndex(0), indexChange1.findChangedIndex(model1, false));

        Assertions.assertEquals("TableA", colChange.getChangedTableName());
        Assertions.assertEquals("COL2", colChange.getChangedColumn());

        Assertions.assertEquals("TableA", indexChange2.getChangedTableName());
        assertIndex("TESTINDEX", true, new String[]{"COL1", "COL3"}, indexChange2.getNewIndex());
    }

    /**
     * Tests the removal of an index.
     */
    public void testDropIndex1() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Col' type='INTEGER'/>
                <unique name='TestIndex'>
                  <unique-column name='Col'/>
                </unique>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Col' type='INTEGER'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(true).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        RemoveIndexChange change = (RemoveIndexChange) changes.get(0);

        Assertions.assertEquals("TableA", change.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getIndex(0), change.findChangedIndex(model1, true));
    }

    /**
     * Tests the removal of an index.
     */
    public void testDropIndex2() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL2' type='DOUBLE'/>
                <column name='COL3' type='VARCHAR' size='32'/>
                <index name='TESTINDEX'>
                  <index-column name='COL3'/>
                  <index-column name='COL2'/>
                  <index-column name='COL1'/>
                </index>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TABLEA'>
                <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='COL1' type='INTEGER'/>
                <column name='COL2' type='DOUBLE'/>
                <column name='COL3' type='VARCHAR' size='32'/>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(false).getChanges(model1, model2);

        Assertions.assertEquals(1, changes.size());

        RemoveIndexChange indexChange = (RemoveIndexChange) changes.get(0);

        Assertions.assertEquals("TableA", indexChange.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getIndex(0), indexChange.findChangedIndex(model1, false));
    }

    /**
     * Tests the recreation of an index because of the change of type of the index.
     */
    public void testAddAndDropIndex() {
        final String MODEL1 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Col' type='INTEGER'/>
                <unique name='TestIndex'>
                  <unique-column name='Col'/>
                </unique>
              </table>
            </database>""";
        final String MODEL2 = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='TableA'>
                <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Col' type='INTEGER'/>
                <index name='TestIndex'>
                  <index-column name='Col'/>
                </index>
              </table>
            </database>""";

        Database model1 = parseDatabaseFromString(MODEL1);
        Database model2 = parseDatabaseFromString(MODEL2);
        List<ModelChange> changes = getPlatform(true).getChanges(model1, model2);

        Assertions.assertEquals(2, changes.size());

        RemoveIndexChange change1 = (RemoveIndexChange) changes.get(0);
        AddIndexChange change2 = (AddIndexChange) changes.get(1);

        Assertions.assertEquals("TableA", change1.getChangedTableName());
        Assertions.assertEquals(model1.findTable("TableA").getIndex(0), change1.findChangedIndex(model1, false));

        Assertions.assertEquals("TableA", change2.getChangedTableName());
        assertIndex("TestIndex", false, new String[]{"Col"}, change2.getNewIndex());
    }
}
