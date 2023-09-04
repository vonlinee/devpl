package org.apache.ddlutils.dynabean;

import junit.framework.Test;
import org.apache.ddlutils.TestAgainstLiveDatabaseBase;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.ModelBasedResultSetIterator;
import org.apache.ddlutils.platform.sybase.SybasePlatform;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Tests the sql querying. get DynaBean result
 * driver jar
 * @version $Revision: 289996 $
 */
public class TestDynaSqlQueries extends TestAgainstLiveDatabaseBase {

    /**
     * Parameterized test case pattern.
     * load properties file
     * @return The tests
     */
    public static Test suite() throws Exception {
        // Note: swhen run test, modify the param as current class ( current file)
        return getTests(TestDynaSqlQueries.class);
    }

    /**
     * Helper method to wrap the given identifier in delimiters if delimited identifier mode is turned on for the test.
     * @param name The identifier
     * @return The identifier, wrapped if delimited identifier mode is turned on, or as-is if not
     */
    private String asIdentifier(String name) {
        if (getPlatform().isDelimitedIdentifierModeOn()) {
            return getPlatformInfo().getDelimiterToken() + name + getPlatformInfo().getDelimiterToken();
        }
        return name;
    }

    /**
     * Tests a simple SELECT query.
     */
    public void testSimpleQuery() throws Exception {
        System.setProperty("jdbc.properties.file", "jdbc.properties.mysql57");
        Properties props = readTestProperties();
        DataSource dataSource = initDataSourceFromProperties(props);
        assert props != null;
        String databaseName = determineDatabaseName(props, dataSource);

        setTestProperties(props);
        setDataSource(dataSource);
        setDatabaseName(databaseName);

        getPlatform().setDataSource(dataSource);

        createDatabase(
                "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                        "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='ddlutils'>\n" +
                        "  <table name='TestTable'>\n" +
                        "    <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>\n" +
                        "    <column name='TheText' type='VARCHAR' size='15'/>\n" +
                        "  </table>\n" +
                        "</database>");

        insertData(
                """
                        <?xml version='1.0' encoding='ISO-8859-1'?>
                        <data>
                          <TestTable TheId='1' TheText='Text 1'/>
                          <TestTable TheId='2' TheText='Text 2'/>
                          <TestTable TheId='3' TheText='Text 3'/></data>""");

        ModelBasedResultSetIterator it = (ModelBasedResultSetIterator) getPlatform().query(getModel(),
                "SELECT * FROM " + asIdentifier("TestTable"),
                new Table[]{getModel().getTable(0)});

        assertTrue(it.hasNext());
        // we call the method a second time to assert that the result set does not get advanced twice

        DynaBean bean = it.next();

        assertEquals(1,
                getPropertyValue(bean, "TheId"));
        assertEquals("Text 1",
                getPropertyValue(bean, "TheText"));

        assertTrue(it.hasNext());

        bean = it.next();

        assertEquals(2,
                getPropertyValue(bean, "TheId"));
        assertEquals("Text 2",
                getPropertyValue(bean, "TheText"));

        assertTrue(it.hasNext());

        bean = it.next();

        assertEquals(3,
                getPropertyValue(bean, "TheId"));
        assertEquals("Text 3",
                getPropertyValue(bean, "TheText"));

        assertFalse(it.hasNext());
        assertFalse(it.isConnectionOpen());
    }

    /**
     * Tests a simple SELECT fetch.
     */
    public void testSimpleFetch() throws Exception {
        createDatabase(
                "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                        "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='ddlutils'>\n" +
                        "  <table name='TestTable'>\n" +
                        "    <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>\n" +
                        "    <column name='TheText' type='VARCHAR' size='15'/>\n" +
                        "  </table>\n" +
                        "</database>");

        insertData(
                """
                        <?xml version='1.0' encoding='ISO-8859-1'?>
                        <data>
                          <TestTable TheId='1' TheText='Text 1'/>
                          <TestTable TheId='2' TheText='Text 2'/>
                          <TestTable TheId='3' TheText='Text 3'/></data>""");

        List<DynaBean> beans = getPlatform().fetch(getModel(),
                "SELECT * FROM " + asIdentifier("TestTable"),
                new Table[]{getModel().getTable(0)});

        assertEquals(3,
                beans.size());

        DynaBean bean = beans.get(0);

        assertEquals(1,
                getPropertyValue(bean, "TheId"));
        assertEquals("Text 1",
                getPropertyValue(bean, "TheText"));

        bean = beans.get(1);

        assertEquals(2,
                getPropertyValue(bean, "TheId"));
        assertEquals("Text 2",
                getPropertyValue(bean, "TheText"));

        bean = beans.get(2);

        assertEquals(3,
                getPropertyValue(bean, "TheId"));
        assertEquals("Text 3",
                getPropertyValue(bean, "TheText"));
    }

