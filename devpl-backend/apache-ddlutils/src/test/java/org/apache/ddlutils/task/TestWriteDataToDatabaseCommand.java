package org.apache.ddlutils.task;

import junit.framework.Test;
import org.apache.ddlutils.model.TableRow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Tests the writeDataToDatabase sub-task.
 */
public class TestWriteDataToDatabaseCommand extends TestTaskBase {
    /**
     * Parameterized test case pattern.
     *
     * @return The tests
     */
    public static Test suite() throws Exception {
        return getTests(TestWriteDataToDatabaseCommand.class);
    }

    /**
     * Adds the writeDataToDatabase sub task to the given task, executes it, and checks its output.
     *
     * @param task          The task
     * @param dataXml       The data xml to write
     * @param useBatchMode  Whether to use batch mode for inserting the data
     * @param ensureFkOrder Whether to ensure foreign key order
     */
    private void runTask(DatabaseToDdlTask task, String dataXml, boolean useBatchMode, boolean ensureFkOrder) throws IOException {
        WriteDataToDatabaseCommand subTask = new WriteDataToDatabaseCommand();
        File tmpFile = File.createTempFile("schema", ".xml");
        FileWriter writer = null;

        try {
            writer = new FileWriter(tmpFile);

            writer.write(dataXml);
            writer.close();

            subTask.setDataFile(tmpFile);
            subTask.setBatchSize(100);
            subTask.setFailOnError(true);
            subTask.setUseBatchMode(useBatchMode);
            subTask.setEnsureForeignKeyOrder(ensureFkOrder);
            task.addWriteDataToDatabase(subTask);
            task.setDatabaseName("roundtriptest");
            task.execute();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    getLog().error("Could not close the writer for the temporary file " + tmpFile.getAbsolutePath(), ex);
                }
            }
            if (!tmpFile.delete()) {
                getLog().warn("Could not delete temporary file " + tmpFile.getAbsolutePath());
            }
        }
    }

    /**
     * Basic test that creates a schema and puts some data into it.
     */
    public void testSimple() throws Exception {
        final String modelXml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                </database>""";
        final String dataXml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <data>
                  <roundtrip pk='val1' avalue='1'/>
                  <roundtrip pk='val2' avalue='2'/>
                  <roundtrip pk='val3' avalue='3'/>
                </data>""";

        createDatabase(modelXml);

        runTask(getDatabaseToDdlTaskInstance(), dataXml, false, false);

        List<TableRow> rows = getRows("roundtrip", "pk");

        assertEquals(3, rows.size());
        assertEquals((Object) "val1", rows.get(0), "pk");
        assertEquals((1), rows.get(0), "avalue");
        assertEquals((Object) "val2", rows.get(1), "pk");
        assertEquals((2), rows.get(1), "avalue");
        assertEquals((Object) "val3", rows.get(2), "pk");
        assertEquals((3), rows.get(2), "avalue");
    }

    /**
     * Tests data insertion in batch mode.
     */
    public void testBatchMode() throws Exception {
        final String modelXml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
                  <table name='roundtrip'>
                    <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                    <column name='avalue' type='INTEGER'/>
                  </table>
                </database>""";

        StringBuilder dataXml = new StringBuilder();
        final int numObjs = 2000;

        dataXml.append("<?xml version='1.0' encoding='ISO-8859-1'?>\n<data>");
        for (int idx = 0; idx < numObjs; idx++) {
            dataXml.append("  <roundtrip pk='val");
            dataXml.append(idx);
            dataXml.append("' avalue='");
            dataXml.append(idx);
            dataXml.append("'/>\n");
        }
        dataXml.append("</data>");

        createDatabase(modelXml);

        runTask(getDatabaseToDdlTaskInstance(), dataXml.toString(), true, false);

        List<TableRow> rows = getRows("roundtrip", "avalue");

        assertEquals(numObjs, rows.size());
        for (int idx = 0; idx < numObjs; idx++) {
            assertEquals((Object) ("val" + idx), rows.get(idx), "pk");
            assertEquals((idx), rows.get(idx), "avalue");
        }
    }
}
