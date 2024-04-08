package org.apache.ddlutils.io;


import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.TestAgainstLiveDatabaseBase;
import org.apache.ddlutils.dynabean.TableObject;
import org.apache.ddlutils.model.CascadeActionEnum;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.DBTypeEnum;
import org.apache.ddlutils.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Performs the constraint tests.
 */
public class TestConstraints extends TestAgainstLiveDatabaseBase {

    /**
     * Tests a nullable column. Basically we're creating the test database
     * and then read it back and compare the original with the read one.
     * In addition, we can also check that DdlUtils does not try to alter the new
     * database when using the <code>alterTables</code>/<code>getAlterTablesSql</code>
     * methods of the {@link org.apache.ddlutils.Platform} with the read-back model.
     *
     * @param modelXml        The model to be tested in XML form
     * @param checkAlteration Whether to also check the alter tables sql
     */
    protected void performConstraintsTest(String modelXml, boolean checkAlteration) {
        createDatabase(modelXml);
        Database modelFromDb = readModelFromDatabase("roundtriptest");
        assertEquals(getAdjustedModel(), modelFromDb);
        if (checkAlteration) {
            String alterTablesSql = getAlterTablesSql(modelFromDb).trim();
            assertTrue(alterTablesSql.isEmpty());
        }
    }

    /**
     * Tests a table name that is longer than the maximum allowed.
     */
    @Test
    public void testLongTableName() {
        if (getSqlBuilder().getMaxTableNameLength() == -1) {
            return;
        }

        String tableName = StringUtils.repeat("Test", (getSqlBuilder().getMaxTableNameLength() / 4) + 3);
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='%s'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' required='false'/>
              </table>
            </database>""".formatted(tableName);

        performConstraintsTest(modelXml, true);
    }

    /**
     * Tests a column name that is longer than the maximum allowed.
     */
    @Test
    public void testLongColumnName() {
        if (getPlatformInfo().getMaxColumnNameLength() == -1) {
            return;
        }

        String columnName = StringUtils.repeat("Test", (getSqlBuilder().getMaxColumnNameLength() / 4) + 3);
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='lengthtest'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='%s' type='INTEGER' required='false'/>
              </table>
            </database>""".formatted(columnName);

        performConstraintsTest(modelXml, true);
    }

    /**
     * Tests a constraint name that is longer than the maximum allowed.
     */
    @Test
    public void testLongConstraintName() {
        if (getSqlBuilder().getMaxConstraintNameLength() == -1) {
            return;
        }

        String constraintName = StringUtils.repeat("Test", (getSqlBuilder().getMaxConstraintNameLength() / 4) + 3);
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='DOUBLE'/>
                <index name='%s'>
                  <index-column name='avalue'/>
                </index>
              </table>
            </database>""".formatted(constraintName);

        performConstraintsTest(modelXml, true);
    }

    /**
     * Tests a foreign key name that is longer than the maximum allowed.
     */
    @Test
    public void testLongForeignKeyName() {
        if (getSqlBuilder().getMaxForeignKeyNameLength() == -1) {
            return;
        }

        String fkName = StringUtils.repeat("Test", (getSqlBuilder().getMaxForeignKeyNameLength() / 4) + 3);
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip_1'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip_2'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' required='true'/>
                <foreign-key name='%s' foreignTable='roundtrip_1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""".formatted(fkName);

        performConstraintsTest(modelXml, true);
    }

    /**
     * Tests a nullable column.
     */
    @Test
    public void testNullableColumn() {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' required='false'/>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);
    }

    /**
     * Tests a not-nullable column.
     */
    @Test
    public void testNotNullableColumn() {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' required='true'/>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);
    }