    /**
     * Tests insertion & reading of auto-increment columns.
     */
    public void testAutoIncrement() throws Exception {
        // we need special catering for Sybase which does not support identity for INTEGER columns
        final String modelXml;

        if (SybasePlatform.DATABASENAME.equals(getPlatform().getName())) {
            modelXml = "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                    "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='ddlutils'>\n" +
                    "  <table name='TestTable'>\n" +
                    "    <column name='TheId' type='NUMERIC' size='12,0' primaryKey='true' required='true' autoIncrement='true'/>\n" +
                    "    <column name='TheText' type='VARCHAR' size='15'/>\n" +
                    "  </table>\n" +
                    "</database>";
        } else {
            modelXml = "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                    "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='ddlutils'>\n" +
                    "  <table name='TestTable'>\n" +
                    "    <column name='TheId' type='INTEGER' primaryKey='true' required='true' autoIncrement='true'/>\n" +
                    "    <column name='TheText' type='VARCHAR' size='15'/>\n" +
                    "  </table>\n" +
                    "</database>";
        }

        createDatabase(modelXml);

        // we're inserting the rows manually via beans since we do want to
        // check the back-reading of the auto-increment columns
        SqlDynaClass dynaClass = getModel().getDynaClassFor("TestTable");
        DynaBean bean;
        Object id1 = null;
        Object id2 = null;
        Object id3 = null;

        bean = dynaClass.newInstance();
        bean.set("TheText", "Text 1");
        getPlatform().insert(getModel(), bean);
        if (getPlatformInfo().isLastIdentityValueReadable()) {
            // we cannot know the value for sure (though it usually will be 1)
            id1 = getPropertyValue(bean, "TheId");
            assertNotNull(id1);
        }
        bean = dynaClass.newInstance();
        bean.set("TheText", "Text 2");
        getPlatform().insert(getModel(), bean);
        if (getPlatformInfo().isLastIdentityValueReadable()) {
            // we cannot know the value for sure (though it usually will be 2)
            id2 = getPropertyValue(bean, "TheId");
            assertNotNull(id2);
        }
        bean = dynaClass.newInstance();
        bean.set("TheText", "Text 3");
        getPlatform().insert(getModel(), bean);
        if (getPlatformInfo().isLastIdentityValueReadable()) {
            // we cannot know the value for sure (though it usually will be 3)
            id3 = getPropertyValue(bean, "TheId");
            assertNotNull(id3);
        }

        List<DynaBean> beans = getPlatform().fetch(getModel(),
                "SELECT * FROM " + asIdentifier("TestTable"),
                new Table[]{getModel().getTable(0)});

        assertEquals(3,
                beans.size());

        bean = beans.get(0);
        if (getPlatformInfo().isLastIdentityValueReadable()) {
            assertEquals(id1,
                    getPropertyValue(bean, "TheId"));
        } else {
            assertNotNull(getPropertyValue(bean, "TheId"));
        }
        assertEquals("Text 1",
                getPropertyValue(bean, "TheText"));

        bean = (DynaBean) beans.get(1);
        if (getPlatformInfo().isLastIdentityValueReadable()) {
            assertEquals(id2,
                    getPropertyValue(bean, "TheId"));
        } else {
            assertNotNull(getPropertyValue(bean, "TheId"));
        }
        assertEquals("Text 2",
                getPropertyValue(bean, "TheText"));

        bean = beans.get(2);
        if (getPlatformInfo().isLastIdentityValueReadable()) {
            assertEquals(id3,
                    getPropertyValue(bean, "TheId"));
        } else {
            assertNotNull(getPropertyValue(bean, "TheId"));
        }
        assertEquals("Text 3",
                getPropertyValue(bean, "TheText"));
    }

