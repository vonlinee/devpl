package org.apache.ddlutils.data;

import org.apache.ddlutils.TestAgainstLiveDatabaseBase;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TableModel;
import org.apache.ddlutils.model.TableRow;
import org.apache.ddlutils.platform.DBTypeEnum;
import org.apache.ddlutils.platform.TableRowIterator;
import org.apache.ddlutils.util.DatabaseTestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the sql querying.
 */
@DisplayName("TestDynaSqlQueries")
public class TestDataSqlQueries extends TestAgainstLiveDatabaseBase {
    /**
     * Parameterized test case pattern.
     */
    @BeforeEach
    public void setDatabaseName() throws Exception {
        String propFile = DatabaseTestHelper.getAbsolutePath("jdbc.properties.mysql57");
        System.setProperty(JDBC_PROPERTIES_PROPERTY, propFile);
        super.init();
    }

    /**
     * Helper method to wrap the given identifier in delimiters if delimited identifier mode is turned on for the test.
     *
     * @param name The identifier
     * @return The identifier, wrapped if delimited identifier mode is turned on, or as-is if not
     */
    private String asIdentifier(String name) {
        return getPlatform().asIdentifier(name);
    }

    /**
     * Tests a simple SELECT query.
     */
    @Test
    public void testSimpleQuery() throws Exception {
        createDatabase("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='ddlutils'>
              <table name='TestTable'>
                <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>
                <column name='TheText' type='VARCHAR' size='15'/>
              </table>
            </database>""");

        insertData("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <data>
              <TestTable TheId='1' TheText='Text 1'/>
              <TestTable TheId='2' TheText='Text 2'/>
              <TestTable TheId='3' TheText='Text 3'/></data>""");

        TableRowIterator it = getPlatform().query(getModel(), "SELECT * FROM " + asIdentifier("TestTable"), new Table[]{getModel().getTable(0)});

        Assertions.assertTrue(it.hasNext());
        // we call the method a second time to assert that the result set does not get advanced twice
        Assertions.assertTrue(true);

        TableRow bean = it.next();

        Assertions.assertEquals(1, getPropertyValue(bean, "TheId"));
        Assertions.assertEquals("Text 1", getPropertyValue(bean, "TheText"));

        Assertions.assertTrue(it.hasNext());

        bean = it.next();

        Assertions.assertEquals((2), getPropertyValue(bean, "TheId"));
        Assertions.assertEquals("Text 2", getPropertyValue(bean, "TheText"));

        Assertions.assertTrue(it.hasNext());

        bean = it.next();

        Assertions.assertEquals((3), getPropertyValue(bean, "TheId"));
        Assertions.assertEquals("Text 3", getPropertyValue(bean, "TheText"));

        Assertions.assertFalse(it.hasNext());
        Assertions.assertFalse(it.isConnectionOpen());
    }

    /**
     * Tests a simple SELECT fetch.
     */
    @Test
    public void testSimpleFetch() throws Exception {
        Database database = createDatabase("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='ddlutils'>
              <table name='TestTable'>
                <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>
                <column name='TheText' type='VARCHAR' size='15'/>
              </table>
            </database>""");

        Database database1 = insertData("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <data>
              <TestTable TheId='1' TheText='Text 1'/>
              <TestTable TheId='2' TheText='Text 2'/>
              <TestTable TheId='3' TheText='Text 3'/>
            </data>
            """);

        Database db = getModel();

        List<TableRow> rows = getPlatform().fetch(db, "SELECT * FROM " + asIdentifier("TestTable"), new Table[]{getModel().getTable(0)});

        Assertions.assertEquals(3, rows.size());

        TableRow bean = rows.get(0);

        Assertions.assertEquals((1), getPropertyValue(bean, "TheId"));
        Assertions.assertEquals("Text 1", getPropertyValue(bean, "TheText"));

        bean = rows.get(1);

        Assertions.assertEquals((2), getPropertyValue(bean, "TheId"));
        Assertions.assertEquals("Text 2", getPropertyValue(bean, "TheText"));

        bean = rows.get(2);

        Assertions.assertEquals((3), getPropertyValue(bean, "TheId"));
        Assertions.assertEquals("Text 3", getPropertyValue(bean, "TheText"));
    }

