package org.apache.ddlutils.io;


import junit.framework.Test;
import org.apache.ddlutils.TestAgainstLiveDatabaseBase;
import org.apache.ddlutils.dynabean.SqlDynaBean;
import org.apache.ddlutils.platform.DBTypeEnum;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests database alterations that add columns.
 */
public class TestAddColumn extends TestAgainstLiveDatabaseBase {
    /**
     * Parameterized test case pattern.
     *
     * @return The tests
     */
    public static Test suite() throws Exception {
        return getTests(TestAddColumn.class);
    }

    /**
     * Tests the addition of a column.
     */
    public void testAddColumn() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List<SqlDynaBean> beans = getRows("roundtrip");

        assertEquals((Object) null, beans.get(0), "avalue");
    }

    /**
     * Tests the addition of an auto-increment column.
     */
    public void testAddAutoIncrementColumn() {
        if (!getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        // we need special catering for Sybase which does not support identity for INTEGER columns
        boolean isSybase = DBTypeEnum.SYBASE.getName().equals(getPlatform().getName());
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml;

        if (isSybase) {
            model2Xml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='NUMERIC' size='12,0' autoIncrement='true'/>
                  </table>
                </database>""";
        } else {
            model2Xml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER' autoIncrement='true'/>
                  </table>
                </database>""";
        }

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        if (isSybase) {
            assertEquals(new BigDecimal(1), beans.get(0), "avalue");
        } else {
            Object avalue = ((SqlDynaBean) beans.get(0)).get("avalue");

            assertTrue((avalue == null) || Objects.equals(1, avalue));
        }
    }

    /**
     * Tests the addition of another auto-increment column.
     */
    public void testAddSecondAutoIncrementColumn() {
        if (!getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported() || !getPlatformInfo().isMultipleIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' autoIncrement='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' autoIncrement='true'/>
                <column name='avalue2' type='INTEGER' autoIncrement='true'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");
        Object avalue1 = ((SqlDynaBean) beans.get(0)).get("avalue1");
        Object avalue2 = ((SqlDynaBean) beans.get(0)).get("avalue2");

        assertTrue((avalue1 == null) || Objects.equals(1, avalue1));
        assertTrue((avalue2 == null) || Objects.equals(1, avalue2));
    }

    /**
     * Tests the addition of a column that is set to NOT NULL.
     */
    public void testAddRequiredColumn() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='NUMERIC' size='12,0' default='2' required='true'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertEquals(new BigDecimal(2), beans.get(0), "avalue");
    }

    /**
     * Tests the addition of a column with a default value.
     */
    public void testAddColumnWithDefault() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='DOUBLE' default='2'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List<SqlDynaBean> beans = getRows("roundtrip");
        Object avalue = beans.get(0).get("avalue");

        assertTrue((avalue == null) || Double.valueOf(2).equals(avalue));
    }

    /**
     * Tests the addition of a required auto-increment column.
     */
    public void testAddRequiredAutoIncrementColumn() {
        if (!getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        // we need special catering for Sybase which does not support identity for INTEGER columns
        boolean isSybase = DBTypeEnum.SYBASE.getName().equals(getPlatform().getName());
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml;

        if (isSybase) {
            model2Xml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='NUMERIC' size='12,0' autoIncrement='true' required='true'/>
                  </table>
                </database>""";
        } else {
            model2Xml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER' autoIncrement='true' required='true'/>
                  </table>
                </database>""";
        }

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        if (isSybase) {
            assertEquals(new BigDecimal(1), beans.get(0), "avalue");
        } else {
            Object avalue = ((SqlDynaBean) beans.get(0)).get("avalue");

            assertTrue((avalue == null) || Objects.equals(1, avalue));
        }
    }

    /**
     * Tests the addition of a column with a default value.
     */
    public void testAddRequiredColumnWithDefault() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='CHAR' size='8' default='sometext' required='true'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");
        Object avalue = ((SqlDynaBean) beans.get(0)).get("avalue");

        assertTrue((avalue == null) || "sometext".equals(avalue));
    }

    /**
     * Tests the addition of several columns at the end of the table.
     */
    public void testAddMultipleColumns() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='VARCHAR' size='32'/>
                <column name='avalue2' type='INTEGER'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='VARCHAR' size='32'/>
                <column name='avalue2' type='INTEGER'/>
                <column name='avalue3' type='DOUBLE' default='1.0'/>
                <column name='avalue4' type='VARCHAR' size='16'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), "test", (3)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");
        Object avalue3 = ((SqlDynaBean) beans.get(0)).get("avalue3");

        assertEquals((Object) "test", beans.get(0), "avalue1");
        assertEquals((3), beans.get(0), "avalue2");
        assertTrue((avalue3 == null) || Double.valueOf(1.0).equals(avalue3));
        assertEquals((Object) null, beans.get(0), "avalue4");
    }

    /**
     * Tests the addition of a primary key and a column.
     */
    public void testAddPKAndColumn() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='avalue' type='INTEGER'/>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        if (DBTypeEnum.MCKOI.getName().equals(getPlatform().getName())) {
            // Mckoi uses null to initialize the new pk column
            assertEquals((Object) null, beans.get(0), "pk");
            assertEquals((1), beans.get(0), "avalue");
        } else {
            assertTrue(beans.isEmpty());
        }
    }

    /**
     * Tests the addition of a primary key and an autoincrement column.
     */
    public void testAddPKAndAutoIncrementColumn() {
        // we need special catering for Sybase which does not support identity for INTEGER columns
        boolean isSybase = DBTypeEnum.SYBASE.getName().equals(getPlatform().getName());
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";
        final String model2Xml;

        if (isSybase) {
            model2Xml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='avalue' type='INTEGER'/>
                    <column name='pk' type='NUMERIC' size='12,0' primaryKey='true' required='true' autoIncrement='true'/>
                  </table>
                </database>""";
        } else {
            model2Xml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='avalue' type='INTEGER'/>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
                  </table>
                </database>""";
        }

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List<SqlDynaBean> beans = getRows("roundtrip");

        assertEquals((1), beans.get(0), "avalue");
        if (isSybase) {
            assertEquals(new BigDecimal(1), beans.get(0), "pk");
        } else {
            assertEquals((1), beans.get(0), "pk");
        }
    }

    /**
     * Tests the addition of a primary key and multiple columns.
     */
    public void testAddPKAndMultipleColumns() {
        if (getPlatformInfo().isPrimaryKeyColumnsHaveToBeRequired()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='avalue' type='INTEGER'/>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk2' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='pk3' type='DOUBLE' primaryKey='true' default='2'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        if (DBTypeEnum.MCKOI.getName().equals(getPlatform().getName())) {
            assertEquals((Object) null, beans.get(0), "pk1");
            assertEquals((Object) null, beans.get(0), "pk2");
            assertEquals(2.0, beans.get(0), "pk3");
            assertEquals((1), beans.get(0), "avalue");
        } else {
            assertTrue(beans.isEmpty());
        }
    }

    /**
     * Tests the addition of a primary key and multiple required columns.
     */
    public void testAddPKAndMultipleRequiredColumns() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='avalue' type='INTEGER'/>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk2' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='pk3' type='DOUBLE' primaryKey='true' required='true' default='2'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        if (DBTypeEnum.MCKOI.getName().equals(getPlatform().getName())) {
            assertEquals((Object) null, beans.get(0), "pk1");
            assertEquals((Object) null, beans.get(0), "pk2");
            assertEquals(2.0, beans.get(0), "pk3");
            assertEquals((1), beans.get(0), "avalue");
        } else {
            assertTrue(beans.isEmpty());
        }
    }

    /**
     * Tests the addition of a primary key and multiple columns.
     */
    public void testAddPKAndMultipleColumnsInclAutoIncrement() {
        if (!getPlatformInfo().isMixingIdentityAndNormalPrimaryKeyColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='avalue' type='INTEGER'/>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
                <column name='pk2' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='pk3' type='DOUBLE' primaryKey='true' required='true'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        if (DBTypeEnum.MCKOI.getName().equals(getPlatform().getName())) {
            assertEquals((1), beans.get(0), "pk1");
            assertEquals((Object) null, beans.get(0), "pk2");
            assertEquals((Object) null, beans.get(0), "pk3");
            assertEquals((1), beans.get(0), "avalue");
        } else {
            assertTrue(beans.isEmpty());
        }
    }

    /**
     * Tests the addition of a column to a primary key.
     */
    public void testAddColumnIntoPK() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <column name='pk2' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        if (DBTypeEnum.MCKOI.getName().equals(getPlatform().getName())) {
            assertEquals((1), beans.get(0), "pk1");
            assertEquals((Object) null, beans.get(0), "pk2");
            assertEquals((2), beans.get(0), "avalue");
        } else {
            assertTrue(beans.isEmpty());
        }
    }

    /**
     * Tests the addition of an autoincrement column into the primary key.
     */
    public void testAddAutoIncrementColumnIntoPK() {
        if (!getPlatformInfo().isMixingIdentityAndNormalPrimaryKeyColumnsSupported()) {
            return;
        }

        // we need special catering for Sybase which does not support identity for INTEGER columns
        boolean isSybase = DBTypeEnum.SYBASE.getName().equals(getPlatform().getName());
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";
        final String model2Xml;

        if (isSybase) {
            model2Xml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                    <column name='pk2' type='NUMERIC' size='12,0' primaryKey='true' required='true' autoIncrement='true'/>
                  </table>
                </database>""";
        } else {
            model2Xml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                    <column name='pk2' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
                  </table>
                </database>""";
        }

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(-1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertEquals((-1), ((SqlDynaBean) beans.get(0)).get("pk1"));
        assertEquals((2), ((SqlDynaBean) beans.get(0)).get("avalue"));
        if (isSybase) {
            assertEquals(new BigDecimal(1), beans.get(0), "pk2");
        } else {
            assertEquals((1), beans.get(0), "pk2");
        }
    }

    /**
     * Tests the addition of multiple columns into the primary key.
     */
    public void testAddMultipleColumnsIntoPK() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <column name='pk2' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='pk3' type='DOUBLE' primaryKey='true' required='true'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        if (DBTypeEnum.MCKOI.getName().equals(getPlatform().getName())) {
            assertEquals((1), beans.get(0), "pk1");
            assertEquals((Object) null, beans.get(0), "pk2");
            assertEquals((Object) null, beans.get(0), "pk3");
            assertEquals((Object) null, beans.get(0), "avalue");
        } else {
            assertTrue(beans.isEmpty());
        }
    }

    /**
     * Tests the addition of multiple columns into the primary key which has an auto increment column.
     */
    public void testAddMultipleColumnsIntoPKWithAutoIncrement() {
        if (!getPlatformInfo().isMixingIdentityAndNormalPrimaryKeyColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
                <column name='avalue' type='INTEGER'/>
                <column name='pk2' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='pk3' type='DOUBLE' primaryKey='true' required='true'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{null, (1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        if (DBTypeEnum.MCKOI.getName().equals(getPlatform().getName())) {
            assertEquals((1), beans.get(0), "pk1");
            assertEquals((Object) null, beans.get(0), "pk2");
            assertEquals((Object) null, beans.get(0), "pk3");
            assertEquals((1), beans.get(0), "avalue");
        } else {
            assertTrue(beans.isEmpty());
        }
    }

    /**
     * Tests the addition of multiple columns including one with auto increment into the primary key.
     */
    public void testAddMultipleColumnsInclAutoIncrementIntoPK() {
        if (!getPlatformInfo().isMixingIdentityAndNormalPrimaryKeyColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='avalue' type='INTEGER'/>
                <column name='pk2' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
                <column name='avalue' type='INTEGER'/>
                <column name='pk2' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='pk3' type='DOUBLE' primaryKey='true' required='true'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), "text"});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        if (DBTypeEnum.MCKOI.getName().equals(getPlatform().getName())) {
            assertEquals((1), beans.get(0), "pk1");
            assertEquals((Object) "text", beans.get(0), "pk2");
            assertEquals((Object) null, beans.get(0), "pk3");
            assertEquals((1), beans.get(0), "avalue");
        } else {
            assertTrue(beans.isEmpty());
        }
    }

    /**
     * Tests the addition of a non-unique index and a column.
     */
    public void testAddNonUniqueIndexAndColumn() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <index name='test'>
                  <index-column name='avalue'/>
                </index>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertEquals((Object) null, beans.get(0), "avalue");
    }

    /**
     * Tests the addition of a non-unique index and an auto increment column.
     */
    public void testAddNonUniqueIndexAndAutoIncrementColumn() {
        if (!getPlatformInfo().isIndicesSupported() || !getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' autoIncrement='true'/>
                <index name='test'>
                  <index-column name='avalue'/>
                </index>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");
        Object avalue = ((SqlDynaBean) beans.get(0)).get("avalue");

        assertTrue((avalue == null) || Objects.equals(1, avalue));
    }

    /**
     * Tests the addition of a non-unique index and a required column.
     */
    public void testAddNonUniqueIndexAndRequiredColumn() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='NUMERIC' size='12,0' required='true'/>
                <index name='test'>
                  <index-column name='avalue'/>
                </index>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertTrue(beans.isEmpty());
    }

    /**
     * Tests the addition of a non-unique index and a column with a default value.
     */
    public void testAddNonUniqueIndexAndColumnWithDefault() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='DOUBLE' default='2'/>
                <index name='test'>
                  <index-column name='avalue'/>
                </index>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");
        Object avalue = ((SqlDynaBean) beans.get(0)).get("avalue");

        assertTrue((avalue == null) || Double.valueOf(2).equals(avalue));
    }

    /**
     * Tests the addition of a non-unique index and a required auto increment column.
     */
    public void testAddNonUniqueIndexAndRequiredAutoIncrementColumn() {
        if (!getPlatformInfo().isIndicesSupported() || !getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' required='true' autoIncrement='true'/>
                <index name='test'>
                  <index-column name='avalue'/>
                </index>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertEquals((1), beans.get(0), "pk");
        assertEquals((1), beans.get(0), "avalue");
    }

    /**
     * Tests the addition of a non-unique index and a required column with a default value.
     */
    public void testAddNonUniqueIndexAndRequiredColumnWithDefault() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='CHAR' size='8' required='true' default='sometext'/>
                <index name='test'>
                  <index-column name='avalue'/>
                </index>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertEquals((Object) "sometext", beans.get(0), "avalue");
    }

    /**
     * Tests the addition of a non-unique index and several columns.
     */
    public void testAddNonUniqueIndexAndMultipleColumns() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' default='1'/>
                <column name='avalue2' type='VARCHAR' size='32' required='true'/>
                <index name='test'>
                  <index-column name='avalue1'/>
                  <index-column name='avalue2'/>
                </index>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertTrue(beans.isEmpty());
    }


    /**
     * Tests the addition of an unique index and a column.
     */
    public void testAddUniqueIndexAndColumn() {
        // TODO
        if (!getPlatformInfo().isIndicesSupported() || DBTypeEnum.INTERBASE.getName().equals(getPlatform().getName())) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <unique name='test'>
                  <unique-column name='avalue'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertEquals((Object) null, beans.get(0), "avalue");
    }

    /**
     * Tests the addition of an unique index and an auto increment column.
     */
    public void testAddUniqueIndexAndAutoIncrementColumn() {
        if (!getPlatformInfo().isIndicesSupported() || !getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' autoIncrement='true'/>
                <unique name='test'>
                  <unique-column name='avalue'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");
        Object avalue = ((SqlDynaBean) beans.get(0)).get("avalue");

        assertTrue((avalue == null) || Objects.equals(1, avalue));
    }

    /**
     * Tests the addition of an unique index and a required column.
     */
    public void testAddUniqueIndexAndRequiredColumn() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='NUMERIC' size='12,0' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertTrue(beans.isEmpty());
    }

    /**
     * Tests the addition of an unique index and a column with a default value.
     */
    public void testAddUniqueIndexAndColumnWithDefault() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='DOUBLE' default='2'/>
                <unique name='test'>
                  <unique-column name='avalue'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");
        Object avalue = ((SqlDynaBean) beans.get(0)).get("avalue");

        assertTrue((avalue == null) || Double.valueOf(2).equals(avalue));
    }

    /**
     * Tests the addition of an unique index and a required auto increment column.
     */
    public void testAddUniqueIndexAndRequiredAutoIncrementColumn() {
        if (!getPlatformInfo().isIndicesSupported() || !getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' required='true' autoIncrement='true'/>
                <unique name='test'>
                  <unique-column name='avalue'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertEquals((1), beans.get(0), "pk");
        assertEquals((1), beans.get(0), "avalue");
    }

    /**
     * Tests the addition of an unique index and a required column with a default value.
     */
    public void testAddUniqueIndexAndRequiredColumnWithDefault() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='CHAR' size='8' required='true' default='sometext'/>
                <unique name='test'>
                  <unique-column name='avalue'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertEquals((Object) "sometext", beans.get(0), "avalue");
    }

    /**
     * Tests the addition of an unique index and several columns.
     */
    public void testAddUniqueIndexAndMultipleColumns() {
        // TODO
        if (!getPlatformInfo().isIndicesSupported() || DBTypeEnum.INTERBASE.getName().equals(getPlatform().getName())) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' default='1'/>
                <column name='avalue2' type='VARCHAR' size='32' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                  <unique-column name='avalue2'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertTrue(beans.isEmpty());
    }

    /**
     * Tests the addition of an unique index and several required columns.
     */
    public void testAddUniqueIndexAndMultipleRequiredColumns() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true' default='1'/>
                <column name='avalue2' type='VARCHAR' size='32' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                  <unique-column name='avalue2'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertTrue(beans.isEmpty());
    }

    /**
     * Tests the addition of a column into a non-unique index.
     */
    public void testAddColumnIntoNonUniqueIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <index name='test'>
                  <index-column name='avalue1'/>
                </index>
              </table>
            </database>""";
        final String model2Xml = """
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

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertEquals((2), beans.get(0), "avalue1");
        assertEquals((Object) null, beans.get(0), "avalue2");
    }

    /**
     * Tests the addition of an auto increment column into a non-unique index.
     */
    public void testAddAutoIncrementColumnIntoNonUniqueIndex() {
        if (!getPlatformInfo().isIndicesSupported() || !getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <index name='test'>
                  <index-column name='avalue1'/>
                </index>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <column name='avalue2' type='INTEGER' autoIncrement='true'/>
                <index name='test'>
                  <index-column name='avalue1'/>
                  <index-column name='avalue2'/>
                </index>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");
        Object avalue2 = ((SqlDynaBean) beans.get(0)).get("avalue2");

        assertEquals((2), beans.get(0), "avalue1");
        assertTrue((avalue2 == null) || Objects.equals(1, avalue2));
    }

    /**
     * Tests the addition of a required column into a non-unique index.
     */
    public void testAddRequiredColumnIntoNonUniqueIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <index name='test'>
                  <index-column name='avalue1'/>
                </index>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <column name='avalue2' type='NUMERIC' size='12,0' required='true'/>
                <index name='test'>
                  <index-column name='avalue1'/>
                  <index-column name='avalue2'/>
                </index>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertTrue(beans.isEmpty());
    }

    /**
     * Tests the addition of a column with a default value into a non-unique index.
     */
    public void testAddColumnWithDefaultIntoNonUniqueIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <index name='test'>
                  <index-column name='avalue1'/>
                </index>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <column name='avalue2' type='DOUBLE' default='2'/>
                <index name='test'>
                  <index-column name='avalue1'/>
                  <index-column name='avalue2'/>
                </index>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");
        Object avalue2 = ((SqlDynaBean) beans.get(0)).get("avalue2");

        assertEquals((2), beans.get(0), "avalue1");
        assertTrue((avalue2 == null) || Double.valueOf(2).equals(avalue2));
    }

    /**
     * Tests the addition of a required auto increment column into a non-unique index.
     */
    public void testAddRequiredAutoIncrementColumnIntoNonUniqueIndex() {
        if (!getPlatformInfo().isIndicesSupported() || !getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <index name='test'>
                  <index-column name='avalue1'/>
                </index>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <column name='avalue2' type='INTEGER' autoIncrement='true' required='true'/>
                <index name='test'>
                  <index-column name='avalue1'/>
                  <index-column name='avalue2'/>
                </index>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertEquals((1), beans.get(0), "pk");
        assertEquals((2), beans.get(0), "avalue1");
        assertEquals((1), beans.get(0), "avalue2");
    }

    /**
     * Tests the addition of a required column with a default value into a non-unique index.
     */
    public void testAddRequiredColumnWithDefaultIntoNonUniqueIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <index name='test'>
                  <index-column name='avalue1'/>
                </index>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <column name='avalue2' type='CHAR' size='8' default='sometext' required='true'/>
                <index name='test'>
                  <index-column name='avalue1'/>
                  <index-column name='avalue2'/>
                </index>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertEquals((2), beans.get(0), "avalue1");
        assertEquals((Object) "sometext", beans.get(0), "avalue2");
    }

    /**
     * Tests the addition of multiple columns into a non-unique index.
     */
    public void testAddMultipleColumnsIntoNonUniqueIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <index name='test'>
                  <index-column name='avalue1'/>
                </index>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <column name='avalue2' type='INTEGER' default='3'/>
                <column name='avalue3' type='DOUBLE' required='true'/>
                <index name='test'>
                  <index-column name='avalue1'/>
                  <index-column name='avalue2'/>
                  <index-column name='avalue3'/>
                </index>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertTrue(beans.isEmpty());
    }

    /**
     * Tests the addition of a column into an unique index.
     */
    public void testAddColumnIntoUniqueIndex() {
        // TODO
        if (!getPlatformInfo().isIndicesSupported() || DBTypeEnum.INTERBASE.getName().equals(getPlatform().getName())) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                </unique>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <column name='avalue2' type='VARCHAR' size='32'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                  <unique-column name='avalue2'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertEquals((2), beans.get(0), "avalue1");
        assertEquals((Object) null, beans.get(0), "avalue2");
    }

    /**
     * Tests the addition of an auto increment column into an unique index.
     */
    public void testAddAutoIncrementColumnIntoUniqueIndex() {
        if (!getPlatformInfo().isIndicesSupported() || !getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                </unique>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <column name='avalue2' type='INTEGER' autoIncrement='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                  <unique-column name='avalue2'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");
        Object avalue2 = ((SqlDynaBean) beans.get(0)).get("avalue2");

        assertEquals((2), beans.get(0), "avalue1");
        assertTrue((avalue2 == null) || Objects.equals(1, avalue2));
    }

    /**
     * Tests the addition of a required column into an unique index.
     */
    public void testAddRequiredColumnIntoUniqueIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                </unique>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <column name='avalue2' type='NUMERIC' size='12,0' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                  <unique-column name='avalue2'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertTrue(beans.isEmpty());
    }

    /**
     * Tests the addition of a column with a default value into an unique index.
     */
    public void testAddColumnWithDefaultIntoUniqueIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                </unique>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <column name='avalue2' type='DOUBLE' default='2'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                  <unique-column name='avalue2'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");
        Object avalue2 = ((SqlDynaBean) beans.get(0)).get("avalue2");

        assertEquals((2), beans.get(0), "avalue1");
        assertTrue((avalue2 == null) || Double.valueOf(2).equals(avalue2));
    }

    /**
     * Tests the addition of a required auto increment column into an unique index.
     */
    public void testAddRequiredAutoIncrementColumnIntoUniqueIndex() {
        if (!getPlatformInfo().isIndicesSupported() || !getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                </unique>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <column name='avalue2' type='INTEGER' autoIncrement='true' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                  <unique-column name='avalue2'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertEquals((1), beans.get(0), "pk");
        assertEquals((2), beans.get(0), "avalue1");
        assertEquals((1), beans.get(0), "avalue2");
    }

    /**
     * Tests the addition of a required column with a default value into an unique index.
     */
    public void testAddRequiredColumnWithDefaultIntoUniqueIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                </unique>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <column name='avalue2' type='CHAR' size='8' default='sometext' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                  <unique-column name='avalue2'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertEquals((2), beans.get(0), "avalue1");
        assertEquals((Object) "sometext", beans.get(0), "avalue2");
    }

    /**
     * Tests the addition of multiple columns into an unique index.
     */
    public void testAddMultipleColumnsIntoUniqueIndex() {
        // TODO
        if (!getPlatformInfo().isIndicesSupported() || DBTypeEnum.INTERBASE.getName().equals(getPlatform().getName())) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                </unique>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <column name='avalue2' type='INTEGER' default='3'/>
                <column name='avalue3' type='DOUBLE' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                  <unique-column name='avalue2'/>
                  <unique-column name='avalue3'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertTrue(beans.isEmpty());
    }

    /**
     * Tests the addition of multiple required columns into an unique index.
     */
    public void testAddMultipleRequiredColumnsIntoUniqueIndex() {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                </unique>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' required='true'/>
                <column name='avalue2' type='INTEGER' required='true' default='3'/>
                <column name='avalue3' type='DOUBLE' required='true'/>
                <unique name='test'>
                  <unique-column name='avalue1'/>
                  <unique-column name='avalue2'/>
                  <unique-column name='avalue3'/>
                </unique>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip", new Object[]{(1), (2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans = getRows("roundtrip");

        assertTrue(beans.isEmpty());
    }

    /**
     * Tests the addition of a foreign key and its local column.
     */
    public void testAddFKAndLocalColumn() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
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

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{"text"});
        insertRow("roundtrip2", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans1 = getRows("roundtrip1");
        List beans2 = getRows("roundtrip2");

        assertEquals((Object) "text", beans1.get(0), "pk");
        assertEquals((1), beans2.get(0), "pk");
        assertEquals((Object) null, beans2.get(0), "avalue");
    }

    /**
     * Tests the addition of a foreign key and its local auto increment column.
     */
    public void testAddFKAndLocalAutoIncrementColumn() {
        if (!getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' autoIncrement='true'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{(1)});
        insertRow("roundtrip2", new Object[]{(2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans1 = getRows("roundtrip1");
        List beans2 = getRows("roundtrip2");
        Object avalue = ((SqlDynaBean) beans2.get(0)).get("avalue");

        assertEquals((1), beans1.get(0), "pk");
        assertEquals((2), beans2.get(0), "pk");
        assertTrue((avalue == null) || Objects.equals(1, avalue));
    }

    /**
     * Tests the addition of a foreign key and its local required column.
     */
    public void testAddFKAndLocalRequiredColumn() {
        // TODO
        if (DBTypeEnum.MYSQL.getName().equals(getPlatform().getName()) || DBTypeEnum.MYSQL5.getName().equals(getPlatform().getName())) {
            // MySql does not allow adding a required column to a fk without a default value
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='NUMERIC' size='12,0' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='NUMERIC' size='12,0' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='NUMERIC' size='12,0' required='true'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{new BigDecimal(1)});
        insertRow("roundtrip2", new Object[]{(2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans1 = getRows("roundtrip1");
        List beans2 = getRows("roundtrip2");

        assertEquals(new BigDecimal(1), beans1.get(0), "pk");
        assertTrue(beans2.isEmpty());
    }

    /**
     * Tests the addition of a foreign key and its local column with a default value.
     */
    public void testAddFKAndLocalColumnWithDefault() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='DOUBLE' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='DOUBLE' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='DOUBLE' default='1'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{1.0});
        insertRow("roundtrip2", new Object[]{(2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans1 = getRows("roundtrip1");
        List beans2 = getRows("roundtrip2");
        Object avalue = ((SqlDynaBean) beans2.get(0)).get("avalue");

        assertEquals(1.0, beans1.get(0), "pk");
        assertEquals((2), beans2.get(0), "pk");
        assertTrue((avalue == null) || Objects.equals(1, avalue));
    }

    /**
     * Tests the addition of a foreign key and its local required auto increment column.
     */
    public void testAddFKAndLocalRequiredAutoIncrementColumn() {
        if (!getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER' required='true' autoIncrement='true'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{(1)});
        insertRow("roundtrip2", new Object[]{(2)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans1 = getRows("roundtrip1");
        List beans2 = getRows("roundtrip2");

        assertEquals((1), beans1.get(0), "pk");
        assertEquals((2), beans2.get(0), "pk");
        assertEquals((1), beans2.get(0), "avalue");
    }

    /**
     * Tests the addition of a foreign key and its local required column with a default value.
     */
    public void testAddFKAndLocalRequiredColumnWithDefault() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='CHAR' size='8' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='CHAR' size='8' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='CHAR' size='8' required='true' default='moretext'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{"moretext"});
        insertRow("roundtrip2", new Object[]{(1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans1 = getRows("roundtrip1");
        List beans2 = getRows("roundtrip2");

        assertEquals((Object) "moretext", beans1.get(0), "pk");
        assertEquals((1), beans2.get(0), "pk");
        assertEquals((Object) "moretext", beans2.get(0), "avalue");
    }

    /**
     * Tests the addition of a foreign key and its local columns.
     */
    public void testAddFKAndMultipleLocalColumns() {
        // TODO
        if (DBTypeEnum.MYSQL.getName().equals(getPlatform().getName()) || DBTypeEnum.MYSQL5.getName().equals(getPlatform().getName())) {
            // MySql does not allow adding a required column to a fk without a default value
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk2' type='DOUBLE' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk2' type='DOUBLE' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER' default='1'/>
                <column name='avalue2' type='DOUBLE' required='true'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                  <reference local='avalue2' foreign='pk2'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{(1), 2.0});
        insertRow("roundtrip2", new Object[]{(3)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans1 = getRows("roundtrip1");
        List beans2 = getRows("roundtrip2");

        assertEquals((1), beans1.get(0), "pk1");
        assertEquals(2.0, beans1.get(0), "pk2");
        assertTrue(beans2.isEmpty());
    }

    /**
     * Tests the addition of a foreign key and its foreign column.
     */
    public void testAddFKAndForeignColumn() {
        final String model1Xml = """
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
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='avalue' type='INTEGER'/>
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

        createDatabase(model1Xml);
        // no point trying this with data in the db as it will only cause a constraint violation
        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));
    }

    /**
     * Tests the addition of a foreign key and its foreign auto increment column.
     */
    public void testAddFKAndForeignAutoIncrementColumn() {
        if (!getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='avalue' type='INTEGER'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='avalue' type='INTEGER'/>
                <column name='pk' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{(2)});
        insertRow("roundtrip2", new Object[]{(1), (1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans1 = getRows("roundtrip1");
        List beans2 = getRows("roundtrip2");

        assertEquals((1), beans1.get(0), "pk");
        assertEquals((2), beans1.get(0), "avalue");
        assertEquals((1), beans2.get(0), "pk");
        assertEquals((1), beans2.get(0), "avalue");
    }

    /**
     * Tests the addition of a foreign key and its foreign auto increment column.
     */
    public void testAddFKAndForeignColumnWithDefault() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='avalue' type='INTEGER'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='DOUBLE'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='avalue' type='INTEGER'/>
                <column name='pk' type='DOUBLE' primaryKey='true' required='true' default='1'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='DOUBLE'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{(2)});
        insertRow("roundtrip2", new Object[]{(1), 1.0});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans1 = getRows("roundtrip1");
        List beans2 = getRows("roundtrip2");

        assertEquals(1.0, beans1.get(0), "pk");
        assertEquals((2), beans1.get(0), "avalue");
        assertEquals((1), beans2.get(0), "pk");
        assertEquals(1.0, beans2.get(0), "avalue");
    }

    /**
     * Tests the addition of a foreign key and its multiple foreign columns.
     */
    public void testAddFKAndMultipleForeignColumns() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='avalue' type='INTEGER'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <column name='avalue2' type='DOUBLE'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='avalue' type='INTEGER'/>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk2' type='DOUBLE' primaryKey='true' required='true' default='1'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <column name='avalue2' type='DOUBLE'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                  <reference local='avalue2' foreign='pk2'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);
        // no point trying this with data in the db as it will only cause a constraint violation
        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));
    }

    /**
     * Tests the addition of local and foreign column into a foreign key.
     */
    public void testAddColumnsIntoFK() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                </foreign-key>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk2' type='VARCHAR' size='32' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <column name='avalue2' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                  <reference local='avalue2' foreign='pk2'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);
        // no point trying this with data in the db as it will only cause a constraint violation
        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));
    }

    /**
     * Tests the addition of local and foreign auto increment columns into a foreign key.
     */
    public void testAddAutoIncrementColumnIntoFK() {
        if (!getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                </foreign-key>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk2' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <column name='avalue2' type='INTEGER' autoIncrement='true'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                  <reference local='avalue2' foreign='pk2'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{(1)});
        insertRow("roundtrip2", new Object[]{(2), (1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans1 = getRows("roundtrip1");
        List beans2 = getRows("roundtrip2");
        Object avalue2 = ((SqlDynaBean) beans2.get(0)).get("avalue2");

        assertEquals((1), beans1.get(0), "pk1");
        assertEquals((1), beans1.get(0), "pk2");
        assertEquals((2), beans2.get(0), "pk");
        assertEquals((1), beans2.get(0), "avalue1");
        assertTrue((avalue2 == null) || Objects.equals(1, avalue2));
    }

    /**
     * Tests the addition of local and foreign required columns into a foreign key.
     */
    public void testAddRequiredColumnsIntoFK() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                </foreign-key>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk2' type='NUMERIC' size='12,0' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <column name='avalue2' type='NUMERIC' size='12,0' required='true'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                  <reference local='avalue2' foreign='pk2'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);
        // no point trying this with data in the db as it will only cause a constraint violation
        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));
    }

    /**
     * Tests the addition of local and foreign columns with default values into a foreign key.
     */
    public void testAddColumnsWithDefaultsIntoFK() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                </foreign-key>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk2' type='DOUBLE' primaryKey='true' required='true' default='2'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <column name='avalue2' type='DOUBLE' default='2'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                  <reference local='avalue2' foreign='pk2'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{(1)});
        insertRow("roundtrip2", new Object[]{(2), (1)});

        alterDatabase(model2Xml);

        List beans1 = getRows("roundtrip1");
        List beans2 = getRows("roundtrip2");
        Object avalue2 = ((SqlDynaBean) beans2.get(0)).get("avalue2");

        assertEquals((1), beans1.get(0), "pk1");
        assertEquals(2.0, beans1.get(0), "pk2");
        assertEquals((2), beans2.get(0), "pk");
        assertEquals((1), beans2.get(0), "avalue1");
        assertTrue((avalue2 == null) || Double.valueOf(2).equals(avalue2));
    }

    /**
     * Tests the addition of local and foreign required auto increment columns into a foreign key.
     */
    public void testAddRequiredAutoIncrementColumnIntoFK() {
        if (!getPlatformInfo().isNonPrimaryKeyIdentityColumnsSupported()) {
            return;
        }

        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                </foreign-key>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk2' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <column name='avalue2' type='INTEGER' required='true' autoIncrement='true'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                  <reference local='avalue2' foreign='pk2'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{(1)});
        insertRow("roundtrip2", new Object[]{(2), (1)});

        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));

        List beans1 = getRows("roundtrip1");
        List beans2 = getRows("roundtrip2");

        assertEquals((1), beans1.get(0), "pk1");
        assertEquals((1), beans1.get(0), "pk2");
        assertEquals((2), beans2.get(0), "pk");
        assertEquals((1), beans2.get(0), "avalue1");
        assertEquals((1), beans2.get(0), "avalue2");
    }

    /**
     * Tests the addition of local and foreign required columns with default values into a foreign key.
     */
    public void testAddRequiredColumnsWithDefaultsIntoFK() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                </foreign-key>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk2' type='CHAR' size='8' primaryKey='true' required='true' default='sometext'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <column name='avalue2' type='CHAR' size='8' required='true' default='sometext'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                  <reference local='avalue2' foreign='pk2'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);

        insertRow("roundtrip1", new Object[]{(1)});
        insertRow("roundtrip2", new Object[]{(2), (1)});

        alterDatabase(model2Xml);

        List beans1 = getRows("roundtrip1");
        List beans2 = getRows("roundtrip2");

        assertEquals((1), beans1.get(0), "pk1");
        assertEquals((Object) "sometext", beans1.get(0), "pk2");
        assertEquals((2), beans2.get(0), "pk");
        assertEquals((1), beans2.get(0), "avalue1");
        assertEquals((Object) "sometext", beans2.get(0), "avalue2");
    }

    /**
     * Tests the addition of multiple local and foreign columns into a foreign key.
     */
    public void testAddMultipleColumnsIntoFK() {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                </foreign-key>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk2' type='INTEGER' primaryKey='true' required='true' default='1'/>
                <column name='pk3' type='DOUBLE' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue1' type='INTEGER'/>
                <column name='avalue2' type='INTEGER' default='1'/>
                <column name='avalue3' type='DOUBLE' required='true'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue1' foreign='pk1'/>
                  <reference local='avalue2' foreign='pk2'/>
                  <reference local='avalue3' foreign='pk3'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);
        // no point trying this with data in the db as it will only cause a constraint violation
        alterDatabase(model2Xml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));
    }
}
