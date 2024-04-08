package org.apache.ddlutils.io;

import org.apache.ddlutils.TestAgainstLiveDatabaseBase;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TableObject;
import org.apache.ddlutils.platform.DBTypeEnum;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;
import org.xml.sax.InputSource;

import java.io.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Contains misc tests.
 */
public class TestMisc extends TestAgainstLiveDatabaseBase {

    /**
     * Tests the backup and restore of a table with an identity column and a foreign key to
     * it when identity override is turned on.
     */
    @Test
    public void testIdentityOverrideOn() throws Exception {
        if (!getPlatformInfo().isIdentityOverrideAllowed()) {
            // TODO: for testing these platforms, we need deleteRows
            return;
        }

        // Sybase does not like INTEGER auto-increment columns
        boolean isSybase = DBTypeEnum.SYBASE.getName().equals(getPlatform().getName());
        final String modelXml;

        if (isSybase) {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='misc1'>
                    <column name='pk' type='NUMERIC' size='12,0' primaryKey='true' required='true' autoIncrement='true'/>
                    <column name='avalue' type='INTEGER' required='false'/>
                  </table>
                  <table name='misc2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='fk' type='NUMERIC' size='12,0' required='false'/>
                    <foreign-key name='test' foreignTable='misc1'>
                      <reference local='fk' foreign='pk'/>
                    </foreign-key>
                  </table>
                </database>""";
        } else {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='misc1'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
                    <column name='avalue' type='INTEGER' required='false'/>
                  </table>
                  <table name='misc2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='fk' type='INTEGER' required='false'/>
                    <foreign-key name='test' foreignTable='misc1'>
                      <reference local='fk' foreign='pk'/>
                    </foreign-key>
                  </table>
                </database>""";
        }

        createDatabase(modelXml);

        getPlatform().setIdentityOverrideOn(true);

        if (isSybase) {
            insertRow("misc1", new Object[]{new BigDecimal(10), (1)});
            insertRow("misc1", new Object[]{new BigDecimal(12), (2)});
            insertRow("misc1", new Object[]{new BigDecimal(13), (3)});
            insertRow("misc2", new Object[]{(1), new BigDecimal(10)});
            insertRow("misc2", new Object[]{(2), new BigDecimal(13)});
        } else {
            insertRow("misc1", new Object[]{(10), (1)});
            insertRow("misc1", new Object[]{(12), (2)});
            insertRow("misc1", new Object[]{(13), (3)});
            insertRow("misc2", new Object[]{(1), (10)});
            insertRow("misc2", new Object[]{(2), (13)});
        }

        StringWriter stringWriter = new StringWriter();
        DatabaseDataIO dataIO = new DatabaseDataIO();

        dataIO.writeDataToXML(getPlatform(), getModel(), stringWriter, "UTF-8");

        String dataAsXml = stringWriter.toString();
        SAXReader reader = new SAXReader();
        Document testDoc = reader.read(new InputSource(new StringReader(dataAsXml)));

        List<Node> misc1Rows = testDoc.selectNodes("//misc1");
        List<Node> misc2Rows = testDoc.selectNodes("//misc2");
        String pkColumnName = "pk";
        String fkColumnName = "fk";
        String valueColumnName = "avalue";

        if (misc1Rows.isEmpty()) {
            misc1Rows = testDoc.selectNodes("//MISC1");
            misc2Rows = testDoc.selectNodes("//MISC2");
            pkColumnName = pkColumnName.toUpperCase();
            fkColumnName = fkColumnName.toUpperCase();
            valueColumnName = valueColumnName.toUpperCase();
        }

        assertEquals(3, misc1Rows.size());
        assertEquals("10", ((Element) misc1Rows.get(0)).attributeValue(pkColumnName));
        assertEquals("1", ((Element) misc1Rows.get(0)).attributeValue(valueColumnName));
        assertEquals("12", ((Element) misc1Rows.get(1)).attributeValue(pkColumnName));
        assertEquals("2", ((Element) misc1Rows.get(1)).attributeValue(valueColumnName));
        assertEquals("13", ((Element) misc1Rows.get(2)).attributeValue(pkColumnName));
        assertEquals("3", ((Element) misc1Rows.get(2)).attributeValue(valueColumnName));
        assertEquals(2, misc2Rows.size());
        assertEquals("1", ((Element) misc2Rows.get(0)).attributeValue(pkColumnName));
        assertEquals("10", ((Element) misc2Rows.get(0)).attributeValue(fkColumnName));
        assertEquals("2", ((Element) misc2Rows.get(1)).attributeValue(pkColumnName));
        assertEquals("13", ((Element) misc2Rows.get(1)).attributeValue(fkColumnName));

        dropDatabase();
        createDatabase(modelXml);

        StringReader stringReader = new StringReader(dataAsXml);

        dataIO.writeDataToDatabase(getPlatform(), getModel(), new Reader[]{stringReader});

        List<TableObject> beans = getRows("misc1");

        if (isSybase) {
            assertEquals(new BigDecimal(10), beans.get(0), "pk");
            assertEquals(new BigDecimal(12), beans.get(1), "pk");
            assertEquals(new BigDecimal(13), beans.get(2), "pk");
        } else {
            assertEquals((10), beans.get(0), "pk");
            assertEquals((12), beans.get(1), "pk");
            assertEquals((13), beans.get(2), "pk");
        }
        assertEquals((1), beans.get(0), "avalue");
        assertEquals((2), beans.get(1), "avalue");
        assertEquals((3), beans.get(2), "avalue");

        beans = getRows("misc2");

        assertEquals((1), beans.get(0), "pk");
        assertEquals((2), beans.get(1), "pk");
        if (isSybase) {
            assertEquals(new BigDecimal(10), beans.get(0), "fk");
            assertEquals(new BigDecimal(13), beans.get(1), "fk");
        } else {
            assertEquals((10), beans.get(0), "fk");
            assertEquals((13), beans.get(1), "fk");
        }
    }