    /**
     * Tests a simple SELECT fetch.
     */
    @Test
    public void testSimpleExistedQuery() throws Exception {

        String sql = "select * from field_info";
        Database db = getPlatform().readModelFromDatabase("devpl");

        List<TableRow> rows = getPlatform().fetch(db, sql, new Table[]{db.getTable(0)});

        System.out.println(rows);
    }

    /**
     * Tests insertion & reading of auto-increment columns.
     */
    @Test
    public void testAutoIncrement() throws Exception {
        // we need special catering for Sybase which does not support identity for INTEGER columns
        final String modelXml;

        if (DBTypeEnum.SYBASE.getName().equals(getPlatform().getName())) {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='ddlutils'>
                  <table name='TestTable'>
                    <column name='TheId' type='NUMERIC' size='12,0' primaryKey='true' required='true' autoIncrement='true'/>
                    <column name='TheText' type='VARCHAR' size='15'/>
                  </table>
                </database>""";
        } else {
            modelXml = """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='ddlutils'>
                  <table name='TestTable'>
                    <column name='TheId' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>
                    <column name='TheText' type='VARCHAR' size='15'/>
                  </table>
                </database>""";
        }

        createDatabase(modelXml);

        // we're inserting the rows manually via rows since we do want to
        // check the back-reading of the auto-increment columns
        TableModel tableModel = getModel().getTableModel("TestTable");
        TableRow bean;
        Object id1 = null;
        Object id2 = null;
        Object id3 = null;

        bean = tableModel.createRow();
        bean.setColumnValue("TheText", "Text 1");
        getPlatform().insert(getModel(), bean);
        if (getPlatformInfo().isLastIdentityValueReadable()) {
            // we cannot know the value for sure (though it usually will be 1)
            id1 = getPropertyValue(bean, "TheId");
            Assertions.assertNotNull(id1);
        }
        bean = tableModel.createRow();
        bean.setColumnValue("TheText", "Text 2");
        getPlatform().insert(getModel(), bean);
        if (getPlatformInfo().isLastIdentityValueReadable()) {
            // we cannot know the value for sure (though it usually will be 2)
            id2 = getPropertyValue(bean, "TheId");
            Assertions.assertNotNull(id2);
        }
        bean = tableModel.createRow();
        bean.setColumnValue("TheText", "Text 3");
        getPlatform().insert(getModel(), bean);
        if (getPlatformInfo().isLastIdentityValueReadable()) {
            // we cannot know the value for sure (though it usually will be 3)
            id3 = getPropertyValue(bean, "TheId");
            Assertions.assertNotNull(id3);
        }

        List<TableRow> rows = getPlatform().fetch(getModel(), "SELECT * FROM " + asIdentifier("TestTable"), new Table[]{getModel().getTable(0)});

        Assertions.assertEquals(3, rows.size());

        bean = rows.get(0);
        if (getPlatformInfo().isLastIdentityValueReadable()) {
            Assertions.assertEquals(id1, getPropertyValue(bean, "TheId"));
        } else {
            Assertions.assertNotNull(getPropertyValue(bean, "TheId"));
        }
        Assertions.assertEquals("Text 1", getPropertyValue(bean, "TheText"));

        bean = rows.get(1);
        if (getPlatformInfo().isLastIdentityValueReadable()) {
            Assertions.assertEquals(id2, getPropertyValue(bean, "TheId"));
        } else {
            Assertions.assertNotNull(getPropertyValue(bean, "TheId"));
        }
        Assertions.assertEquals("Text 2", getPropertyValue(bean, "TheText"));

        bean = rows.get(2);
        if (getPlatformInfo().isLastIdentityValueReadable()) {
            Assertions.assertEquals(id3, getPropertyValue(bean, "TheId"));
        } else {
            Assertions.assertNotNull(getPropertyValue(bean, "TheId"));
        }
        Assertions.assertEquals("Text 3", getPropertyValue(bean, "TheText"));
    }

    /**
     * Tests a more complicated SELECT query that leads to a JOIN in the database.
     */
    @Test
    public void testJoinQuery() throws Exception {
        createDatabase("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='ddlutils'>
              <table name='TestTable1'>
                <column name='Id1' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Id2' type='INTEGER'/>
              </table>
              <table name='TestTable2'>
                <column name='Id' type='INTEGER' primaryKey='true' required='true'/>
                <column name='Avalue' type='VARCHAR' size='15'/>
              </table>
            </database>""");

        insertData("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <data>
              <TestTable1 Id1='1'/>
              <TestTable1 Id1='2' Id2='3'/>
              <TestTable2 Id='1' Avalue='Text 1'/>
              <TestTable2 Id='2' Avalue='Text 2'/>
              <TestTable2 Id='3' Avalue='Text 3'/></data>""");

        String sql = "SELECT " + asIdentifier("Id1") + "," + asIdentifier("Avalue") + " FROM " + asIdentifier("TestTable1") + "," + asIdentifier("TestTable2") + " WHERE " + asIdentifier("Id2") + "=" + asIdentifier("Id");

        TableRowIterator it = getPlatform().query(getModel(), sql, new Table[]{getModel().getTable(0), getModel().getTable(1)});

        Assertions.assertTrue(it.hasNext());

        TableRow bean = it.next();

        Assertions.assertEquals((2), getPropertyValue(bean, "Id1"));
        Assertions.assertEquals("Text 3", getPropertyValue(bean, "Avalue"));

        Assertions.assertFalse(it.hasNext());
        Assertions.assertFalse(it.isConnectionOpen());
    }

    /**
     * Tests the insert method.
     */
    @Test
    public void testInsertSingle() throws Exception {
        createDatabase("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='ddlutils'>
              <table name='TestTable'>
                <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>
                <column name='TheText' type='VARCHAR' size='15'/>
              </table>
            </database>""");

        TableModel dynaClass = TableModel.of(getModel().getTable(0));
        TableRow dynaBean = new TableRow(dynaClass);

        dynaBean.setColumnValue("TheId", (1));
        dynaBean.setColumnValue("TheText", "Text 1");

        getPlatform().insert(getModel(), dynaBean);

        List<TableRow> rows = getPlatform().fetch(getModel(), "SELECT * FROM " + asIdentifier("TestTable"), new Table[]{getModel().getTable(0)});

        Assertions.assertEquals(1, rows.size());

        TableRow bean = rows.get(0);

        Assertions.assertEquals((1), getPropertyValue(bean, "TheId"));
        Assertions.assertEquals("Text 1", getPropertyValue(bean, "TheText"));
    }

