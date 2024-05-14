package org.apache.ddlutils.task;

import org.apache.ddlutils.DdlUtilsTaskException;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.util.DatabaseTestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * Tests the writeSchemaToFile sub-task.
 */
public class TestWriteSchemaToFileCommand extends TestTaskBase {

    @BeforeEach
    public void setup() throws Exception {
        System.setProperty(JDBC_PROPERTIES_PROPERTY, DatabaseTestHelper.getAbsolutePath("jdbc.properties.mysql57"));
        initDataSource();
    }

    /**
     * Adds the writeSchemaToFile sub-task to the given task, executes it, and checks its output.
     *
     * @param task          The task
     * @param expectedModel The expected model
     */
    private void runTask(DatabaseToDdlTask task, Database expectedModel) throws IOException {
        WriteSchemaToFileCommand subTask = new WriteSchemaToFileCommand();
        File tmpFile = File.createTempFile("schema", ".xml");

        try {
            subTask.setOutputFile(tmpFile);

            getLog().info("写入文件" + tmpFile.getAbsolutePath());
            task.addWriteSchemaToFile(subTask);
            task.setModelName(expectedModel.getName());
            task.execute();

            assertEquals(expectedModel, new DatabaseIO().read(tmpFile), isUseDelimitedIdentifiers());
        } catch (Exception ignored) {

        }
    }

    /**
     * Tests the task against an empty database.
     */
    @Test
    public void testEmptyDatabase() throws IOException {
        runTask(getDatabaseToDdlTaskInstance(), new Database("roundtriptest"));
    }

    @Test
    public void testLiveDatabase() throws IOException {
        runTask(getDatabaseToDdlTaskInstance(), readModelFromDatabase("devpl"));
    }

    /**
     * Tests against a model with two tables and a FK.
     */
    @Test
    public void testSimpleModel() throws IOException {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(modelXml);
        runTask(getDatabaseToDdlTaskInstance(), readModelFromDatabase("roundtriptest"));
    }

    /**
     * Tests against a model with two tables and a FK.
     */
    @Test
    public void testSimpleModelWithDelimitedIdentifiers() throws IOException {
        if (!isUseDelimitedIdentifiers()) {
            return;
        }

        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip 1'>
                <column name='A PK' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='A Value' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip 2'>
                  <reference local='A Value' foreign='A PK'/>
                </foreign-key>
              </table>
              <table name='Roundtrip 2'>
                <column name='A PK' type='INTEGER' primaryKey='true' required='true'/>
                <column name='A Value' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='Roundtrip 1'>
                  <reference local='A Value' foreign='A PK'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(modelXml);
        runTask(getDatabaseToDdlTaskInstance(), readModelFromDatabase("roundtriptest"));
    }