    /**
     * Tests a more complicated SELECT query that leads to a JOIN in the database.
     */
    public void testJoinQuery() {
        createDatabase(
                "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                        "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='ddlutils'>\n" +
                        "  <table name='TestTable1'>\n" +
                        "    <column name='Id1' type='INTEGER' primaryKey='true' required='true'/>\n" +
                        "    <column name='Id2' type='INTEGER'/>\n" +
                        "  </table>\n" +
                        "  <table name='TestTable2'>\n" +
                        "    <column name='Id' type='INTEGER' primaryKey='true' required='true'/>\n" +
                        "    <column name='Avalue' type='VARCHAR' size='15'/>\n" +
                        "  </table>\n" +
                        "</database>");

        insertData(
                """
                        <?xml version='1.0' encoding='ISO-8859-1'?>
                        <data>
                          <TestTable1 Id1='1'/>
                          <TestTable1 Id1='2' Id2='3'/>
                          <TestTable2 Id='1' Avalue='Text 1'/>
                          <TestTable2 Id='2' Avalue='Text 2'/>
                          <TestTable2 Id='3' Avalue='Text 3'/></data>""");

        String sql = "SELECT " +
                asIdentifier("Id1") +
                "," +
                asIdentifier("Avalue") +
                " FROM " + asIdentifier("TestTable1") +
                "," +
                asIdentifier("TestTable2") +
                " WHERE " +
                asIdentifier("Id2") +
                "=" +
                asIdentifier("Id");

        ModelBasedResultSetIterator it = (ModelBasedResultSetIterator) getPlatform().query(getModel(),
                sql,
                new Table[]{getModel().getTable(0), getModel().getTable(1)});

        assertTrue(it.hasNext());

        DynaBean bean = it.next();

        assertEquals(2, getPropertyValue(bean, "Id1"));
        assertEquals("Text 3", getPropertyValue(bean, "Avalue"));

        assertFalse(it.hasNext());
        assertFalse(it.isConnectionOpen());
    }

    /**
     * Tests the insert method.
     */
    public void testInsertSingle() throws Exception {
        createDatabase(
                "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                        "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='ddlutils'>\n" +
                        "  <table name='TestTable'>\n" +
                        "    <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>\n" +
                        "    <column name='TheText' type='VARCHAR' size='15'/>\n" +
                        "  </table>\n" +
                        "</database>");

        SqlDynaClass dynaClass = SqlDynaClass.newInstance(getModel().getTable(0));
        DynaBean dynaBean = new SqlDynaBean(dynaClass);

        dynaBean.set("TheId", 1);
        dynaBean.set("TheText", "Text 1");

        getPlatform().insert(getModel(), dynaBean);

        List<DynaBean> beans = getPlatform().fetch(getModel(),
                "SELECT * FROM " + asIdentifier("TestTable"),
                new Table[]{getModel().getTable(0)});

        assertEquals(1,
                beans.size());

        DynaBean bean = beans.get(0);

        assertEquals(1, getPropertyValue(bean, "TheId"));
        assertEquals("Text 1", getPropertyValue(bean, "TheText"));
    }

    /**
     * Tests the insert method.
     */
    public void testInsertMultiple() throws Exception {
        createDatabase(
                "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                        "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='ddlutils'>\n" +
                        "  <table name='TestTable'>\n" +
                        "    <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>\n" +
                        "    <column name='TheText' type='VARCHAR' size='15'/>\n" +
                        "  </table>\n" +
                        "</database>");

        SqlDynaClass dynaClass = SqlDynaClass.newInstance(getModel().getTable(0));
        DynaBean dynaBean1 = new SqlDynaBean(dynaClass);
        DynaBean dynaBean2 = new SqlDynaBean(dynaClass);
        DynaBean dynaBean3 = new SqlDynaBean(dynaClass);

        dynaBean1.set("TheId", 1);
        dynaBean1.set("TheText", "Text 1");
        dynaBean2.set("TheId", 2);
        dynaBean2.set("TheText", "Text 2");
        dynaBean3.set("TheId", 3);
        dynaBean3.set("TheText", "Text 3");

        List<DynaBean> dynaBeans = new ArrayList<>();

        dynaBeans.add(dynaBean1);
        dynaBeans.add(dynaBean2);
        dynaBeans.add(dynaBean3);

        getPlatform().insert(getModel(), dynaBeans);

        List<DynaBean> beans = getPlatform().fetch(getModel(),
                "SELECT * FROM " + asIdentifier("TestTable"),
                new Table[]{getModel().getTable(0)});

        assertEquals(3,
                beans.size());

        DynaBean bean = (DynaBean) beans.get(0);

        assertEquals(1,
                getPropertyValue(bean, "TheId"));
        assertEquals("Text 1",
                getPropertyValue(bean, "TheText"));

        bean = beans.get(1);

        assertEquals(2,
                getPropertyValue(bean, "TheId"));
        assertEquals("Text 2",
                getPropertyValue(bean, "TheText"));

        bean = beans.get(2);

        assertEquals(3, getPropertyValue(bean, "TheId"));
        assertEquals("Text 3", getPropertyValue(bean, "TheText"));
    }