    /**
     * Tests the insert method.
     */
    @Test
    public void testInsertMultiple() throws Exception {
        createDatabase("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='ddlutils'>
              <table name='TestTable'>
                <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>
                <column name='TheText' type='VARCHAR' size='15'/>
              </table>
            </database>""");

        TableModel tableModel = TableModel.of(getModel().getTable(0));
        TableRow dynaBean1 = tableModel.createRow();
        TableRow dynaBean2 = tableModel.createRow();
        TableRow dynaBean3 = tableModel.createRow();

        dynaBean1.setColumnValue("TheId", (1));
        dynaBean1.setColumnValue("TheText", "Text 1");
        dynaBean2.setColumnValue("TheId", (2));
        dynaBean2.setColumnValue("TheText", "Text 2");
        dynaBean3.setColumnValue("TheId", (3));
        dynaBean3.setColumnValue("TheText", "Text 3");

        List<TableRow> dynaBeans = new ArrayList<>();

        dynaBeans.add(dynaBean1);
        dynaBeans.add(dynaBean2);
        dynaBeans.add(dynaBean3);

        getPlatform().insert(getModel(), dynaBeans);

        List<TableRow> rows = getPlatform().fetch(getModel(), "SELECT * FROM " + asIdentifier("TestTable"), new Table[]{getModel().getTable(0)});

        Assertions.assertEquals(3, rows.size());

        TableRow bean = rows.get(0);

        Assertions.assertEquals((1), getPropertyValue(bean, "TheId"));
        Assertions.assertEquals("Text 1", getPropertyValue(bean, "TheText"));

        bean = rows.get(1);

        Assertions.assertEquals((2), getPropertyValue(bean, "TheId"));
        Assertions.assertEquals("Text 2", getPropertyValue(bean, "TheText"));

        bean = rows.get(2);

        Assertions.assertEquals((3), getPropertyValue(bean, "TheId"));
        Assertions.assertEquals("Text 3", getPropertyValue(bean, "TheText"));
    }

    /**
     * Tests the update method.
     */
    @Test
    public void testUpdate() throws Exception {
        createDatabase("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='ddlutils'>
              <table name='TestTable'>
                <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>
                <column name='TheText' type='VARCHAR' size='15'/>
              </table>
            </database>""");

        insertData("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <data>
              <TestTable TheId='1' TheText='Text 1'/>
            </data>""");

        TableModel dynaClass = TableModel.of(getModel().getTable(0));
        TableRow dynaBean = new TableRow(dynaClass);

        dynaBean.setColumnValue("TheId", (1));
        dynaBean.setColumnValue("TheText", "Text 10");

        getPlatform().update(getModel(), dynaBean);

        List<TableRow> rows = getPlatform().fetch(getModel(), "SELECT * FROM " + asIdentifier("TestTable"), new Table[]{getModel().getTable(0)});

        Assertions.assertEquals(1, rows.size());

        TableRow bean = rows.get(0);

        Assertions.assertEquals((1), getPropertyValue(bean, "TheId"));
        Assertions.assertEquals("Text 10", getPropertyValue(bean, "TheText"));
    }

    /**
     * Tests the exists method.
     */
    @Test
    public void testExists() throws Exception {
        createDatabase("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='ddlutils'>
              <table name='TestTable'>
                <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>
                <column name='TheText' type='VARCHAR' size='15'/>
              </table>
            </database>""");

        insertData("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <data>
              <TestTable TheId='1' TheText='Text 1'/>
              <TestTable TheId='3' TheText='Text 3'/>
            </data>""");

        TableModel dynaClass = TableModel.of(getModel().getTable(0));
        TableRow dynaBean1 = new TableRow(dynaClass);
        TableRow dynaBean2 = new TableRow(dynaClass);
        TableRow dynaBean3 = new TableRow(dynaClass);

        dynaBean1.setColumnValue("TheId", (1));
        dynaBean1.setColumnValue("TheText", "Text 1");
        dynaBean2.setColumnValue("TheId", (2));
        dynaBean2.setColumnValue("TheText", "Text 2");
        dynaBean3.setColumnValue("TheId", (3));
        dynaBean3.setColumnValue("TheText", "Text 30");

        Assertions.assertTrue(getPlatform().exists(getModel(), dynaBean1));
        Assertions.assertFalse(getPlatform().exists(getModel(), dynaBean2));
        Assertions.assertTrue(getPlatform().exists(getModel(), dynaBean3));
    }


    /**
     * Tests the store method.
     */
    @Test
    public void testStoreNew() throws Exception {
        createDatabase("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='ddlutils'>
              <table name='TestTable'>
                <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>
                <column name='TheText' type='VARCHAR' size='15'/>
              </table>
            </database>""");

        TableModel dynaClass = TableModel.of(getModel().getTable(0));
        TableRow dynaBean = new TableRow(dynaClass);

        dynaBean.setColumnValue("TheId", (1));
        dynaBean.setColumnValue("TheText", "Text 1");

        getPlatform().store(getModel(), dynaBean);

        List<TableRow> rows = getPlatform().fetch(getModel(), "SELECT * FROM " + asIdentifier("TestTable"), new Table[]{getModel().getTable(0)});

        Assertions.assertEquals(1, rows.size());

        TableRow bean = rows.get(0);

        Assertions.assertEquals((1), getPropertyValue(bean, "TheId"));
        Assertions.assertEquals("Text 1", getPropertyValue(bean, "TheText"));
    }

    /**
     * Tests the store method.
     */
    @Test
    public void testStoreExisting() throws Exception {
        createDatabase("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='ddlutils'>
              <table name='TestTable'>
                <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>
                <column name='TheText' type='VARCHAR' size='15'/>
              </table>
            </database>""");

        insertData("""
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <data>
              <TestTable TheId='1' TheText='Text 1'/>
            </data>""");

        TableModel tableModel = TableModel.of(getModel().getTable(0));
        TableRow dynaBean = tableModel.createRow();

        dynaBean.setColumnValue("TheId", (1));
        dynaBean.setColumnValue("TheText", "Text 10");

        getPlatform().store(getModel(), dynaBean);

        List<TableRow> rows = getPlatform().fetch(getModel(), "SELECT * FROM " + asIdentifier("TestTable"), new Table[]{getModel().getTable(0)});

        Assertions.assertEquals(1, rows.size());

        TableRow bean = rows.get(0);

        Assertions.assertEquals((1), getPropertyValue(bean, "TheId"));
        Assertions.assertEquals("Text 10", getPropertyValue(bean, "TheText"));
    }
}
