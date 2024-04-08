package org.apache.ddlutils.io;


import org.apache.ddlutils.dynabean.SqlDynaBean;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Tests the {@link org.apache.ddlutils.io.DataReader} and {@link org.apache.ddlutils.io.DataWriter} classes.
 */
public class TestDataReaderAndWriter {
    /**
     * Reads the given schema xml into a {@link Database} object.
     *
     * @param schemaXml The schema xml
     * @return The database model object
     */
    private Database readModel(String schemaXml) {
        DatabaseIO modelIO = new DatabaseIO();

        modelIO.setValidateXml(true);

        return modelIO.read(new StringReader(schemaXml));
    }

    /**
     * Writes the given dyna bean via a {@link DataWriter} and returns the raw xml output.
     *
     * @param model    The database model to use
     * @param bean     The bean to write
     * @param encoding The encoding in which to write the xml
     * @return The xml output as raw bytes
     */
    private byte[] writeBean(Database model, SqlDynaBean bean, String encoding) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        DataWriter dataWriter = new DataWriter(output, encoding);

        dataWriter.writeDocumentStart();
        dataWriter.write(bean);
        dataWriter.writeDocumentEnd();

        return output.toByteArray();
    }

    /**
     * Uses a {@link DataReader} with default settings to read dyna beans from the given xml data.
     *
     * @param model   The database model to use
     * @param dataXml The raw xml data
     * @return The read dyna beans
     */
    private List<SqlDynaBean> readBeans(Database model, byte[] dataXml) {
        List<SqlDynaBean> beans = new ArrayList<>();
        DataReader dataReader = new DataReader();

        dataReader.setModel(model);
        dataReader.setSink(new TestDataSink(beans));
        dataReader.read(new ByteArrayInputStream(dataXml));
        return beans;
    }

    /**
     * Uses a {@link DataReader} with default settings to read dyna beans from the given xml data.
     *
     * @param model   The database model to use
     * @param dataXml The xml data
     * @return The read dyna beans
     */
    private List<SqlDynaBean> readBeans(Database model, String dataXml) {
        List<SqlDynaBean> beans = new ArrayList<>();
        DataReader dataReader = new DataReader();

        dataReader.setModel(model);
        dataReader.setSink(new TestDataSink(beans));
        dataReader.read(new StringReader(dataXml));
        return beans;
    }

    /**
     * Helper method to perform a test that writes a bean and then reads it back.
     *
     * @param model           The database model to use
     * @param bean            The bean to write and read back
     * @param encoding        The encoding to use for the data xml
     * @param expectedDataXml The expected xml generated for the bean
     */
    private void roundtripTest(Database model, SqlDynaBean bean, String encoding, String expectedDataXml) throws UnsupportedEncodingException {
        byte[] xmlData = writeBean(model, bean, encoding);

        Assertions.assertEquals(expectedDataXml, new String(xmlData, encoding));

        List<SqlDynaBean> beans = readBeans(model, xmlData);

        Assertions.assertEquals(1, beans.size());
        Assertions.assertEquals(bean, beans.get(0));
    }

    /**
     * Tests reading the data from XML.
     */
    @Test
    public void testRead() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='bookstore'>
              <table name='author'>
                <column name='author_id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='name' type='VARCHAR' size='50' required='true'/>
                <column name='organisation' type='VARCHAR' size='50' required='false'/>
              </table>
              <table name='book'>
                <column name='book_id' type='INTEGER' required='true' primaryKey='true' autoIncrement='true'/>
                <column name='isbn' type='VARCHAR' size='15' required='true'/>
                <column name='author_id' type='INTEGER' required='true'/>
                <column name='title' type='VARCHAR' size='255' default='N/A' required='true'/>
                <column name='issue_date' type='DATE' required='false'/>
                <foreign-key foreignTable='author'>
                  <reference local='author_id' foreign='author_id'/>
                </foreign-key>
                <index name='book_isbn'>
                  <index-column name='isbn'/>
                </index>
              </table>
            </database>""");
        List<SqlDynaBean> beans = readBeans(model, """
            <data>
              <author author_id='1' name='Ernest Hemingway'/>
              <author author_id='2' name='William Shakespeare'/>
              <book book_id='1' author_id='1'>
                <isbn>0684830493</isbn>
                <title>Old Man And The Sea</title>
                <issue_date>1952</issue_date>
              </book>
              <book book_id='2' author_id='2'>
                <isbn>0198321465</isbn>
                <title>Macbeth</title>
                <issue_date>1606</issue_date>
              </book>
              <book book_id='3' author_id='2'>
                <isbn>0140707026</isbn>
                <title>A Midsummer Night's Dream</title>
                <issue_date>1595</issue_date>
              </book>
            </data>""");

        Assertions.assertEquals(5, beans.size());

        SqlDynaBean obj1 = beans.get(0);
        SqlDynaBean obj2 = beans.get(1);
        SqlDynaBean obj3 = beans.get(2);
        SqlDynaBean obj4 = beans.get(3);
        SqlDynaBean obj5 = beans.get(4);

        Assertions.assertEquals("author", obj1.getDynaClass().getName());
        Assertions.assertEquals("1", obj1.get("author_id").toString());
        Assertions.assertEquals("Ernest Hemingway", obj1.get("name").toString());
        Assertions.assertEquals("author", obj2.getDynaClass().getName());
        Assertions.assertEquals("2", obj2.get("author_id").toString());
        Assertions.assertEquals("William Shakespeare", obj2.get("name").toString());
        Assertions.assertEquals("book", obj3.getDynaClass().getName());
        Assertions.assertEquals("1", obj3.get("book_id").toString());
        Assertions.assertEquals("1", obj3.get("author_id").toString());
        Assertions.assertEquals("0684830493", obj3.get("isbn").toString());
        Assertions.assertEquals("Old Man And The Sea", obj3.get("title").toString());
        Assertions.assertEquals("1952-01-01", obj3.get("issue_date").toString());    // parsed as a java.sql.Date
        Assertions.assertEquals("book", obj4.getDynaClass().getName());
        Assertions.assertEquals("2", obj4.get("book_id").toString());
        Assertions.assertEquals("2", obj4.get("author_id").toString());
        Assertions.assertEquals("0198321465", obj4.get("isbn").toString());
        Assertions.assertEquals("Macbeth", obj4.get("title").toString());
        Assertions.assertEquals("1606-01-01", obj4.get("issue_date").toString());    // parsed as a java.sql.Date
        Assertions.assertEquals("book", obj5.getDynaClass().getName());
        Assertions.assertEquals("3", obj5.get("book_id").toString());
        Assertions.assertEquals("2", obj5.get("author_id").toString());
        Assertions.assertEquals("0140707026", obj5.get("isbn").toString());
        Assertions.assertEquals("A Midsummer Night's Dream", obj5.get("title").toString());
        Assertions.assertEquals("1595-01-01", obj5.get("issue_date").toString());    // parsed as a java.sql.Date
    }

    /**
     * Tests reading the data from a file via the {#link {@link DataReader#read(String)} method.
     */
    @Test
    public void testReadFromFile1() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testDataXml = """
            <data>
              <test id='1' value='foo'/>
            </data>""";

        File tmpFile = File.createTempFile("data", ".xml");

        try {
            Writer writer = new BufferedWriter(new FileWriter(tmpFile));

            writer.write(testDataXml);
            writer.close();

            List<SqlDynaBean> beans = new ArrayList<>();
            DataReader dataReader = new DataReader();

            dataReader.setModel(model);
            dataReader.setSink(new TestDataSink(beans));
            dataReader.read(tmpFile.getAbsolutePath());

            Assertions.assertEquals(1, beans.size());

            SqlDynaBean obj = beans.get(0);

            Assertions.assertEquals("test", obj.getDynaClass().getName());
            Assertions.assertEquals("1", obj.get("id").toString());
            Assertions.assertEquals("foo", obj.get("value").toString());
        } finally {
            tmpFile.delete();
        }
    }

    /**
     * Tests reading the data from a file via the {#link {@link DataReader#read(File)} method.
     */
    @Test
    public void testReadFromFile2() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testDataXml = """
            <data>
              <test id='1' value='foo'/>
            </data>""";

        File tmpFile = File.createTempFile("data", ".xml");

        try {
            Writer writer = new BufferedWriter(new FileWriter(tmpFile));

            writer.write(testDataXml);
            writer.close();

            List<SqlDynaBean> beans = new ArrayList<>();
            DataReader dataReader = new DataReader();

            dataReader.setModel(model);
            dataReader.setSink(new TestDataSink(beans));
            dataReader.read(tmpFile);

            Assertions.assertEquals(1, beans.size());

            SqlDynaBean obj = beans.get(0);

            Assertions.assertEquals("test", obj.getDynaClass().getName());
            Assertions.assertEquals("1", obj.get("id").toString());
            Assertions.assertEquals("foo", obj.get("value").toString());
        } finally {
            tmpFile.delete();
        }
    }

    /**
     * Tests reading the data from a file via the {#link {@link DataReader#read(java.io.InputStream)} method.
     */
    @Test
    public void testReadFromFile3() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testDataXml = """
            <data>
              <test id='1' value='foo'/>
            </data>""";

        File tmpFile = File.createTempFile("data", ".xml");

        try {
            Writer writer = new BufferedWriter(new FileWriter(tmpFile));

            writer.write(testDataXml);
            writer.close();

            List<SqlDynaBean> beans = new ArrayList<>();
            DataReader dataReader = new DataReader();

            dataReader.setModel(model);
            dataReader.setSink(new TestDataSink(beans));
            dataReader.read(new FileInputStream(tmpFile));

            Assertions.assertEquals(1, beans.size());

            SqlDynaBean obj = beans.get(0);

            Assertions.assertEquals("test", obj.getDynaClass().getName());
            Assertions.assertEquals("1", obj.get("id").toString());
            Assertions.assertEquals("foo", obj.get("value").toString());
        } finally {
            tmpFile.delete();
        }
    }

    /**
     * Tests sub elements for columns.
     */
    @Test
    public void testSubElements() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        List<SqlDynaBean> beans = readBeans(model, """
            <data>
              <test id='1'>
                <value>foo</value>
              </test>
              <test id='2' value='foo'>
                <value>bar</value>
              </test>
              <test id='3' value='baz'>
              </test>
            </data>""");

        Assertions.assertEquals(3, beans.size());

        SqlDynaBean obj = beans.get(0);

        Assertions.assertEquals("test", obj.getDynaClass().getName());
        Assertions.assertEquals("1", obj.get("id").toString());
        Assertions.assertEquals("foo", obj.get("value").toString());

        obj = beans.get(1);

        Assertions.assertEquals("test", obj.getDynaClass().getName());
        Assertions.assertEquals("2", obj.get("id").toString());
        Assertions.assertEquals("bar", obj.get("value").toString());

        obj = beans.get(2);

        Assertions.assertEquals("test", obj.getDynaClass().getName());
        Assertions.assertEquals("3", obj.get("id").toString());
        Assertions.assertEquals("baz", obj.get("value").toString());
    }

    /**
     * Tests that the name of the root element does not matter.
     */
    @Test
    public void testRootElementNameDoesntMatter() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        List<SqlDynaBean> beans = readBeans(model, """
            <someRandomName>
              <test id='1' value='foo'/>
            </someRandomName>""");

        Assertions.assertEquals(1, beans.size());

        SqlDynaBean obj = beans.get(0);

        Assertions.assertEquals("test", obj.getDynaClass().getName());
        Assertions.assertEquals("1", obj.get("id").toString());
        Assertions.assertEquals("foo", obj.get("value").toString());
    }

    /**
     * Tests that elements for undefined tables are ignored.
     */
    @Test
    public void testElementForUndefinedTable() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        List<SqlDynaBean> beans = readBeans(model, """
            <data>
              <test id='1' value='foo'/>
              <other id='2' value='bar'/>
              <test id='3' value='baz'/>
            </data>""");

        Assertions.assertEquals(2, beans.size());

        SqlDynaBean obj = beans.get(0);

        Assertions.assertEquals("test", obj.getDynaClass().getName());
        Assertions.assertEquals("1", obj.get("id").toString());
        Assertions.assertEquals("foo", obj.get("value").toString());

        obj = beans.get(1);

        Assertions.assertEquals("test", obj.getDynaClass().getName());
        Assertions.assertEquals("3", obj.get("id").toString());
        Assertions.assertEquals("baz", obj.get("value").toString());
    }

    /**
     * Tests that attributes for which no column is defined, are ignored.
     */
    @Test
    public void testAttributeForUndefinedColumn() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        List<SqlDynaBean> beans = readBeans(model, """
            <data>
              <test id='1' value1='foo'/>
            </data>""");

        Assertions.assertEquals(1, beans.size());

        SqlDynaBean obj = beans.get(0);

        Assertions.assertEquals("test", obj.getDynaClass().getName());
        Assertions.assertEquals("1", obj.get("id").toString());
        Assertions.assertNull(obj.get("value"));
    }

    /**
     * Tests that sub elements for which no column is defined, are ignored.
     */
    @Test
    public void testSubElementForUndefinedColumn() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        List<SqlDynaBean> beans = readBeans(model, """
            <data>
              <test id='1'>
                <value2>foo</value2>
              </test>
            </data>""");

        Assertions.assertEquals(1, beans.size());

        SqlDynaBean obj = beans.get(0);

        Assertions.assertEquals("test", obj.getDynaClass().getName());
        Assertions.assertEquals("1", obj.get("id").toString());
        Assertions.assertNull(obj.get("value"));
    }

    /**
     * Tests parsing when case sensitivity is turned on.
     */
    @Test
    public void testCaseSensitivityTurnedOn() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='Test'>
                <column name='Id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testDataXml = """
            <data>
              <test Id='1' Value='foo'/>
              <Test Id='2' value='baz'/>
            </data>""";

        List<SqlDynaBean> beans = new ArrayList<>();
        DataReader dataReader = new DataReader();

        dataReader.setCaseSensitive(true);
        dataReader.setModel(model);
        dataReader.setSink(new TestDataSink(beans));
        dataReader.read(new StringReader(testDataXml));

        Assertions.assertEquals(1, beans.size());

        SqlDynaBean obj = beans.get(0);

        Assertions.assertEquals("Test", obj.getDynaClass().getName());
        Assertions.assertEquals("2", obj.get("Id").toString());
        Assertions.assertNull(obj.get("Value"));
    }

    /**
     * Tests parsing when case sensitivity is turned off.
     */
    @Test
    public void testCaseSensitivityTurnedOff() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='Test'>
                <column name='Id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testDataXml = """
            <data>
              <test Id='1' Value='foo'/>
              <Test Id='2' value='bar'/>
              <Test id='3' Value='baz'/>
            </data>""";

        List<SqlDynaBean> beans = new ArrayList<>();
        DataReader dataReader = new DataReader();

        dataReader.setCaseSensitive(false);
        dataReader.setModel(model);
        dataReader.setSink(new TestDataSink(beans));
        dataReader.read(new StringReader(testDataXml));

        Assertions.assertEquals(3, beans.size());

        SqlDynaBean obj = beans.get(0);

        Assertions.assertEquals("Test", obj.getDynaClass().getName());
        Assertions.assertEquals("1", obj.get("Id").toString());
        Assertions.assertEquals("foo", obj.get("Value").toString());

        obj = beans.get(1);

        Assertions.assertEquals("Test", obj.getDynaClass().getName());
        Assertions.assertEquals("2", obj.get("Id").toString());
        Assertions.assertEquals("bar", obj.get("Value").toString());

        obj = beans.get(2);

        Assertions.assertEquals("Test", obj.getDynaClass().getName());
        Assertions.assertEquals("3", obj.get("Id").toString());
        Assertions.assertEquals("baz", obj.get("Value").toString());
    }

    /**
     * Tests special characters in the data XML (for DDLUTILS-63).
     */
    @Test
    public void testSpecialCharacters() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testedValue = "Some Special Characters: \u0001\u0009\u0010";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("value", testedValue);


        String encoded = base64Encode(testedValue);

        roundtripTest(model, bean, "ISO-8859-1", """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <data>
              <test id="1">
                <value %s="true">%s</value>
              </test>
            </data>
            """.formatted(DatabaseIO.BASE64_ATTR_NAME, encoded));
    }

    /**
     * Tests special characters in the data XML (for DDLUTILS-233).
     */
    @Test
    public void testSpecialCharactersUTF8() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='UTF-8'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        // \t = \u0009
        String testedValue = "Some Special Characters: \u0001\t\u0010";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("value", testedValue);

        roundtripTest(model, bean, "ISO-8859-1", "<?xml version='1.0' encoding='ISO-8859-1'?>\n" + "<data>\n" + "  <test id=\"1\">\n" + "    <value " + DatabaseIO.BASE64_ATTR_NAME + "=\"true\">" + base64Encode(testedValue) + "</value>\n" + "  </test>\n" + "</data>\n");
    }

    /**
     * Tests a cdata section (see DDLUTILS-174).
     */
    @Test
    public void testCData() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value1' type='VARCHAR' size='50' required='true'/>
                <column name='value2' type='VARCHAR' size='4000' required='true'/>
                <column name='value3' type='LONGVARCHAR' size='4000' required='true'/>
                <column name='value4' type='LONGVARCHAR' size='4000' required='true'/>
                <column name='value5' type='LONGVARCHAR' size='4000' required='true'/>
              </table>
            </database>""");
        String testedValue1 = "<?xml version='1.0' encoding='ISO-8859-1'?><test><![CDATA[some text]]></test>";
        String testedValue2 = StringUtils.repeat("a ", 1000) + testedValue1;
        String testedValue3 = "<div>\n<h1><![CDATA[WfMOpen]]></h1>\n" + StringUtils.repeat("Make it longer\n", 99) + "</div>";
        String testedValue4 = "<![CDATA[" + StringUtils.repeat("b \n", 1000) + "]]>";
        String testedValue5 = "<<![CDATA[" + StringUtils.repeat("b \n", 500) + "]]>><![CDATA[" + StringUtils.repeat("c \n", 500) + "]]>";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("value1", testedValue1);
        bean.set("value2", testedValue2);
        bean.set("value3", testedValue3);
        bean.set("value4", testedValue4);
        bean.set("value5", testedValue5);

        byte[] xmlData = writeBean(model, bean, "UTF-8");
        List<SqlDynaBean> beans = readBeans(model, xmlData);

        Assertions.assertEquals(1, beans.size());
        Assertions.assertEquals(bean, beans.get(0));
    }

    /**
     * Tests the reader & writer behavior when the table name is not a valid XML identifier.
     */
    @Test
    public void testTableNameLong() throws Exception {
        String tableName = StringUtils.repeat("test", 100);
        Database model = readModel("<?xml version='1.0' encoding='UTF-8'?>\n" + "<database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>\n" + "  <table name='" + tableName + "'>\n" + "    <column name='id' type='INTEGER' primaryKey='true' required='true'/>\n" + "    <column name='value' type='VARCHAR' size='50' required='true'/>\n" + "  </table>\n" + "</database>");
        String testedValue = "Some Text";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("value", testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <table id=\"1\" value=\"" + testedValue + "\">\n" + "    <table-name>" + tableName + "</table-name>\n" + "  </table>\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when the table name is not a valid XML identifier.
     */
    @Test
    public void testTableNameNotAValidXmlIdentifier() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='UTF-8'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test$'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testedValue = "Some Text";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("value", testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <table table-name=\"test$\" id=\"1\" value=\"" + testedValue + "\" />\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when the table name is not a valid XML identifier and too long.
     */
    @Test
    public void testTableNameInvalidAndLong() throws Exception {
        String tableName = StringUtils.repeat("table name", 50);
        Database model = readModel("<?xml version='1.0' encoding='UTF-8'?>\n" + "<database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>\n" + "  <table name='" + tableName + "'>\n" + "    <column name='id' type='INTEGER' primaryKey='true' required='true'/>\n" + "    <column name='value' type='VARCHAR' size='50' required='true'/>\n" + "  </table>\n" + "</database>");
        String testedValue = "Some Text";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("value", testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <table id=\"1\" value=\"" + testedValue + "\">\n" + "    <table-name>" + tableName + "</table-name>\n" + "  </table>\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when the table name contains a '&' character.
     */
    @Test
    public void testTableNameContainsAmpersand() throws Exception {
        String tableName = "test&table";
        Database model = new Database("test");
        Table table = new Table();
        Column idColumn = new Column();
        Column valueColumn = new Column();

        idColumn.setName("id");
        idColumn.setType("INTEGER");
        idColumn.setPrimaryKey(true);
        idColumn.setRequired(true);
        valueColumn.setName("value");
        valueColumn.setType("VARCHAR");
        valueColumn.setSize("50");
        valueColumn.setRequired(true);
        table.setName(tableName);
        table.addColumn(idColumn);
        table.addColumn(valueColumn);
        model.addTable(table);

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));
        String testedValue = "Some Text";

        bean.set("id", (1));
        bean.set("value", testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <table table-name=\"test&amp;table\" id=\"1\" value=\"" + testedValue + "\" />\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when the table name contains a '<' character.
     */
    @Test
    public void testTableNameContainsLessCharacter() throws Exception {
        String tableName = "test<table";
        Database model = new Database("test");
        Table table = new Table();
        Column idColumn = new Column();
        Column valueColumn = new Column();

        idColumn.setName("id");
        idColumn.setType("INTEGER");
        idColumn.setPrimaryKey(true);
        idColumn.setRequired(true);
        valueColumn.setName("value");
        valueColumn.setType("VARCHAR");
        valueColumn.setSize("50");
        valueColumn.setRequired(true);
        table.setName(tableName);
        table.addColumn(idColumn);
        table.addColumn(valueColumn);
        model.addTable(table);

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));
        String testedValue = "Some Text";

        bean.set("id", (1));
        bean.set("value", testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <table table-name=\"test&lt;table\" id=\"1\" value=\"" + testedValue + "\" />\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when the table name contains a '>' character.
     */
    @Test
    public void testTableNameContainsMoreCharacter() throws Exception {
        String tableName = "test>table";
        Database model = new Database("test");
        Table table = new Table();
        Column idColumn = new Column();
        Column valueColumn = new Column();

        idColumn.setName("id");
        idColumn.setType("INTEGER");
        idColumn.setPrimaryKey(true);
        idColumn.setRequired(true);
        valueColumn.setName("value");
        valueColumn.setType("VARCHAR");
        valueColumn.setSize("50");
        valueColumn.setRequired(true);
        table.setName(tableName);
        table.addColumn(idColumn);
        table.addColumn(valueColumn);
        model.addTable(table);

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));
        String testedValue = "Some Text";

        bean.set("id", (1));
        bean.set("value", testedValue);

        roundtripTest(model, bean, "UTF-8", """
            <?xml version='1.0' encoding='UTF-8'?>
            <data>
              <table table-name="test>table" id="1" value="%s" />
            </data>
            """.formatted(testedValue));
    }

    /**
     * Tests the reader & writer behavior when the table name contains characters not allowed in XML.
     */
    @Test
    public void testTableNameContainsInvalidCharacters() throws Exception {
        String tableName = "test\u0000table";
        Database model = new Database("test");
        Table table = new Table();
        Column idColumn = new Column();
        Column valueColumn = new Column();

        idColumn.setName("id");
        idColumn.setType("INTEGER");
        idColumn.setPrimaryKey(true);
        idColumn.setRequired(true);
        valueColumn.setName("value");
        valueColumn.setType("VARCHAR");
        valueColumn.setSize("50");
        valueColumn.setRequired(true);
        table.setName(tableName);
        table.addColumn(idColumn);
        table.addColumn(valueColumn);
        model.addTable(table);

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));
        String testedValue = "Some Text";

        bean.set("id", (1));
        bean.set("value", testedValue);

        roundtripTest(model, bean, "UTF-8", """
            <?xml version='1.0' encoding='UTF-8'?>
            <data>
              <table id="1" value="%s">
                <table-name %s="true">%s</table-name>
              </table>
            </data>
            """.formatted(testedValue, DatabaseIO.BASE64_ATTR_NAME, base64Encode(tableName)));
    }

    public String base64Encode(String src) {
        return new String(Base64.getEncoder().encode(src.getBytes(StandardCharsets.UTF_8)), StandardCharsets.ISO_8859_1);
    }

    /**
     * Tests the reader & writer behavior when the table name is 'table'.
     */
    @Test
    public void testTableNameIsTable() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='UTF-8'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='table'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testedValue = "Some Text";

        DatabaseIO modelIO = new DatabaseIO();

        modelIO.setValidateXml(true);

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("value", testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <table table-name=\"table\" id=\"1\" value=\"" + testedValue + "\" />\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column name is a normal valid tag,
     * and both column name and value are shorter than 255 characters.
     */
    @Test
    public void testColumnNameAndValueShort() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='UTF-8'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testedValue = "Some Text";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("value", testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <test id=\"1\" value=\"" + testedValue + "\" />\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column name is a normal valid tag,
     * and the column name is shorter than 255 characters but the value is longer.
     */
    @Test
    public void testColumnNameShortAndValueLong() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='UTF-8'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value' type='VARCHAR' size='400' required='true'/>
              </table>
            </database>""");
        String testedValue = StringUtils.repeat("Some Text", 40);

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("value", testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <test id=\"1\">\n" + "    <value>" + testedValue + "</value>\n" + "  </test>\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column name is not a valid XML identifier.
     */
    @Test
    public void testColumnNameShortAndInvalidAndValueShort() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='UTF-8'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='the value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testedValue = "Some Text";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("the value", testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <test id=\"1\">\n" + "    <column column-name=\"the value\">" + testedValue + "</column>\n" + "  </test>\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column name is not a valid tag,
     * and the column name is shorter than 255 characters and the value is longer.
     */
    @Test
    public void testColumnNameShortAndInvalidAndValueLong() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='UTF-8'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='the value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testedValue = StringUtils.repeat("Some Text", 40);

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("the value", testedValue);

        roundtripTest(model, bean, "UTF-8", """
            <?xml version='1.0' encoding='UTF-8'?>
            <data>
              <test id="1">
                <column column-name="the value">%s</column>
              </test>
            </data>
            """.formatted(testedValue));
    }

    /**
     * Tests the reader & writer behavior when a column name is a valid tag,
     * and the column name is longer than 255 characters and the value is shorter.
     */
    @Test
    public void testColumnNameLongAndValueShort() throws Exception {
        String columnName = StringUtils.repeat("value", 100);
        Database model = readModel("<?xml version='1.0' encoding='UTF-8'?>\n" + "<database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>\n" + "  <table name='test'>\n" + "    <column name='id' type='INTEGER' primaryKey='true' required='true'/>\n" + "    <column name='" + columnName + "' type='VARCHAR' size='50' required='true'/>\n" + "  </table>\n" + "</database>");
        String testedValue = "Some Text";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set(columnName, testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <test id=\"1\">\n" + "    <column>\n" + "      <column-name>" + columnName + "</column-name>\n" + "      <column-value>" + testedValue + "</column-value>\n" + "    </column>\n" + "  </test>\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column name is a valid tag,
     * and both the column name and value are longer than 255 characters.
     */
    @Test
    public void testColumnNameLongAndValueLong() throws Exception {
        String columnName = StringUtils.repeat("value", 100);
        Database model = readModel("<?xml version='1.0' encoding='UTF-8'?>\n" + "<database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>\n" + "  <table name='test'>\n" + "    <column name='id' type='INTEGER' primaryKey='true' required='true'/>\n" + "    <column name='" + columnName + "' type='VARCHAR' size='500' required='true'/>\n" + "  </table>\n" + "</database>");
        String testedValue = StringUtils.repeat("Some Text", 40);

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set(columnName, testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <test id=\"1\">\n" + "    <column>\n" + "      <column-name>" + columnName + "</column-name>\n" + "      <column-value>" + testedValue + "</column-value>\n" + "    </column>\n" + "  </test>\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column name is a valid tag,
     * and the column name is longer than 255 characters and the value is shorter.
     */
    @Test
    public void testColumnNameAndValueLong() throws Exception {
        String columnName = StringUtils.repeat("value", 100);
        Database model = readModel("<?xml version='1.0' encoding='UTF-8'?>\n" + "<database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>\n" + "  <table name='test'>\n" + "    <column name='id' type='INTEGER' primaryKey='true' required='true'/>\n" + "    <column name='" + columnName + "' type='VARCHAR' size='50' required='true'/>\n" + "  </table>\n" + "</database>");
        String testedValue = "Some Text";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set(columnName, testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <test id=\"1\">\n" + "    <column>\n" + "      <column-name>" + columnName + "</column-name>\n" + "      <column-value>" + testedValue + "</column-value>\n" + "    </column>\n" + "  </test>\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column name is not a valid tag,
     * and the value is invalid, and both are short.
     */
    @Test
    public void testColumnNameAndValueShortAndInvalid() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='UTF-8'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='the value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testedValue = "the\u0000value";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("the value", testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <test id=\"1\">\n" + "    <column column-name=\"the value\" " + DatabaseIO.BASE64_ATTR_NAME + "=\"true\">" + base64Encode(testedValue) + "</column>\n" + "  </test>\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column name is a valid tag and longer,
     * than 255 characters, and the value is invalid and shorter than 255 characters.
     */
    @Test
    public void testColumnNameLongAndValueInvalidAndShort() throws Exception {
        String columnName = StringUtils.repeat("value", 100);
        Database model = readModel("<?xml version='1.0' encoding='UTF-8'?>\n" + "<database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>\n" + "  <table name='test'>\n" + "    <column name='id' type='INTEGER' primaryKey='true' required='true'/>\n" + "    <column name='" + columnName + "' type='VARCHAR' size='50' required='true'/>\n" + "  </table>\n" + "</database>");
        String testedValue = "the\u0000value";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set(columnName, testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <test id=\"1\">\n" + "    <column>\n" + "      <column-name>" + columnName + "</column-name>\n" + "      <column-value " + DatabaseIO.BASE64_ATTR_NAME + "=\"true\">" + base64Encode(testedValue) + "</column-value>\n" + "    </column>\n" + "  </test>\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column name is not a valid tag,
     * and the value is invalid, and both are short.
     */
    @Test
    public void testColumnNameAndValueLongAndInvalid() throws Exception {
        Database model = new Database("test");
        Table table = new Table();
        Column idColumn = new Column();
        Column valueColumn = new Column();
        String columnName = StringUtils.repeat("the\u0000name", 100);

        idColumn.setName("id");
        idColumn.setType("INTEGER");
        idColumn.setPrimaryKey(true);
        idColumn.setRequired(true);
        valueColumn.setName(columnName);
        valueColumn.setType("VARCHAR");
        valueColumn.setSize("50");
        valueColumn.setRequired(true);
        table.setName("test");
        table.addColumn(idColumn);
        table.addColumn(valueColumn);
        model.addTable(table);

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));
        String testedValue = StringUtils.repeat("the\u0000value", 40);

        bean.set("id", (1));
        bean.set(columnName, testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <test id=\"1\">\n" + "    <column>\n" + "      <column-name " + DatabaseIO.BASE64_ATTR_NAME + "=\"true\">" + base64Encode(columnName) + "</column-name>\n" + "      <column-value " + DatabaseIO.BASE64_ATTR_NAME + "=\"true\">" + base64Encode(testedValue) + "</column-value>\n" + "    </column>\n" + "  </test>\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column name contains an invalid character.
     */
    @Test
    public void testColumnNameContainsInvalidCharacters() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='UTF-8'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='value' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testedValue = "the\u0000value";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("value", testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <test id=\"1\">\n" + "    <value " + DatabaseIO.BASE64_ATTR_NAME + "=\"true\">" + base64Encode(testedValue) + "</value>\n" + "  </test>\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column value contains an invalid character.
     */
    @Test
    public void testColumnValueContainsInvalidCharacters() throws Exception {
        Database model = new Database("test");
        Table table = new Table();
        Column idColumn = new Column();
        Column valueColumn = new Column();
        String columnName = "the\u0000value";

        idColumn.setName("id");
        idColumn.setType("INTEGER");
        idColumn.setPrimaryKey(true);
        idColumn.setRequired(true);
        valueColumn.setName(columnName);
        valueColumn.setType("VARCHAR");
        valueColumn.setSize("50");
        valueColumn.setRequired(true);
        table.setName("test");
        table.addColumn(idColumn);
        table.addColumn(valueColumn);
        model.addTable(table);

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));
        String testedValue = "Some Text";

        bean.set("id", (1));
        bean.set(columnName, testedValue);
        roundtripTest(model, bean, "UTF-8", """
            <?xml version='1.0' encoding='UTF-8'?>
            <data>
              <test id="1">
                <column>
                  <column-name %s="true">%s</column-name>
                  <column-value>%s</column-value>
                </column>
              </test>
            </data>
            """.formatted(DatabaseIO.BASE64_ATTR_NAME, base64Encode(columnName), testedValue));
    }

    /**
     * Tests the reader & writer behavior when a column value contains the '&' character.
     */
    @Test
    public void testColumnValueContainsAmpersand() throws Exception {
        Database model = new Database("test");
        Table table = new Table();
        Column idColumn = new Column();
        Column valueColumn = new Column();
        String columnName = "foo&bar";

        idColumn.setName("id");
        idColumn.setType("INTEGER");
        idColumn.setPrimaryKey(true);
        idColumn.setRequired(true);
        valueColumn.setName(columnName);
        valueColumn.setType("VARCHAR");
        valueColumn.setSize("50");
        valueColumn.setRequired(true);
        table.setName("test");
        table.addColumn(idColumn);
        table.addColumn(valueColumn);
        model.addTable(table);

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));
        String testedValue = "Some Text";

        bean.set("id", (1));
        bean.set(columnName, testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <test id=\"1\">\n" + "    <column column-name=\"foo&amp;bar\">" + testedValue + "</column>\n" + "  </test>\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column value contains the '<' character.
     */
    @Test
    public void testColumnValueContainsLessCharacter() throws Exception {
        Database model = new Database("test");
        Table table = new Table();
        Column idColumn = new Column();
        Column valueColumn = new Column();
        String columnName = "foo<bar";

        idColumn.setName("id");
        idColumn.setType("INTEGER");
        idColumn.setPrimaryKey(true);
        idColumn.setRequired(true);
        valueColumn.setName(columnName);
        valueColumn.setType("VARCHAR");
        valueColumn.setSize("50");
        valueColumn.setRequired(true);
        table.setName("test");
        table.addColumn(idColumn);
        table.addColumn(valueColumn);
        model.addTable(table);

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));
        String testedValue = "Some Text";

        bean.set("id", (1));
        bean.set(columnName, testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <test id=\"1\">\n" + "    <column column-name=\"foo&lt;bar\">" + testedValue + "</column>\n" + "  </test>\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column value contains the '>' character.
     */
    @Test
    public void testColumnValueContainsMoreCharacter() throws Exception {
        Database model = new Database("test");
        Table table = new Table();
        Column idColumn = new Column();
        Column valueColumn = new Column();
        String columnName = "foo>bar";

        idColumn.setName("id");
        idColumn.setType("INTEGER");
        idColumn.setPrimaryKey(true);
        idColumn.setRequired(true);
        valueColumn.setName(columnName);
        valueColumn.setType("VARCHAR");
        valueColumn.setSize("50");
        valueColumn.setRequired(true);
        table.setName("test");
        table.addColumn(idColumn);
        table.addColumn(valueColumn);
        model.addTable(table);

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));
        String testedValue = "Some Text";

        bean.set("id", (1));
        bean.set(columnName, testedValue);

        roundtripTest(model, bean, "UTF-8", """
            <?xml version='1.0' encoding='UTF-8'?>
            <data>
              <test id="1">
                <column column-name="foo>bar">%s</column>
              </test>
            </data>
            """.formatted(testedValue));
    }

    /**
     * Tests the reader & writer behavior when a column name is 'column'.
     */
    @Test
    public void testColumnNameIsColumn() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='UTF-8'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='column' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testedValue = "Some Text";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("column", testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <test id=\"1\" column=\"" + testedValue + "\" />\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column name is 'column-name'.
     */
    @Test
    public void testColumnNameIsColumnName() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='UTF-8'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='column-name' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testedValue = "Some Text";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("column-name", testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <test id=\"1\" column-name=\"" + testedValue + "\" />\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column name is 'table-name'.
     */
    @Test
    public void testColumnNameIsTableName() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='UTF-8'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='table-name' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""");
        String testedValue = "Some Text";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set("table-name", testedValue);

        roundtripTest(model, bean, "UTF-8", "<?xml version='1.0' encoding='UTF-8'?>\n" + "<data>\n" + "  <test id=\"1\">\n" + "    <column column-name=\"table-name\">" + testedValue + "</column>\n" + "  </test>\n" + "</data>\n");
    }

    /**
     * Tests the reader & writer behavior when a column name is 'base64'.
     */
    @Test
    public void testColumnNameIsBase64() throws Exception {
        Database model = readModel("""
            <?xml version='1.0' encoding='UTF-8'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
              <table name='test'>
                <column name='id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='%s' type='VARCHAR' size='50' required='true'/>
              </table>
            </database>""".formatted(DatabaseIO.BASE64_ATTR_NAME));
        String testedValue = "Some Text";

        SqlDynaBean bean = model.createDynaBeanFor(model.getTable(0));

        bean.set("id", (1));
        bean.set(DatabaseIO.BASE64_ATTR_NAME, testedValue);

        roundtripTest(model, bean, "UTF-8", """
            <?xml version='1.0' encoding='UTF-8'?>
            <data>
              <test id="1">
                <column column-name="%s">%s</column>
              </test>
            </data>
            """.formatted(DatabaseIO.BASE64_ATTR_NAME, testedValue));
    }

    /**
     * A test data sink. There is no need to call start/end as they don't do anything anyway in this class.
     */
    private static class TestDataSink implements DataSink {
        /**
         * Stores the read objects.
         */
        private final List<SqlDynaBean> readObjects;

        /**
         * Creates a new test data sink using the given list as the backing store.
         *
         * @param readObjects The list to store the read object
         */
        private TestDataSink(List<SqlDynaBean> readObjects) {
            this.readObjects = readObjects;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void start() throws DataSinkException {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void addBean(SqlDynaBean bean) throws DataSinkException {
            readObjects.add(bean);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void end() throws DataSinkException {
        }
    }
}