    /**
     * Tests the backup and restore of a table with an identity column and a foreign key to
     * it when identity override is turned off.
     */
    @Test
    public void testIdentityOverrideOff() throws Exception {
        if (!getPlatformInfo().isIdentityOverrideAllowed()) {
            // TODO: for testing these platforms, we need deleteRows
            return;
        }

        // Sybase does not like INTEGER auto-increment columns
        boolean isSybase = DBTypeEnum.SYBASE.getName().equals(getPlatform().getName());
        final String modelXml;

        if (isSybase) {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='misc1'>
                    <column name='pk' type='NUMERIC' size='12,0' primaryKey='true' required='true' autoIncrement='true'/>
                    <column name='avalue' type='INTEGER' required='false'/>
                  </table>
                  <table name='misc2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='fk' type='NUMERIC' size='12,0' required='false'/>
                    <foreign-key name='test' foreignTable='misc1'>
                      <reference local='fk' foreign='pk'/>
                    </foreign-key>
                  </table>
                </database>""";
        } else {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='misc1'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
                    <column name='avalue' type='INTEGER' required='false'/>
                  </table>
                  <table name='misc2'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='fk' type='INTEGER' required='false'/>
                    <foreign-key name='test' foreignTable='misc1'>
                      <reference local='fk' foreign='pk'/>
                    </foreign-key>
                  </table>
                </database>""";
        }

        createDatabase(modelXml);

        getPlatform().setIdentityOverrideOn(true);

        if (isSybase) {
            insertRow("misc1", new Object[]{new BigDecimal(10), (1)});
            insertRow("misc1", new Object[]{new BigDecimal(12), (2)});
            insertRow("misc1", new Object[]{new BigDecimal(13), (3)});
            insertRow("misc2", new Object[]{(1), new BigDecimal(10)});
            insertRow("misc2", new Object[]{(2), new BigDecimal(13)});
        } else {
            insertRow("misc1", new Object[]{(10), (1)});
            insertRow("misc1", new Object[]{(12), (2)});
            insertRow("misc1", new Object[]{(13), (3)});
            insertRow("misc2", new Object[]{(1), (10)});
            insertRow("misc2", new Object[]{(2), (13)});
        }

        StringWriter stringWriter = new StringWriter();
        DatabaseDataIO dataIO = new DatabaseDataIO();

        dataIO.writeDataToXML(getPlatform(), getModel(), stringWriter, "UTF-8");

        String dataAsXml = stringWriter.toString();
        SAXReader reader = new SAXReader();
        Document testDoc = reader.read(new InputSource(new StringReader(dataAsXml)));

        List<Node> misc1Rows = testDoc.selectNodes("//misc1");
        List<Node> misc2Rows = testDoc.selectNodes("//misc2");
        String pkColumnName = "pk";
        String fkColumnName = "fk";
        String valueColumnName = "avalue";

        if (misc1Rows.isEmpty()) {
            misc1Rows = testDoc.selectNodes("//MISC1");
            misc2Rows = testDoc.selectNodes("//MISC2");
            pkColumnName = pkColumnName.toUpperCase();
            fkColumnName = fkColumnName.toUpperCase();
            valueColumnName = valueColumnName.toUpperCase();
        }

        assertEquals(3, misc1Rows.size());
        assertEquals("10", ((Element) misc1Rows.get(0)).attributeValue(pkColumnName));
        assertEquals("1", ((Element) misc1Rows.get(0)).attributeValue(valueColumnName));
        assertEquals("12", ((Element) misc1Rows.get(1)).attributeValue(pkColumnName));
        assertEquals("2", ((Element) misc1Rows.get(1)).attributeValue(valueColumnName));
        assertEquals("13", ((Element) misc1Rows.get(2)).attributeValue(pkColumnName));
        assertEquals("3", ((Element) misc1Rows.get(2)).attributeValue(valueColumnName));
        assertEquals(2, misc2Rows.size());
        assertEquals("1", ((Element) misc2Rows.get(0)).attributeValue(pkColumnName));
        assertEquals("10", ((Element) misc2Rows.get(0)).attributeValue(fkColumnName));
        assertEquals("2", ((Element) misc2Rows.get(1)).attributeValue(pkColumnName));
        assertEquals("13", ((Element) misc2Rows.get(1)).attributeValue(fkColumnName));

        dropDatabase();
        createDatabase(modelXml);

        getPlatform().setIdentityOverrideOn(false);

        StringReader stringReader = new StringReader(dataAsXml);

        dataIO.writeDataToDatabase(getPlatform(), getModel(), new Reader[]{stringReader});

        List<TableObject> beans = getRows("misc1");

        if (isSybase) {
            assertEquals(new BigDecimal(1), beans.get(0), "pk");
            assertEquals(new BigDecimal(2), beans.get(1), "pk");
            assertEquals(new BigDecimal(3), beans.get(2), "pk");
        } else {
            assertEquals((1), beans.get(0), "pk");
            assertEquals((2), beans.get(1), "pk");
            assertEquals((3), beans.get(2), "pk");
        }
        assertEquals((1), beans.get(0), "avalue");
        assertEquals((2), beans.get(1), "avalue");
        assertEquals((3), beans.get(2), "avalue");

        beans = getRows("misc2");

        assertEquals((1), beans.get(0), "pk");
        assertEquals((2), beans.get(1), "pk");
        if (isSybase) {
            assertEquals(new BigDecimal(1), beans.get(0), "fk");
            assertEquals(new BigDecimal(3), beans.get(1), "fk");
        } else {
            assertEquals((1), beans.get(0), "fk");
            assertEquals((3), beans.get(1), "fk");
        }
    }