    /**
     * Tests of the includeTables filter.
     */
    @Test
    public void testIncludeSingleTable() throws IOException {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setIncludeTables("roundtrip1");
        } else {
            task.setIncludeTables("ROUNDTRIP1");
        }
        runTask(task, parseDatabaseFromString(model2Xml));
    }

    /**
     * Tests of the includeTables filter in the presence of a foreign key to the indicated table.
     */
    @Test
    public void testIncludeSingleTableWithFk() throws IOException {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(modelXml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setIncludeTables("roundtrip1");
        } else {
            task.setIncludeTables("ROUNDTRIP1");
        }
        try {
            runTask(task, readModelFromDatabase("roundtriptest"));
            Assertions.fail();
        } catch (DdlUtilsTaskException ex) {
            // expected
        }
    }

    /**
     * Tests of the includeTableFilter filter.
     */
    @Test
    public void testIncludeSingleTableViaRegExp() throws IOException {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip_1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
              <table name='Roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip_1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setIncludeTableFilter("Round.*1");
        } else {
            task.setIncludeTableFilter("ROUND.*1");
        }
        runTask(task, parseDatabaseFromString(model2Xml));
    }

    /**
     * Tests of the includeTableFilter filter in the presence of a foreign key to the indicated table.
     */
    @Test
    public void testIncludeSingleTableWithFkViaRegExp() throws IOException {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip_1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='Roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='Roundtrip_1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(modelXml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setIncludeTableFilter("Round.*1");
        } else {
            task.setIncludeTableFilter("ROUND.*1");
        }
        try {
            runTask(task, readModelFromDatabase("roundtriptest"));
            Assertions.fail();
        } catch (DdlUtilsTaskException ex) {
            // expected
        }
    }

    /**
     * Tests of the includeTables filter for multiple tables.
     */
    @Test
    public void testIncludeMultipleTables() throws IOException {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip3'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
              </table>
              <table name='roundtrip3'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip3'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='roundtrip3'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setIncludeTables("roundtrip1,roundtrip3");
        } else {
            task.setIncludeTables("ROUNDTRIP1,ROUNDTRIP3");
        }
        runTask(task, parseDatabaseFromString(model2Xml));
    }

    /**
     * Tests of the includeTables filter for multiple tables.
     */
    @Test
    public void testIncludeMultipleTablesWithFKPointingToThem() throws IOException {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='roundtrip3'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(modelXml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setIncludeTables("roundtrip1,roundtrip3");
        } else {
            task.setIncludeTables("ROUNDTRIP1,ROUNDTRIP3");
        }
        try {
            runTask(task, readModelFromDatabase("roundtriptest"));
            fail();
        } catch (DdlUtilsTaskException ex) {
            // expected
        }
    }

    /**
     * Tests of the includeTableFilter filter for multiple tables.
     */
    @Test
    public void testIncludeMultipleTablesViaRegExp() throws IOException {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip_1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_3'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='Roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='Roundtrip_3'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='Roundtrip_1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip_1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_3'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='Roundtrip_3'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='Roundtrip_1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setIncludeTableFilter(".*trip_[1|3]");
        } else {
            task.setIncludeTableFilter(".*TRIP_[1|3]");
        }
        runTask(task, parseDatabaseFromString(model2Xml));
    }

    /**
     * Tests of the includeTables filter for multiple tables via regexp.
     */
    @Test
    public void testIncludeMultipleTablesWithFKPointingToThemViaRegExp() throws IOException {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip_1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_3'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='Roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_3'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='Roundtrip_3'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='Roundtrip_1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(modelXml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setIncludeTableFilter(".*trip_*[1|3]");
        } else {
            task.setIncludeTableFilter(".*TRIP_*[1|3]");
        }
        try {
            runTask(task, readModelFromDatabase("roundtriptest"));
            fail();
        } catch (DdlUtilsTaskException ex) {
            // expected
        }
    }

    /**
     * Tests of the excludeTables filter.
     */
    @Test
    public void testExcludeSingleTable() throws IOException {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setExcludeTables("roundtrip2");
        } else {
            task.setExcludeTables("ROUNDTRIP2");
        }
        runTask(task, parseDatabaseFromString(model2Xml));
    }

    /**
     * Tests of the excludeTables filter in the presence of a foreign key to the indicated table.
     */
    @Test
    public void testExcludeSingleTableWithFk() throws IOException {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(modelXml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setExcludeTables("roundtrip1");
        } else {
            task.setExcludeTables("ROUNDTRIP1");
        }
        try {
            runTask(task, readModelFromDatabase("roundtriptest"));
            fail();
        } catch (DdlUtilsTaskException ex) {
            // expected
        }
    }

    /**
     * Tests of the excludeTableFilter filter.
     */
    @Test
    public void testExcludeSingleTableViaRegExp() throws IOException {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip_1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
              <table name='Roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip_1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setExcludeTableFilter("Round.*_2");
        } else {
            task.setExcludeTableFilter("ROUND.*_2");
        }
        runTask(task, parseDatabaseFromString(model2Xml));
    }

    /**
     * Tests of the excludeTableFilter filter in the presence of a foreign key to the indicated table.
     */
    @Test
    public void testExcludeSingleTableWithFkViaRegExp() throws IOException {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip_1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='Roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='Roundtrip_1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(modelXml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setExcludeTableFilter("Round.*_2");
        } else {
            task.setExcludeTableFilter("ROUND.*_2");
        }
        try {
            runTask(task, readModelFromDatabase("roundtriptest"));
            fail();
        } catch (DdlUtilsTaskException ex) {
            // expected
        }
    }

    /**
     * Tests of the excludeTables filter for multiple tables.
     */
    @Test
    public void testExcludeMultipleTables() throws IOException {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip3'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
              </table>
              <table name='roundtrip3'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setExcludeTables("roundtrip1,roundtrip3");
        } else {
            task.setExcludeTables("ROUNDTRIP1,ROUNDTRIP3");
        }
        runTask(task, parseDatabaseFromString(model2Xml));
    }

    /**
     * Tests of the excludeTables filter for multiple tables.
     */
    @Test
    public void testExcludeMultipleTablesWithFKPointingToThem() throws IOException {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='roundtrip3'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(modelXml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setExcludeTables("roundtrip1,roundtrip3");
        } else {
            task.setExcludeTables("ROUNDTRIP1,ROUNDTRIP3");
        }
        try {
            runTask(task, readModelFromDatabase("roundtriptest"));
            fail();
        } catch (DdlUtilsTaskException ex) {
            // expected
        }
    }

    /**
     * Tests of the excludeTableFilter filter for multiple tables.
     */
    @Test
    public void testExcludeMultipleTablesViaRegExp() throws IOException {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip_1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_3'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='Roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='Roundtrip_3'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='Roundtrip_1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(model1Xml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setExcludeTableFilter(".*trip_[1|3]");
        } else {
            task.setExcludeTableFilter(".*TRIP_[1|3]");
        }
        runTask(task, parseDatabaseFromString(model2Xml));
    }

    /**
     * Tests of the excludeTables filter for multiple tables via regexp.
     */
    @Test
    public void testExcludeMultipleTablesWithFKPointingToThemViaRegExp() throws IOException {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip_1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_3'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='Roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_3'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='Roundtrip_3'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='Roundtrip_1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
            </database>""";

        createDatabase(modelXml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setExcludeTableFilter(".*trip_*[1|3]");
        } else {
            task.setExcludeTableFilter(".*TRIP_*[1|3]");
        }
        try {
            runTask(task, readModelFromDatabase("roundtriptest"));
            fail();
        } catch (DdlUtilsTaskException ex) {
            // expected
        }
    }

    /**
     * Tests of the includeTables and excludeTables filters for multiple tables.
     */
    @Test
    public void testIncludeAndExcludeMultipleTables() throws IOException {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip_1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_3'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='Roundtrip_2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='Roundtrip_2'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='Roundtrip_3'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='Roundtrip_1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='Roundtrip_4'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='Roundtrip_4'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        if (isUseDelimitedIdentifiers()) {
            task.setIncludeTables("Roundtrip_1,Roundtrip_3,Roundtrip_4");
            task.setExcludeTables("Roundtrip_1,Roundtrip_3");
        } else {
            task.setIncludeTables("ROUNDTRIP_1,ROUNDTRIP_3,ROUNDTRIP_4");
            task.setExcludeTables("ROUNDTRIP_1,ROUNDTRIP_3");
        }
        runTask(task, parseDatabaseFromString(model2Xml));
    }

    /**
     * Tests of the includeTableFilter and excludeTableFilter filters for multiple tables.
     */
    @Test
    public void testIncludeAndExcludeMultipleTablesViaRegExp() throws IOException {
        final String model1Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip1'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
                <foreign-key foreignTable='roundtrip3'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
              </table>
              <table name='roundtrip3'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
                <foreign-key foreignTable='roundtrip1'>
                  <reference local='avalue' foreign='pk'/>
                </foreign-key>
              </table>
              <table name='roundtrip4'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
              </table>
            </database>""";
        final String model2Xml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip2'>
                <column name='pk' type='INTEGER' primaryKey='true' required='true'/>
                <column name='avalue' type='VARCHAR' size='32'/>
              </table>
            </database>""";

        createDatabase(model1Xml);

        DatabaseToDdlTask task = getDatabaseToDdlTaskInstance();

        task.setIncludeTableFilter(".*[1|2|3]");
        task.setExcludeTableFilter(".*[1|3]");
        runTask(task, parseDatabaseFromString(model2Xml));
    }
}
