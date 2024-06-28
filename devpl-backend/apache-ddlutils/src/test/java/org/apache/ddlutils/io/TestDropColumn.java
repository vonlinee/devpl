package org.apache.ddlutils.io;

import junit.framework.Test;
import org.apache.ddlutils.TestAgainstLiveDatabaseBase;
import org.apache.ddlutils.model.TableRow;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * Tests database alterations that drop columns.
 */
public class TestDropColumn extends TestAgainstLiveDatabaseBase {
    /**
     * Parameterized test case pattern.
     *
     * @return The tests
     */
    public static Test suite() throws Exception {
        return getTests(TestDropColumn.class);
    }

    /**
     * Tests the removal of a column.
     */
    public void testDropColumn() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='TIMESTAMP'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), new Date(new java.util.Date().getTime())});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> rows = getRows("roundtrip");

        assertEquals((1), rows.get(0), "pk");
    }

    /**
     * Tests the removal of a auto increment column.
     */
    public void testDropAutoIncrementColumn() {
        if (!getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER' autoIncrement='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> rows = getRows("roundtrip");

        assertEquals((1), rows.get(0), "pk");
    }

    /**
     * Tests the removal of a required column.
     */
    public void testDropRequiredColumn() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='NUMERIC' size='12,0' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), new BigDecimal(2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> rows = getRows("roundtrip");

        assertEquals((1), rows.get(0), "pk");
    }

    /**
     * Tests the removal of a column that has a default value.
     */
    public void testDropColumnWithDefault() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='DOUBLE' default='3.1'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> rows = getRows("roundtrip");

        assertEquals((1), rows.get(0), "pk");
    }

    /**
     * Tests the removal of a required column that has a default value.
     */
    public void testDropRequiredColumnWithDefault() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='CHAR' size='8' required='true' default='text'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> rows = getRows("roundtrip");

        assertEquals((1), rows.get(0), "pk");
    }

    /**
     * Tests the removal of multiple columns.
     */
    public void testDropMultipleColumns() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue1' type='CHAR' size='8' default='text'/>
                    <column name='avalue2' type='INTEGER'/>
                    <column name='avalue3' type='DOUBLE' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue2' type='INTEGER'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), null, (2), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> rows = getRows("roundtrip");

        assertEquals((1), rows.get(0), "pk");
        assertEquals((2), rows.get(0), "avalue2");
    }

    /**
     * Tests the removal of multiple columns, including one with auto incremen.
     */
    public void testDropMultipleColumnsInclAutoIncrement() {
        if (!getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue1' type='CHAR' size='8' default='text'/>
                    <column name='avalue2' type='DOUBLE' required='true'/>
                    <column name='avalue3' type='INTEGER' autoIncrement='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue2' type='DOUBLE' required='true'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), null, (2), null});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> rows = getRows("roundtrip");

        assertEquals((1), rows.get(0), "pk");
        assertEquals((2), rows.get(0), "avalue2");
    }

    /**
     * Tests the removal of a primary key column.
     */
    public void testDropPKColumn() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='pk2' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk2' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2), (3)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> rows = getRows("roundtrip");

        assertEquals((2), rows.get(0), "pk2");
        assertEquals((3), rows.get(0), "avalue");
    }

    /**
     * Tests the removal of the single primary key column.
     */
    public void testDropSinglePKColumn() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> rows = getRows("roundtrip");

        assertEquals((2), rows.get(0), "avalue");
    }

    /**
     * Tests the removal of multiple primary key columns.
     */
    public void testDropMultiplePKColumns() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='pk2' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='pk3' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk2' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2), (3), (4)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> rows = getRows("roundtrip");

        assertEquals((2), rows.get(0), "pk2");
        assertEquals((4), rows.get(0), "avalue");
    }

    /**
     * Tests the removal of all primary key columns.
     */
    public void testDropAllPKColumns() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='pk2' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='pk3' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2), (3), (4)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> rows = getRows("roundtrip");

        assertEquals((4), rows.get(0), "avalue");
    }

    /**
     * Tests the removal of a column from a non-unique index.
     */
    public void testDropColumnFromIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue1' type='INTEGER'/>
                    <column name='avalue2' type='VARCHAR' size='32'/>
                    <index name='test'>
                      <index-column name='avalue1'/>
                      <index-column name='avalue2'/>
                    </index>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue2' type='VARCHAR' size='32'/>
                    <index name='test'>
                      <index-column name='avalue2'/>
                    </index>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2), "text"});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> rows = getRows("roundtrip");

        assertEquals((1), rows.get(0), "pk");
        assertEquals((Object) "text", rows.get(0), "avalue2");
    }

    /**
     * Tests the removal of the single column from an unique index.
     */
    public void testDropSingleColumnFromIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                    <unique name='test'>
                      <unique-column name='avalue'/>
                    </unique>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> rows = getRows("roundtrip");

        assertEquals((1), rows.get(0), "pk");
    }

    /**
     * Tests the removal of a column from a non-unique index.
     */
    public void testDropMultipleColumnsFromIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue1' type='INTEGER'/>
                    <column name='avalue2' type='VARCHAR' size='32'/>
                    <column name='avalue3' type='TIMESTAMP'/>
                    <index name='test'>
                      <index-column name='avalue1'/>
                      <index-column name='avalue2'/>
                      <index-column name='avalue3'/>
                    </index>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue2' type='VARCHAR' size='32'/>
                    <index name='test'>
                      <index-column name='avalue2'/>
                    </index>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2), "text"});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> rows = getRows("roundtrip");

        assertEquals((1), rows.get(0), "pk");
        assertEquals((Object) "text", rows.get(0), "avalue2");
    }

    /**
     * Tests the removal of all column from an unique index.
     */
    public void testDropAllColumnsFromIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue1' type='INTEGER' required='true'/>
                    <column name='avalue2' type='VARCHAR' size='32' required='true'/>
                    <column name='avalue3' type='DOUBLE' required='true'/>
                    <unique name='test'>
                      <unique-column name='avalue1'/>
                      <unique-column name='avalue2'/>
                      <unique-column name='avalue3'/>
                    </unique>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2), "text", (2.0)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> rows = getRows("roundtrip");

        assertEquals((1), rows.get(0), "pk");
    }

    /**
     * Tests the removal of the single local column from a foreign key.
     */
    public void testDropSingleLocalColumnFromFK() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip1'>
                    <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                  </table>
                  <table name='roundtrip2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='VARCHAR' size='32'/>
                    <foreign-key foreignTable='roundtrip1'>
                      <reference local='avalue' foreign='pk'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip1'>
                    <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                  </table>
                  <table name='roundtrip2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{"text"});
        insertRow("roundtrip2", new Object[]{(1), "text"});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> beans1 = getRows("roundtrip1");
        List<TableRow> beans2 = getRows("roundtrip2");

        assertEquals((Object) "text", beans1.get(0), "pk");
        assertEquals((1), beans2.get(0), "pk");
    }

    /**
     * Tests the removal of the single foreign column from a foreign key.
     */
    public void testDropSingleForeignColumnFromFK() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip1'>
                    <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                  <table name='roundtrip2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='VARCHAR' size='32'/>
                    <foreign-key foreignTable='roundtrip1'>
                      <reference local='avalue' foreign='pk'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip1'>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                  <table name='roundtrip2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='VARCHAR' size='32'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{"text", (2)});
        insertRow("roundtrip2", new Object[]{(1), "text"});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> beans1 = getRows("roundtrip1");
        List<TableRow> beans2 = getRows("roundtrip2");

        assertEquals((2), beans1.get(0), "avalue");
        assertEquals((1), beans2.get(0), "pk");
        assertEquals((Object) "text", beans2.get(0), "avalue");
    }

    /**
     * Tests the removal of all local columns from a foreign key.
     */
    public void testDropAllLocalColumnsFromFK() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip1'>
                    <column name='pk1' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                    <column name='pk2' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='roundtrip2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue1' type='VARCHAR' size='32'/>
                    <column name='avalue2' type='INTEGER'/>
                    <foreign-key foreignTable='roundtrip1'>
                      <reference local='avalue1' foreign='pk1'/>
                      <reference local='avalue2' foreign='pk2'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip1'>
                    <column name='pk1' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                    <column name='pk2' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='roundtrip2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{"text", (2)});
        insertRow("roundtrip2", new Object[]{(1), "text", (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> beans1 = getRows("roundtrip1");
        List<TableRow> beans2 = getRows("roundtrip2");

        assertEquals((Object) "text", beans1.get(0), "pk1");
        assertEquals((2), beans1.get(0), "pk2");
        assertEquals((1), beans2.get(0), "pk");
    }

    /**
     * Tests the removal of all foreign columns from a foreign key.
     */
    public void testDropAllForeignColumnsFromFK() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip1'>
                    <column name='pk1' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                    <column name='pk2' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                  <table name='roundtrip2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue1' type='VARCHAR' size='32'/>
                    <column name='avalue2' type='INTEGER'/>
                    <foreign-key foreignTable='roundtrip1'>
                      <reference local='avalue1' foreign='pk1'/>
                      <reference local='avalue2' foreign='pk2'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip1'>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                  <table name='roundtrip2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue1' type='VARCHAR' size='32'/>
                    <column name='avalue2' type='INTEGER'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{"text", (2), (3)});
        insertRow("roundtrip2", new Object[]{(1), "text", (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> beans1 = getRows("roundtrip1");
        List<TableRow> beans2 = getRows("roundtrip2");

        assertEquals((3), beans1.get(0), "avalue");
        assertEquals((1), beans2.get(0), "pk");
        assertEquals((Object) "text", beans2.get(0), "avalue1");
        assertEquals((2), beans2.get(0), "avalue2");
    }

    /**
     * Tests the removal of a local and foreign column from a foreign key.
     */
    public void testDropLocalAndForeignColumnFromFK() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip1'>
                    <column name='pk1' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                    <column name='pk2' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                  <table name='roundtrip2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue1' type='VARCHAR' size='32'/>
                    <column name='avalue2' type='INTEGER'/>
                    <foreign-key foreignTable='roundtrip1'>
                      <reference local='avalue1' foreign='pk1'/>
                      <reference local='avalue2' foreign='pk2'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip1'>
                    <column name='pk2' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                  <table name='roundtrip2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue2' type='INTEGER'/>
                    <foreign-key foreignTable='roundtrip1'>
                      <reference local='avalue2' foreign='pk2'/>
                    </foreign-key>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{"text", (2), (3)});
        insertRow("roundtrip2", new Object[]{(1), "text", (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> beans1 = getRows("roundtrip1");
        List<TableRow> beans2 = getRows("roundtrip2");

        assertEquals((2), beans1.get(0), "pk2");
        assertEquals((3), beans1.get(0), "avalue");
        assertEquals((1), beans2.get(0), "pk");
        assertEquals((2), beans2.get(0), "avalue2");
    }

    /**
     * Tests the removal of multiple local and foreign columns from a foreign key.
     */
    public void testDropMultipleLocalAndForeignColumnsFromFK() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip1'>
                    <column name='pk1' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                    <column name='pk2' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='pk3' type='DOUBLE' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                  <table name='roundtrip2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue1' type='VARCHAR' size='32'/>
                    <column name='avalue2' type='INTEGER'/>
                    <column name='avalue3' type='DOUBLE'/>
                    <foreign-key foreignTable='roundtrip1'>
                      <reference local='avalue1' foreign='pk1'/>
                      <reference local='avalue2' foreign='pk2'/>
                      <reference local='avalue3' foreign='pk3'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip1'>
                    <column name='pk2' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                  <table name='roundtrip2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue2' type='INTEGER'/>
                    <foreign-key foreignTable='roundtrip1'>
                      <reference local='avalue2' foreign='pk2'/>
                    </foreign-key>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{"text", (2), (4), (3)});
        insertRow("roundtrip2", new Object[]{(1), "text", (2), (4)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List<TableRow> beans1 = getRows("roundtrip1");
        List<TableRow> beans2 = getRows("roundtrip2");

        assertEquals((2), beans1.get(0), "pk2");
        assertEquals((3), beans1.get(0), "avalue");
        assertEquals((1), beans2.get(0), "pk");
        assertEquals((2), beans2.get(0), "avalue2");
    }

    /**
     * Tests the removal of all local and foreign columns from a foreign key.
     */
    public void testDropAllLocalAndForeignColumnsFromFK() {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip1'>
                    <column name='pk1' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                    <column name='pk2' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='pk3' type='DOUBLE' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                  <table name='roundtrip2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue1' type='VARCHAR' size='32'/>
                    <column name='avalue2' type='INTEGER'/>
                    <column name='avalue3' type='DOUBLE'/>
                    <foreign-key foreignTable='roundtrip1'>
                      <reference local='avalue1' foreign='pk1'/>
                      <reference local='avalue2' foreign='pk2'/>
                      <reference local='avalue3' foreign='pk3'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip1'>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                  <table name='roundtrip2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{"text", (2), (4), (3)});
        insertRow("roundtrip2", new Object[]{(1), "text", (2), (4)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(),
            readModelFromDatabase("roundtriptest"));

        List beans1 = getRows("roundtrip1");
        List beans2 = getRows("roundtrip2");

        assertEquals((3), beans1.get(0), "avalue");
        assertEquals((1), beans2.get(0), "pk");
    }
}