    /**
     * Tests the backup and restore of a table with an identity column and a foreign key to
     * itself while identity override is off.
     */
    public void testSelfReferenceIdentityOverrideOff() throws Exception {
        // Hsqldb does not allow rows to reference themselves
        if (DBTypeEnum.HSQLDB.getName().equals(getPlatform().getName())) {
            return;
        }

        // Sybase does not like INTEGER auto-increment columns
        boolean isSybase = DBTypeEnum.SYBASE.getName().equals(getPlatform().getName());
        final String modelXml;

        if (isSybase) {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='misc'>
                    <column name='pk' type='NUMERIC' size='12,0' primaryKey='true' required='true' autoIncrement='true'/>
                    <column name='fk' type='NUMERIC' size='12,0' required='false'/>
                    <foreign-key name='test' foreignTable='misc'>
                      <reference local='fk' foreign='pk'/>
                    </foreign-key>
                  </table>
                </database>""";
        } else {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='misc'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
                    <column name='fk' type='INTEGER' required='false'/>
                    <foreign-key name='test' foreignTable='misc'>
                      <reference local='fk' foreign='pk'/>
                    </foreign-key>
                  </table>
                </database>""";
        }

        createDatabase(modelXml);

        getPlatform().setIdentityOverrideOn(false);

        if (isSybase) {
            insertRow("misc", new Object[]{new BigDecimal(1), null});
            insertRow("misc", new Object[]{new BigDecimal(2), new BigDecimal(1)});
            insertRow("misc", new Object[]{new BigDecimal(3), new BigDecimal(2)});
            insertRow("misc", new Object[]{new BigDecimal(4), new BigDecimal(4)});
        } else {
            insertRow("misc", new Object[]{(1), null});
            insertRow("misc", new Object[]{(2), (1)});
            insertRow("misc", new Object[]{(3), (2)});
            insertRow("misc", new Object[]{(4), (4)});
        }

        StringWriter stringWriter = new StringWriter();
        DatabaseDataIO dataIO = new DatabaseDataIO();

        dataIO.writeDataToXML(getPlatform(), getModel(), stringWriter, "UTF-8");

        String dataAsXml = stringWriter.toString();
        SAXReader reader = new SAXReader();
        Document testDoc = reader.read(new InputSource(new StringReader(dataAsXml)));

        List<Node> miscRows = testDoc.selectNodes("//misc");
        String pkColumnName = "pk";
        String fkColumnName = "fk";

        if (miscRows.isEmpty()) {
            miscRows = testDoc.selectNodes("//MISC");
            pkColumnName = pkColumnName.toUpperCase();
            fkColumnName = fkColumnName.toUpperCase();
        }

        assertEquals(4, miscRows.size());
        assertEquals("1", ((Element) miscRows.get(0)).attributeValue(pkColumnName));
        assertNull(((Element) miscRows.get(0)).attributeValue(fkColumnName));
        assertEquals("2", ((Element) miscRows.get(1)).attributeValue(pkColumnName));
        assertEquals("1", ((Element) miscRows.get(1)).attributeValue(fkColumnName));
        assertEquals("3", ((Element) miscRows.get(2)).attributeValue(pkColumnName));
        assertEquals("2", ((Element) miscRows.get(2)).attributeValue(fkColumnName));
        assertEquals("4", ((Element) miscRows.get(3)).attributeValue(pkColumnName));
        assertEquals("4", ((Element) miscRows.get(3)).attributeValue(fkColumnName));

        dropDatabase();
        createDatabase(modelXml);

        StringReader stringReader = new StringReader(dataAsXml);

        dataIO.writeDataToDatabase(getPlatform(), getModel(), new Reader[]{stringReader});

        List<TableObject> beans = getRows("misc");

        if (isSybase) {
            assertEquals(new BigDecimal(1), beans.get(0), "pk");
            assertNull(beans.get(0).getColumnValue("fk"));
            assertEquals(new BigDecimal(2), beans.get(1), "pk");
            assertEquals(new BigDecimal(1), beans.get(1), "fk");
            assertEquals(new BigDecimal(3), beans.get(2), "pk");
            assertEquals(new BigDecimal(2), beans.get(2), "fk");
            assertEquals(new BigDecimal(4), beans.get(3), "pk");
            assertEquals(new BigDecimal(4), beans.get(3), "fk");
        } else {
            assertEquals((1), beans.get(0), "pk");
            assertNull(beans.get(0).getColumnValue("fk"));
            assertEquals((2), beans.get(1), "pk");
            assertEquals((1), beans.get(1), "fk");
            assertEquals((3), beans.get(2), "pk");
            assertEquals((2), beans.get(2), "fk");
            assertEquals((4), beans.get(3), "pk");
            assertEquals((4), beans.get(3), "fk");
        }
    }