    /**
     * Tests the update method.
     */
    public void testUpdate() throws Exception {
        createDatabase(
                "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                        "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='ddlutils'>\n" +
                        "  <table name='TestTable'>\n" +
                        "    <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>\n" +
                        "    <column name='TheText' type='VARCHAR' size='15'/>\n" +
                        "  </table>\n" +
                        "</database>");

        insertData(
                """
                        <?xml version='1.0' encoding='ISO-8859-1'?>
                        <data>
                          <TestTable TheId='1' TheText='Text 1'/>
                        </data>""");

        SqlDynaClass dynaClass = SqlDynaClass.newInstance(getModel().getTable(0));
        DynaBean dynaBean = new SqlDynaBean(dynaClass);

        dynaBean.set("TheId", 1);
        dynaBean.set("TheText", "Text 10");

        getPlatform().update(getModel(), dynaBean);

        List<DynaBean> beans = getPlatform().fetch(getModel(),
                "SELECT * FROM " + asIdentifier("TestTable"),
                new Table[]{getModel().getTable(0)});

        assertEquals(1,
                beans.size());

        DynaBean bean = (DynaBean) beans.get(0);

        assertEquals(1, getPropertyValue(bean, "TheId"));
        assertEquals("Text 10", getPropertyValue(bean, "TheText"));
    }

    /**
     * Tests the exists method.
     */
    public void testExists() throws Exception {
        createDatabase(
                "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                        "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='ddlutils'>\n" +
                        "  <table name='TestTable'>\n" +
                        "    <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>\n" +
                        "    <column name='TheText' type='VARCHAR' size='15'/>\n" +
                        "  </table>\n" +
                        "</database>");

        insertData(
                """
                        <?xml version='1.0' encoding='ISO-8859-1'?>
                        <data>
                          <TestTable TheId='1' TheText='Text 1'/>
                          <TestTable TheId='3' TheText='Text 3'/>
                        </data>""");

        SqlDynaClass dynaClass = SqlDynaClass.newInstance(getModel().getTable(0));
        DynaBean dynaBean1 = new SqlDynaBean(dynaClass);
        DynaBean dynaBean2 = new SqlDynaBean(dynaClass);
        DynaBean dynaBean3 = new SqlDynaBean(dynaClass);

        dynaBean1.set("TheId", 1);
        dynaBean1.set("TheText", "Text 1");
        dynaBean2.set("TheId", 2);
        dynaBean2.set("TheText", "Text 2");
        dynaBean3.set("TheId", 3);
        dynaBean3.set("TheText", "Text 30");

        assertTrue(getPlatform().exists(getModel(), dynaBean1));
        assertFalse(getPlatform().exists(getModel(), dynaBean2));
        assertTrue(getPlatform().exists(getModel(), dynaBean3));
    }


    /**
     * Tests the store method.
     */
    public void testStoreNew() throws Exception {
        createDatabase(
                "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                        "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='ddlutils'>\n" +
                        "  <table name='TestTable'>\n" +
                        "    <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>\n" +
                        "    <column name='TheText' type='VARCHAR' size='15'/>\n" +
                        "  </table>\n" +
                        "</database>");

        SqlDynaClass dynaClass = SqlDynaClass.newInstance(getModel().getTable(0));
        DynaBean dynaBean = new SqlDynaBean(dynaClass);

        dynaBean.set("TheId", 1);
        dynaBean.set("TheText", "Text 1");

        getPlatform().store(getModel(), dynaBean);

        List<DynaBean> beans = getPlatform().fetch(getModel(),
                "SELECT * FROM " + asIdentifier("TestTable"),
                new Table[]{getModel().getTable(0)});

        assertEquals(1, beans.size());

        DynaBean bean = beans.get(0);

        assertEquals(1,
                getPropertyValue(bean, "TheId"));
        assertEquals("Text 1",
                getPropertyValue(bean, "TheText"));
    }

    /**
     * Tests the store method.
     */
    public void testStoreExisting() throws Exception {
        createDatabase(
                "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                        "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='ddlutils'>\n" +
                        "  <table name='TestTable'>\n" +
                        "    <column name='TheId' type='INTEGER' primaryKey='true' required='true'/>\n" +
                        "    <column name='TheText' type='VARCHAR' size='15'/>\n" +
                        "  </table>\n" +
                        "</database>");

        insertData(
                """
                        <?xml version='1.0' encoding='ISO-8859-1'?>
                        <data>
                          <TestTable TheId='1' TheText='Text 1'/>
                        </data>""");

        SqlDynaClass dynaClass = SqlDynaClass.newInstance(getModel().getTable(0));
        DynaBean dynaBean = new SqlDynaBean(dynaClass);

        dynaBean.set("TheId", 1);
        dynaBean.set("TheText", "Text 10");

        getPlatform().store(getModel(), dynaBean);

        List<DynaBean> beans = getPlatform().fetch(getModel(),
                "SELECT * FROM " + asIdentifier("TestTable"),
                new Table[]{getModel().getTable(0)});

        assertEquals(1, beans.size());

        DynaBean bean = beans.get(0);

        assertEquals(1, getPropertyValue(bean, "TheId"));
        assertEquals("Text 10", getPropertyValue(bean, "TheText"));
    }
}