    /**
     * Tests an auto-increment INTEGER column.
     */
    @Test
    public void testAutoIncrementIntegerColumn() {
        // only test this if the platform supports it
        if (!getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        // we need special catering for Sybase which does not support identity for INTEGER columns
        final String modelXml;

        if (DBTypeEnum.SYBASE.getName().equals(getPlatform().getName())) {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='NUMERIC' size='12,0' required='true' autoIncrement='true'/>
                  </table>
                </database>""";
        } else {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER' required='true' autoIncrement='true'/>
                  </table>
                </database>""";
        }

        performConstraintsTest(modelXml, getPlatformInfo().getIdentityStatusReadingSupported());
    }

    /**
     * Tests an auto-increment primary key column.
     */
    @Test
    public void testPrimaryKeyAutoIncrementColumn() {
        // we need special catering for Sybase which does not support identity for INTEGER columns
        final String modelXml;

        if (DBTypeEnum.SYBASE.getName().equals(getPlatform().getName())) {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='NUMERIC' size='12,0' primaryKey='true' required='true' autoIncrement='true'/>
                  </table>
                </database>""";
        } else {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
                  </table>
                </database>""";
        }

        performConstraintsTest(modelXml, getPlatformInfo().getIdentityStatusReadingSupported());
    }

    /**
     * Test for DDLUTILS-199.
     */
    @Test
    public void testAutoIncrementPrimaryKeyWithUnderscoreInName() {
        // we need special catering for Sybase which does not support identity for INTEGER columns
        final String modelXml;

        if (DBTypeEnum.SYBASE.getName().equals(getPlatform().getName())) {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='PK_Column' type='NUMERIC' size='12,0' primaryKey='true' required='true' autoIncrement='true'/>
                  </table>
                </database>""";
        } else {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='PK_Column' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
                  </table>
                </database>""";
        }

        performConstraintsTest(modelXml, getPlatformInfo().getIdentityStatusReadingSupported());
    }

    /**
     * Tests a simple index.
     */
    @Test
    public void testIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='DOUBLE'/>
                <index name='TEST_INDEX'>
                  <index-column name='avalue'/>
                </index>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);
    }

    /**
     * Tests an unique index for two columns.
     */
    @Test
    public void testUniqueIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value_1' type='DOUBLE'/>
                <column name='value_2' type='VARCHAR' size='32'/>
                <unique name='test_index'>
                  <unique-column name='value_2'/>
                  <unique-column name='value_1'/>
                </unique>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);
    }

    /**
     * Tests an index for two columns, one of which a pk column.
     */
    @Test
    public void testPrimaryKeyIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk_1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk_2' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='DOUBLE'/>
                <index name='test_index'>
                  <index-column name='avalue'/>
                  <index-column name='pk_1'/>
                </index>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);
    }

    /**
     * Tests two tables with a simple foreign key relationship between them.
     */
    @Test
    public void testSimpleForeignKey() {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip_1'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip_2'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' required='true'/>
                <foreign-key foreignTable='roundtrip_1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);
    }

    /**
     * Tests two tables with overlapping foreign key relationships between them.
     */
    @Test
    public void testOverlappingForeignKeys() {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip_1'>
                <column name='pk_1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk_2' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip_2'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='value_1' type='INTEGER' required='true'/>
                <column name='value_2' type='INTEGER'/>
                <column name='value_3' type='VARCHAR' size='32'/>
                <foreign-key name='fk_1' foreignTable='roundtrip_1'>
                  <reference local='value_1' foreign='pk_1'/>
                  <reference local='value_3' foreign='pk_2'/>
                </foreign-key>
                <foreign-key foreignTable='roundtrip_1'>
                  <reference local='value_2' foreign='pk_1'/>
                  <reference local='value_3' foreign='pk_2'/>
                </foreign-key>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);
    }

    /**
     * Tests two tables with circular foreign key relationships between them.
     */
    @Test
    public void testCircularForeignKeys() {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip_1'>
                <column name='pk_1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk_2' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='value_1' type='INTEGER'/>
                <column name='value_2' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='roundtrip_2'>
                  <reference local='value_1' foreign='pk_1'/>
                  <reference local='value_2' foreign='pk_2'/>
                </foreign-key>
              </table>
              <table name='roundtrip_2'>
                <column name='pk_1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk_2' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='value_1' type='VARCHAR' size='32' required='true'/>
                <column name='value_2' type='INTEGER' required='true'/>
                <foreign-key foreignTable='roundtrip_1'>
                  <reference local='value_2' foreign='pk_1'/>
                  <reference local='value_1' foreign='pk_2'/>
                </foreign-key>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);
    }

    /**
     * Tests two tables with a foreign key with a restrict onDelete action.
     */
    @Test
    public void testForeignKeyWithOnDeleteRestrict() {
        if (!getPlatformInfo().isActionSupportedForOnDelete(CascadeActionEnum.RESTRICT)) {
            return;
        }

        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip_1'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' required='true'/>
                <foreign-key foreignTable='roundtrip_1' onDelete='restrict'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);

        insertRow("roundtrip_1", new Object[]{(1)});
        insertRow("roundtrip_2", new Object[]{(5), (1)});

        List<TableObject> beansTable1 = getRows("roundtrip_1");
        List<TableObject> beansTable2 = getRows("roundtrip_2");

        assertEquals(1, beansTable1.size());
        assertEquals(1, beansTable2.size());
        assertEquals((1), beansTable1.get(0), "pk");
        assertEquals((5), beansTable2.get(0), "pk");
        assertEquals((1), beansTable2.get(0), "avalue");

        try {
            deleteRow("roundtrip_1", new Object[]{(1)});
            Assertions.fail();
        } catch (DdlUtilsException ignored) {
        }
    }

    /**
     * Tests two tables with a foreign key with a cascade onDelete action.
     */
    @Test
    public void testForeignKeyWithOnDeleteCascade() {
        if (!getPlatformInfo().isActionSupportedForOnDelete(CascadeActionEnum.CASCADE)) {
            return;
        }

        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip_1'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' required='true'/>
                <foreign-key foreignTable='roundtrip_1' onDelete='cascade'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);

        insertRow("roundtrip_1", new Object[]{(1)});
        insertRow("roundtrip_2", new Object[]{(5), (1)});

        List<TableObject> beansTable1 = getRows("roundtrip_1");
        List<TableObject> beansTable2 = getRows("roundtrip_2");

        assertEquals(1, beansTable1.size());
        assertEquals(1, beansTable2.size());
        assertEquals((1), beansTable1.get(0), "pk");
        assertEquals((5), beansTable2.get(0), "pk");
        assertEquals((1), beansTable2.get(0), "avalue");

        deleteRow("roundtrip_1", new Object[]{(1)});

        beansTable1 = getRows("roundtrip_1");
        beansTable2 = getRows("roundtrip_2");

        assertEquals(0, beansTable1.size());
        assertEquals(0, beansTable2.size());
    }

    /**
     * Tests two tables with a foreign key with a set-null onDelete action.
     */
    @Test
    public void testForeignKeyWithOnDeleteSetNull() {
        if (!getPlatformInfo().isActionSupportedForOnDelete(CascadeActionEnum.SET_NULL)) {
            return;
        }

        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip_1'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' required='false'/>
                <foreign-key foreignTable='roundtrip_1' onDelete='setnull'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);

        insertRow("roundtrip_1", new Object[]{(1)});
        insertRow("roundtrip_2", new Object[]{(5), (1)});

        List<TableObject> beansTable1 = getRows("roundtrip_1");
        List<TableObject> beansTable2 = getRows("roundtrip_2");

        assertEquals(1, beansTable1.size());
        assertEquals(1, beansTable2.size());
        assertEquals((1), beansTable1.get(0), "pk");
        assertEquals((5), beansTable2.get(0), "pk");
        assertEquals((1), beansTable2.get(0), "avalue");

        deleteRow("roundtrip_1", new Object[]{(1)});

        beansTable1 = getRows("roundtrip_1");
        beansTable2 = getRows("roundtrip_2");

        assertEquals(0, beansTable1.size());
        assertEquals(1, beansTable2.size());
        assertEquals((5), beansTable2.get(0), "pk");
        assertEquals((Object) null, beansTable2.get(0), "avalue");
    }

    /**
     * Tests two tables with a foreign key with a set-default onDelete action.
     */
    @Test
    public void testForeignKeyWithOnDeleteSetDefault() {
        if (!getPlatformInfo().isActionSupportedForOnDelete(CascadeActionEnum.SET_DEFAULT)) {
            return;
        }

        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip_1'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' required='false' default='2'/>
                <foreign-key foreignTable='roundtrip_1' onDelete='setdefault'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);

        insertRow("roundtrip_1", new Object[]{(1)});
        insertRow("roundtrip_1", new Object[]{(2)});
        insertRow("roundtrip_2", new Object[]{(5), (1)});

        List<TableObject> beansTable1 = getRows("roundtrip_1");
        List<TableObject> beansTable2 = getRows("roundtrip_2");

        assertEquals(2, beansTable1.size());
        assertEquals(1, beansTable2.size());
        assertEquals((1), beansTable1.get(0), "pk");
        assertEquals((2), beansTable1.get(1), "pk");
        assertEquals((5), beansTable2.get(0), "pk");
        assertEquals((1), beansTable2.get(0), "avalue");

        deleteRow("roundtrip_1", new Object[]{(1)});

        beansTable1 = getRows("roundtrip_1");
        beansTable2 = getRows("roundtrip_2");

        assertEquals(1, beansTable1.size());
        assertEquals(1, beansTable2.size());
        assertEquals((2), beansTable1.get(0), "pk");
        assertEquals((5), beansTable2.get(0), "pk");
        assertEquals((2), beansTable2.get(0), "avalue");
    }

    /**
     * Tests two tables with a foreign key with a restrict onUpdate action.
     */
    @Test
    public void testForeignKeyWithOnUpdateRestrict() {
        if (!getPlatformInfo().isActionSupportedForOnUpdate(CascadeActionEnum.RESTRICT)) {
            return;
        }

        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip_1'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' required='true'/>
                <foreign-key foreignTable='roundtrip_1' onUpdate='restrict'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);

        insertRow("roundtrip_1", new Object[]{(1)});
        insertRow("roundtrip_2", new Object[]{(5), (1)});

        List<TableObject> beansTable1 = getRows("roundtrip_1");
        List<TableObject> beansTable2 = getRows("roundtrip_2");

        assertEquals(1, beansTable1.size());
        assertEquals(1, beansTable2.size());
        assertEquals((1), beansTable1.get(0), "pk");
        assertEquals((5), beansTable2.get(0), "pk");
        assertEquals((1), beansTable2.get(0), "avalue");

        try {
            updateRow("roundtrip_1", beansTable1.get(0), new Object[]{(5)});
            Assertions.fail();
        } catch (DdlUtilsException ignored) {
        }
    }

    /**
     * Tests two tables with a foreign key with a cascade onUpdate action.
     */
    @Test
    public void testForeignKeyWithOnUpdateCascade() {
        if (!getPlatformInfo().isActionSupportedForOnUpdate(CascadeActionEnum.CASCADE)) {
            return;
        }

        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip_1'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' required='true'/>
                <foreign-key foreignTable='roundtrip_1' onUpdate='cascade'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);

        insertRow("roundtrip_1", new Object[]{(1)});
        insertRow("roundtrip_2", new Object[]{(5), (1)});

        List<TableObject> beansTable1 = getRows("roundtrip_1");
        List<TableObject> beansTable2 = getRows("roundtrip_2");

        assertEquals(1, beansTable1.size());
        assertEquals(1, beansTable2.size());
        assertEquals((1), beansTable1.get(0), "pk");
        assertEquals((5), beansTable2.get(0), "pk");
        assertEquals((1), beansTable2.get(0), "avalue");

        updateRow("roundtrip_1", beansTable1.get(0), new Object[]{(2)});

        beansTable1 = getRows("roundtrip_1");
        beansTable2 = getRows("roundtrip_2");

        assertEquals(1, beansTable1.size());
        assertEquals(1, beansTable2.size());
        assertEquals((2), beansTable1.get(0), "pk");
        assertEquals((5), beansTable2.get(0), "pk");
        assertEquals((2), beansTable2.get(0), "avalue");
    }

    /**
     * Tests two tables with a foreign key with a set-null onUpdate action.
     */
    @Test
    public void testForeignKeyWithOnUpdateSetNull() {
        if (!getPlatformInfo().isActionSupportedForOnUpdate(CascadeActionEnum.SET_NULL)) {
            return;
        }

        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip_1'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' required='false'/>
                <foreign-key foreignTable='roundtrip_1' onUpdate='setnull'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);

        insertRow("roundtrip_1", new Object[]{(1)});
        insertRow("roundtrip_2", new Object[]{(5), (1)});

        List<TableObject> beansTable1 = getRows("roundtrip_1");
        List<TableObject> beansTable2 = getRows("roundtrip_2");

        assertEquals(1, beansTable1.size());
        assertEquals(1, beansTable2.size());
        assertEquals((1), beansTable1.get(0), "pk");
        assertEquals((5), beansTable2.get(0), "pk");
        assertEquals((1), beansTable2.get(0), "avalue");

        updateRow("roundtrip_1", beansTable1.get(0), new Object[]{(2)});

        beansTable1 = getRows("roundtrip_1");
        beansTable2 = getRows("roundtrip_2");

        assertEquals(1, beansTable1.size());
        assertEquals(1, beansTable2.size());
        assertEquals((2), beansTable1.get(0), "pk");
        assertEquals((5), beansTable2.get(0), "pk");
        assertEquals((Object) null, beansTable2.get(0), "avalue");
    }

    /**
     * Tests two tables with a foreign key with a det-default onUpdate action.
     */
    @Test
    public void testForeignKeyWithOnUpdateSetDefault() {
        if (!getPlatformInfo().isActionSupportedForOnUpdate(CascadeActionEnum.SET_DEFAULT)) {
            return;
        }

        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip_1'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' required='false' default='1'/>
                <foreign-key foreignTable='roundtrip_1' onUpdate='setdefault'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        performConstraintsTest(modelXml, true);

        insertRow("roundtrip_1", new Object[]{(1)});
        insertRow("roundtrip_1", new Object[]{(2)});
        insertRow("roundtrip_2", new Object[]{(5), (2)});

        List<TableObject> beansTable1 = getRows("roundtrip_1");
        List<TableObject> beansTable2 = getRows("roundtrip_2");

        assertEquals(2, beansTable1.size());
        assertEquals(1, beansTable2.size());
        assertEquals((1), beansTable1.get(0), "pk");
        assertEquals((2), beansTable1.get(1), "pk");
        assertEquals((5), beansTable2.get(0), "pk");
        assertEquals((2), beansTable2.get(0), "avalue");

        updateRow("roundtrip_1", beansTable1.get(1), new Object[]{(0)});

        beansTable1 = getRows("roundtrip_1", "pk");
        beansTable2 = getRows("roundtrip_2", "pk");

        assertEquals(2, beansTable1.size());
        assertEquals(1, beansTable2.size());
        assertEquals((0), beansTable1.get(0), "pk");
        assertEquals((1), beansTable1.get(1), "pk");
        assertEquals((5), beansTable2.get(0), "pk");
        assertEquals((1), beansTable2.get(0), "avalue");
    }
}