    /**
     * Tests the backup and restore of a table with an identity column and a foreign key to
     * itself while identity override is off.
     */
    public void testSelfReferenceIdentityOverrideOn() throws Exception {
        if (!getPlatformInfo().isIdentityOverrideAllowed()) {
            // TODO: for testing these platforms, we need deleteRows
            return;
        }

        // Sybase does not like INTEGER auto-increment columns
        boolean isSybase = DBTypeEnum.SYBASE.getName().equals(getPlatform().getName());
        final String modelXml;

        if (isSybase) {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='misc'>
                    <column name='pk' type='NUMERIC' size='12,0' primaryKey='true' required='true' autoIncrement='true'/>
                    <column name='fk' type='NUMERIC' size='12,0' required='false'/>
                    <foreign-key name='test' foreignTable='misc'>
                      <reference local='fk' foreign='pk'/>
                    </foreign-key>
                  </table>
                </database>""";
        } else {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='misc'>
                    <column name='pk' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
                    <column name='fk' type='INTEGER' required='false'/>
                    <foreign-key name='test' foreignTable='misc'>
                      <reference local='fk' foreign='pk'/>
                    </foreign-key>
                  </table>
                </database>""";
        }

        createDatabase(modelXml);

        getPlatform().setIdentityOverrideOn(true);

        if (isSybase) {
            insertRow("misc", new Object[]{new BigDecimal(10), null});
            insertRow("misc", new Object[]{new BigDecimal(11), new BigDecimal(10)});
            insertRow("misc", new Object[]{new BigDecimal(12), new BigDecimal(11)});
            insertRow("misc", new Object[]{new BigDecimal(13), new BigDecimal(13)});
        } else {
            insertRow("misc", new Object[]{(10), null});
            insertRow("misc", new Object[]{(11), (10)});
            insertRow("misc", new Object[]{(12), (11)});
            insertRow("misc", new Object[]{(13), (13)});
        }

        StringWriter stringWriter = new StringWriter();
        DatabaseDataIO dataIO = new DatabaseDataIO();

        dataIO.writeDataToXML(getPlatform(), getModel(), stringWriter, "UTF-8");

        String dataAsXml = stringWriter.toString();
        SAXReader reader = new SAXReader();
        Document testDoc = reader.read(new InputSource(new StringReader(dataAsXml)));

        List<Node> miscRows = testDoc.selectNodes("//misc");
        String pkColumnName = "pk";
        String fkColumnName = "fk";

        if (miscRows.isEmpty()) {
            miscRows = testDoc.selectNodes("//MISC");
            pkColumnName = pkColumnName.toUpperCase();
            fkColumnName = fkColumnName.toUpperCase();
        }

        assertEquals(4, miscRows.size());
        assertEquals("10", ((Element) miscRows.get(0)).attributeValue(pkColumnName));
        assertNull(((Element) miscRows.get(0)).attributeValue(fkColumnName));
        assertEquals("11", ((Element) miscRows.get(1)).attributeValue(pkColumnName));
        assertEquals("10", ((Element) miscRows.get(1)).attributeValue(fkColumnName));
        assertEquals("12", ((Element) miscRows.get(2)).attributeValue(pkColumnName));
        assertEquals("11", ((Element) miscRows.get(2)).attributeValue(fkColumnName));
        assertEquals("13", ((Element) miscRows.get(3)).attributeValue(pkColumnName));
        assertEquals("13", ((Element) miscRows.get(3)).attributeValue(fkColumnName));

        dropDatabase();
        createDatabase(modelXml);

        StringReader stringReader = new StringReader(dataAsXml);

        dataIO.writeDataToDatabase(getPlatform(), getModel(), new Reader[]{stringReader});

        List<TableObject> beans = getRows("misc");

        if (isSybase) {
            assertEquals(new BigDecimal(10), beans.get(0), "pk");
            assertNull(beans.get(0).getColumnValue("fk"));
            assertEquals(new BigDecimal(11), beans.get(1), "pk");
            assertEquals(new BigDecimal(10), beans.get(1), "fk");
            assertEquals(new BigDecimal(12), beans.get(2), "pk");
            assertEquals(new BigDecimal(11), beans.get(2), "fk");
            assertEquals(new BigDecimal(13), beans.get(3), "pk");
            assertEquals(new BigDecimal(13), beans.get(3), "fk");
        } else {
            assertEquals((10), beans.get(0), "pk");
            assertNull(beans.get(0).getColumnValue("fk"));
            assertEquals((11), beans.get(1), "pk");
            assertEquals((10), beans.get(1), "fk");
            assertEquals((12), beans.get(2), "pk");
            assertEquals((11), beans.get(2), "fk");
            assertEquals((13), beans.get(3), "pk");
            assertEquals((13), beans.get(3), "fk");
        }
    }

    /**
     * Tests the backup and restore of a self-referencing data set.
     */
    public void testSelfReferences() throws Exception {
        if (!getPlatformInfo().isIdentityOverrideAllowed()) {
            // TODO: for testing these platforms, we need deleteRows
            return;
        }

        // Sybase does not like INTEGER auto-increment columns
        boolean isSybase = DBTypeEnum.SYBASE.getName().equals(getPlatform().getName());
        final String modelXml;

        if (isSybase) {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='misc'>
                    <column name='id' primaryKey='true' required='true' type='NUMERIC' size='10,0' autoIncrement='true'/>
                    <column name='parent_id' primaryKey='false' required='false' type='NUMERIC' size='10,0' autoIncrement='false'/>
                    <foreign-key foreignTable='misc' name='misc_parent_fk'>
                      <reference local='parent_id' foreign='id'/>
                    </foreign-key>
                  </table>
                </database>""";
        } else {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='misc'>
                    <column name='id' primaryKey='true' required='true' type='SMALLINT' size='2' autoIncrement='true'/>
                    <column name='parent_id' primaryKey='false' required='false' type='SMALLINT' size='2' autoIncrement='false'/>
                    <foreign-key foreignTable='misc' name='misc_parent_fk'>
                      <reference local='parent_id' foreign='id'/>
                    </foreign-key>
                  </table>
                </database>""";
        }

        final String dataXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <data>
              <misc id='4' parent_id='1'/>
              <misc id='7' parent_id='1'/>
              <misc id='3' parent_id='2'/>
              <misc id='5' parent_id='3'/>
              <misc id='8' parent_id='7'/>
              <misc id='9' parent_id='6'/>
              <misc id='10' parent_id='4'/>
              <misc id='1'/>
              <misc id='2'/>
              <misc id='6'/>
              <misc id='11'/>
              <misc id='12' parent_id='11'/>
            </data>""";

        createDatabase(modelXml);

        getPlatform().setIdentityOverrideOn(true);

        DatabaseDataIO dataIO = new DatabaseDataIO();
        StringReader stringReader = new StringReader(dataXml);

        dataIO.writeDataToDatabase(getPlatform(), getModel(), new Reader[]{stringReader});

        List<TableObject> beans = getRows("misc", "id");

        assertEquals(12, beans.size());
        if (isSybase) {
            assertEquals(new BigDecimal(1), beans.get(0), "id");
            assertNull(beans.get(0).getColumnValue("parent_id"));
            assertEquals(new BigDecimal(2), beans.get(1), "id");
            assertNull(beans.get(1).getColumnValue("parent_id"));
            assertEquals(new BigDecimal(3), beans.get(2), "id");
            assertEquals(new BigDecimal(2), beans.get(2), "parent_id");
            assertEquals(new BigDecimal(4), beans.get(3), "id");
            assertEquals(new BigDecimal(1), beans.get(3), "parent_id");
            assertEquals(new BigDecimal(5), beans.get(4), "id");
            assertEquals(new BigDecimal(3), beans.get(4), "parent_id");
            assertEquals(new BigDecimal(6), beans.get(5), "id");
            assertNull(beans.get(5).getColumnValue("parent_id"));
            assertEquals(new BigDecimal(7), beans.get(6), "id");
            assertEquals(new BigDecimal(1), beans.get(6), "parent_id");
            assertEquals(new BigDecimal(8), beans.get(7), "id");
            assertEquals(new BigDecimal(7), beans.get(7), "parent_id");
            assertEquals(new BigDecimal(9), beans.get(8), "id");
            assertEquals(new BigDecimal(6), beans.get(8), "parent_id");
            assertEquals(new BigDecimal(10), beans.get(9), "id");
            assertEquals(new BigDecimal(4), beans.get(9), "parent_id");
            assertEquals(new BigDecimal(11), beans.get(10), "id");
            assertNull(beans.get(10).getColumnValue("parent_id"));
            assertEquals(new BigDecimal(12), beans.get(11), "id");
            assertEquals(new BigDecimal(11), beans.get(11), "parent_id");
        } else {
            assertEquals((1), beans.get(0), "id");
            assertNull(beans.get(0).getColumnValue("parent_id"));
            assertEquals((2), beans.get(1), "id");
            assertNull(beans.get(1).getColumnValue("parent_id"));
            assertEquals((3), beans.get(2), "id");
            assertEquals((2), beans.get(2), "parent_id");
            assertEquals((4), beans.get(3), "id");
            assertEquals((1), beans.get(3), "parent_id");
            assertEquals((5), beans.get(4), "id");
            assertEquals((3), beans.get(4), "parent_id");
            assertEquals((6), beans.get(5), "id");
            assertNull(beans.get(5).getColumnValue("parent_id"));
            assertEquals((7), beans.get(6), "id");
            assertEquals((1), beans.get(6), "parent_id");
            assertEquals((8), beans.get(7), "id");
            assertEquals((7), beans.get(7), "parent_id");
            assertEquals((9), beans.get(8), "id");
            assertEquals((6), beans.get(8), "parent_id");
            assertEquals((10), beans.get(9), "id");
            assertEquals((4), beans.get(9), "parent_id");
            assertEquals((11), beans.get(10), "id");
            assertNull(beans.get(10).getColumnValue("parent_id"));
            assertEquals((12), beans.get(11), "id");
            assertEquals((11), beans.get(11), "parent_id");
        }
    }

    /**
     * Tests the backup and restore of a self-referencing data set (with multiple self references
     * in the same table).
     */
    public void testMultiSelfReferences() throws Exception {
        if (!getPlatformInfo().isIdentityOverrideAllowed()) {
            // TODO: for testing these platforms, we need deleteRows
            return;
        }

        // Sybase does not like INTEGER auto-increment columns
        boolean isSybase = DBTypeEnum.SYBASE.getName().equals(getPlatform().getName());
        final String modelXml;

        if (isSybase) {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='misc'>
                    <column name='id' primaryKey='true' required='true' type='NUMERIC' size='6,0' autoIncrement='true'/>
                    <column name='left_id' primaryKey='false' required='false' type='NUMERIC' size='6,0' autoIncrement='false'/>
                    <column name='right_id' primaryKey='false' required='false' type='NUMERIC' size='6,0' autoIncrement='false'/>
                    <foreign-key foreignTable='misc' name='misc_left_fk'>
                      <reference local='left_id' foreign='id'/>
                    </foreign-key>
                    <foreign-key foreignTable='misc' name='misc_right_fk'>
                      <reference local='right_id' foreign='id'/>
                    </foreign-key>
                  </table>
                </database>""";
        } else {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='misc'>
                    <column name='id' primaryKey='true' required='true' type='SMALLINT' size='2' autoIncrement='true'/>
                    <column name='left_id' primaryKey='false' required='false' type='SMALLINT' size='2' autoIncrement='false'/>
                    <column name='right_id' primaryKey='false' required='false' type='SMALLINT' size='2' autoIncrement='false'/>
                    <foreign-key foreignTable='misc' name='misc_left_fk'>
                      <reference local='left_id' foreign='id'/>
                    </foreign-key>
                    <foreign-key foreignTable='misc' name='misc_right_fk'>
                      <reference local='right_id' foreign='id'/>
                    </foreign-key>
                  </table>
                </database>""";
        }

        final String dataXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <data>
              <misc id='1' left_id='2' right_id='3'/>
              <misc id='3' left_id='2' right_id='4'/>
              <misc id='2' left_id='5' right_id='4'/>
              <misc id='5' right_id='6'/>
              <misc id='6'/>
              <misc id='4' left_id='6'/>
            </data>""";

        createDatabase(modelXml);

        getPlatform().setIdentityOverrideOn(true);

        DatabaseDataIO dataIO = new DatabaseDataIO();
        StringReader stringReader = new StringReader(dataXml);

        dataIO.writeDataToDatabase(getPlatform(), getModel(), new Reader[]{stringReader});

        List<TableObject> beans = getRows("misc", "id");

        assertEquals(6, beans.size());
        if (isSybase) {
            assertEquals(new BigDecimal(1), beans.get(0), "id");
            assertEquals(new BigDecimal(2), beans.get(0), "left_id");
            assertEquals(new BigDecimal(3), beans.get(0), "right_id");
            assertEquals(new BigDecimal(2), beans.get(1), "id");
            assertEquals(new BigDecimal(5), beans.get(1), "left_id");
            assertEquals(new BigDecimal(4), beans.get(1), "right_id");
            assertEquals(new BigDecimal(3), beans.get(2), "id");
            assertEquals(new BigDecimal(2), beans.get(2), "left_id");
            assertEquals(new BigDecimal(4), beans.get(2), "right_id");
            assertEquals(new BigDecimal(4), beans.get(3), "id");
            assertEquals(new BigDecimal(6), beans.get(3), "left_id");
            assertEquals((Object) null, beans.get(3), "right_id");
            assertEquals(new BigDecimal(5), beans.get(4), "id");
            assertEquals((Object) null, beans.get(4), "left_id");
            assertEquals(new BigDecimal(6), beans.get(4), "right_id");
            assertEquals(new BigDecimal(6), beans.get(5), "id");
            assertEquals((Object) null, beans.get(5), "left_id");
            assertEquals((Object) null, beans.get(5), "right_id");
        } else {
            assertEquals((1), beans.get(0), "id");
            assertEquals((2), beans.get(0), "left_id");
            assertEquals((3), beans.get(0), "right_id");
            assertEquals((2), beans.get(1), "id");
            assertEquals((5), beans.get(1), "left_id");
            assertEquals((4), beans.get(1), "right_id");
            assertEquals((3), beans.get(2), "id");
            assertEquals((2), beans.get(2), "left_id");
            assertEquals((4), beans.get(2), "right_id");
            assertEquals((4), beans.get(3), "id");
            assertEquals((6), beans.get(3), "left_id");
            assertEquals((Object) null, beans.get(3), "right_id");
            assertEquals((5), beans.get(4), "id");
            assertEquals((Object) null, beans.get(4), "left_id");
            assertEquals((6), beans.get(4), "right_id");
            assertEquals((6), beans.get(5), "id");
            assertEquals((Object) null, beans.get(5), "left_id");
            assertEquals((Object) null, beans.get(5), "right_id");
        }
    }

    /**
     * Tests the backup and restore of several tables with complex relationships with an identity column and a foreign key to
     * itself while identity override is off.
     */
    public void testComplexTableModel() throws Exception {
        // A: self-reference (A1->A2)
        // B: self- and foreign-reference (B1->B2|G1, B2->G2)
        // C: circular reference involving more than one table (C1->D1,C2->D2)
        // D: foreign-reference to F (D1->F1,D2)
        // E: isolated table (E1)
        // F: foreign-reference to C (F1->C2)
        // G: no references (G1, G2)

        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='A'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='fk' type='INTEGER' required='false'/>
                <foreign-key name='AtoA' foreignTable='A'>
                  <reference local='fk' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='B'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='fk1' type='INTEGER' required='false'/>
                <column name='fk2' type='INTEGER' required='false'/>
                <foreign-key name='BtoB' foreignTable='B'>
                  <reference local='fk1' foreign='pk'/>
                </foreign-key>
                <foreign-key name='BtoG' foreignTable='G'>
                  <reference local='fk2' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='C'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='fk' type='INTEGER' required='false'/>
                <foreign-key name='CtoD' foreignTable='D'>
                  <reference local='fk' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='D'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='fk' type='INTEGER' required='false'/>
                <foreign-key name='DtoF' foreignTable='F'>
                  <reference local='fk' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='E'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='F'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='fk' type='INTEGER' required='false'/>
                <foreign-key name='FtoC' foreignTable='C'>
                  <reference local='fk' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='G'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        createDatabase(modelXml);

        getPlatform().setIdentityOverrideOn(true);

        // this is the optimal insertion order
        insertRow("E", new Object[]{(1)});
        insertRow("G", new Object[]{(1)});
        insertRow("G", new Object[]{(2)});
        insertRow("A", new Object[]{(2), null});
        insertRow("A", new Object[]{(1), (2)});
        insertRow("B", new Object[]{(2), null, (2)});
        insertRow("B", new Object[]{(1), (2), (1)});
        insertRow("D", new Object[]{(2), null});
        insertRow("C", new Object[]{(2), (2)});
        insertRow("F", new Object[]{(1), (2)});
        insertRow("D", new Object[]{(1), (1)});
        insertRow("C", new Object[]{(1), (1)});

        StringWriter stringWriter = new StringWriter();
        DatabaseDataIO dataIO = new DatabaseDataIO();

        dataIO.writeDataToXML(getPlatform(), getModel(), stringWriter, "UTF-8");

        String dataAsXml = stringWriter.toString();

        // the somewhat optimized order that DdlUtils currently generates is:
        // E1, G1, G2, A2, A1, B2, B1, C2, C1, D2, D1, F1
        // note that the order per table is the insertion order above
        SAXReader reader = new SAXReader();
        Document testDoc = reader.read(new InputSource(new StringReader(dataAsXml)));
        boolean uppercase = false;
        List<Node> rows = testDoc.selectNodes("/*/*");
        String pkColumnName = "pk";

        assertEquals(12, rows.size());
        if (!"e".equals(rows.get(0).getName())) {
            assertEquals("E", rows.get(0).getName());
            uppercase = true;
        }
        if (!"pk".equals(((Element) rows.get(0)).attribute(0).getName())) {
            pkColumnName = pkColumnName.toUpperCase();
        }
        assertEquals("1", ((Element) rows.get(0)).attributeValue(pkColumnName));

        // we cannot be sure of the order in which the database returns the rows
        // per table (some return them in pk order, some in insertion order)
        // so we don't assume an order in this test
        HashSet<String> pkValues = new HashSet<>();
        HashSet<String> expectedValues = new HashSet<>(Arrays.asList("1", "2"));

        assertEquals(uppercase ? "G" : "g", rows.get(1).getName());
        assertEquals(uppercase ? "G" : "g", rows.get(2).getName());
        pkValues.add(((Element) rows.get(1)).attributeValue(pkColumnName));
        pkValues.add(((Element) rows.get(2)).attributeValue(pkColumnName));
        assertEquals(pkValues, expectedValues);

        pkValues.clear();

        assertEquals(uppercase ? "A" : "a", rows.get(3).getName());
        assertEquals(uppercase ? "A" : "a", rows.get(4).getName());
        pkValues.add(((Element) rows.get(3)).attributeValue(pkColumnName));
        pkValues.add(((Element) rows.get(4)).attributeValue(pkColumnName));
        assertEquals(pkValues, expectedValues);

        pkValues.clear();

        assertEquals(uppercase ? "B" : "b", rows.get(5).getName());
        assertEquals(uppercase ? "B" : "b", rows.get(6).getName());
        pkValues.add(((Element) rows.get(5)).attributeValue(pkColumnName));
        pkValues.add(((Element) rows.get(6)).attributeValue(pkColumnName));
        assertEquals(pkValues, expectedValues);

        pkValues.clear();

        assertEquals(uppercase ? "C" : "c", rows.get(7).getName());
        assertEquals(uppercase ? "C" : "c", rows.get(8).getName());
        pkValues.add(((Element) rows.get(7)).attributeValue(pkColumnName));
        pkValues.add(((Element) rows.get(8)).attributeValue(pkColumnName));
        assertEquals(pkValues, expectedValues);

        pkValues.clear();

        assertEquals(uppercase ? "D" : "d", rows.get(9).getName());
        assertEquals(uppercase ? "D" : "d", rows.get(10).getName());
        pkValues.add(((Element) rows.get(9)).attributeValue(pkColumnName));
        pkValues.add(((Element) rows.get(10)).attributeValue(pkColumnName));
        assertEquals(pkValues, expectedValues);

        pkValues.clear();

        assertEquals(uppercase ? "F" : "f", rows.get(11).getName());
        assertEquals("1", ((Element) rows.get(11)).attributeValue(pkColumnName));

        dropDatabase();
        createDatabase(modelXml);

        StringReader stringReader = new StringReader(dataAsXml);

        dataIO.writeDataToDatabase(getPlatform(), getModel(), new Reader[]{stringReader});

        assertEquals(2, getRows("A").size());
        assertEquals(2, getRows("B").size());
        assertEquals(2, getRows("C").size());
        assertEquals(2, getRows("D").size());
        assertEquals(1, getRows("E").size());
        assertEquals(1, getRows("F").size());
        assertEquals(2, getRows("G").size());
    }

    /**
     * Test for DDLUTILS-178.
     */
    public void testDdlUtils178() throws Exception {
        if (!getPlatformInfo().isIndicesSupported()) {
            return;
        }

        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
              <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                <table name='ad_sequence_no'>
                  <column name='ad_sequence_id' required='true' type='NUMERIC' size='10'/>
                  <column name='ad_year' required='true' type='VARCHAR' size='4' default='0000'/>
                  <column name='ad_client_id' required='true' type='NUMERIC' size='10'/>
                  <unique name='ad_sequence_no_key'>
                    <unique-column name='ad_sequence_id'/>
                    <unique-column name='ad_year'/>
                  </unique>
                </table>
            </database>""";

        createDatabase(modelXml);

        assertEquals(getAdjustedModel(), readModelFromDatabase("roundtriptest"));
    }

    /**
     * Test for DDLUTILS-179.
     */
    public void testDdlUtils179() throws Exception {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='A'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='fk' type='INTEGER' required='false'/>
                <foreign-key name='AtoA' foreignTable='A'>
                  <reference local='fk' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='B'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='fk1' type='INTEGER' required='false'/>
                <column name='fk2' type='INTEGER' required='false'/>
                <foreign-key name='BtoB' foreignTable='B'>
                  <reference local='fk1' foreign='pk'/>
                </foreign-key>
                <foreign-key name='BtoG' foreignTable='G'>
                  <reference local='fk2' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='C'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='fk' type='INTEGER' required='false'/>
                <foreign-key name='CtoD' foreignTable='D'>
                  <reference local='fk' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='D'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='fk' type='INTEGER' required='false'/>
                <foreign-key name='DtoF' foreignTable='F'>
                  <reference local='fk' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='E'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
              <table name='F'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='fk' type='INTEGER' required='false'/>
                <foreign-key name='FtoC' foreignTable='C'>
                  <reference local='fk' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='G'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        createDatabase(modelXml);

        Database readModel = readModelFromDatabase("roundtriptest");

        assertEquals(getAdjustedModel(), readModel);

        File tmpFile = File.createTempFile("model", ".xml");

        try {
            DatabaseIO dbIO = new DatabaseIO();
            FileOutputStream out = new FileOutputStream(tmpFile);

            dbIO.write(readModel, out);
            out.flush();
            out.close();

            assertEquals(getAdjustedModel(), dbIO.read(tmpFile));
        } finally {
            if (!tmpFile.delete()) {
                getLog().warn("Could not delete the temporary file " + tmpFile.getAbsolutePath());
            }
        }
    }

    /**
     * Test for DDLUTILS-214.
     */
    public void testDdlUtils214() throws Exception {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='pk2' type='VARCHAR' primaryKey='true' required='true'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk2' type='VARCHAR' primaryKey='true' required='true'/>
                <column name='pk1' type='INTEGER' primaryKey='true' required='true'/>
              </table>
            </database>""";

        createDatabase(modelXml);

        Database readModel = readModelFromDatabase("roundtriptest");

        assertEquals(getAdjustedModel(), readModel);

        insertRow("roundtrip1", new Object[]{(1), "foo"});
        insertRow("roundtrip1", new Object[]{(2), "bar"});
        insertRow("roundtrip2", new Object[]{"foo", (1)});
        insertRow("roundtrip2", new Object[]{"bar", (2)});

        List<TableObject> beans1 = getRows("roundtrip1", "pk1");
        List<TableObject> beans2 = getRows("roundtrip2", "pk1");

        assertEquals(2, beans1.size());
        assertEquals(2, beans2.size());
        assertEquals((1), beans1.get(0), "pk1");
        assertEquals((Object) "foo", beans1.get(0), "pk2");
        assertEquals((2), beans1.get(1), "pk1");
        assertEquals((Object) "bar", beans1.get(1), "pk2");
        assertEquals((1), beans2.get(0), "pk1");
        assertEquals((Object) "foo", beans2.get(0), "pk2");
        assertEquals((2), beans2.get(1), "pk1");
        assertEquals((Object) "bar", beans2.get(1), "pk2");

        deleteRow("roundtrip1", new Object[]{(1), "foo"});
        deleteRow("roundtrip2", new Object[]{"foo", (1)});

        beans1 = getRows("roundtrip1", "pk1");
        beans2 = getRows("roundtrip2", "pk1");

        assertEquals(1, beans1.size());
        assertEquals(1, beans2.size());
        assertEquals((2), beans1.get(0), "pk1");
        assertEquals((Object) "bar", beans1.get(0), "pk2");
        assertEquals((2), beans2.get(0), "pk1");
        assertEquals((Object) "bar", beans2.get(0), "pk2");
    }

    /**
     * Test for DDLUTILS-227.
     */
    public void testDdlUtils227() throws Exception {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip'>
                <column name='Pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Avalue' type='VARCHAR'/>
              </table>
            </database>""";

        createDatabase(modelXml);

        Database readModel = readModelFromDatabase("roundtriptest");

        assertEquals(getAdjustedModel(), readModel);

        insertRow("Roundtrip", new Object[]{(1), "foo"});

        List<TableObject> beans = getRows("Roundtrip");

        assertEquals(1, beans.size());
        assertEquals((1), beans.get(0), "Pk");
        assertEquals((Object) "foo", beans.get(0), "Avalue");

        Table table = getModel().findTable("Roundtrip", getPlatform().isDelimitedIdentifierModeOn());
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM (SELECT * FROM ");
        if (getPlatform().isDelimitedIdentifierModeOn()) {
            query.append(getPlatformInfo().getDelimiterToken());
        }
        query.append(table.getName());
        if (getPlatform().isDelimitedIdentifierModeOn()) {
            query.append(getPlatformInfo().getDelimiterToken());
        }
        query.append(")");
        // Some JDBC drivers do not allow us to perform the query without an explicit alias
        if (DBTypeEnum.MYSQL.getName().equals(getPlatform().getName()) || DBTypeEnum.MYSQL5.getName().equals(getPlatform().getName()) || DBTypeEnum.POSTGRE_SQL.getName().equals(getPlatform().getName()) || DBTypeEnum.DERBY.getName().equals(getPlatform().getName()) || DBTypeEnum.MSSQL.getName().equals(getPlatform().getName())) {
            query.append(" AS ");
            if (getPlatform().isDelimitedIdentifierModeOn()) {
                query.append(getPlatformInfo().getDelimiterToken());
            }
            query.append(table.getName());
            if (getPlatform().isDelimitedIdentifierModeOn()) {
                query.append(getPlatformInfo().getDelimiterToken());
            }
        }

        beans = getPlatform().fetch(getModel(), query.toString(), new Table[]{table});

        assertEquals(1, beans.size());
        assertEquals((1), beans.get(0), "Pk");
        assertEquals((Object) "foo", beans.get(0), "Avalue");
    }
}
