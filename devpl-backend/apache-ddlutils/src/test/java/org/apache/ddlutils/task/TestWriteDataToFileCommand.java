package org.apache.ddlutils.task;

import org.apache.ddlutils.io.DataReader;
import org.apache.ddlutils.io.DataSink;
import org.apache.ddlutils.io.DataSinkException;
import org.apache.ddlutils.model.TableModel;
import org.apache.ddlutils.model.TableRow;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests the writeDataToFile sub-task.
 */
public class TestWriteDataToFileCommand extends TestTaskBase {

    /**
     * Adds the writeDataToFile sub-task to the given task, executes it, and checks its output.
     *
     * @param task         The task
     * @param expectedData A map table name -> list of dyna beans sorted by the pk value that is expected
     */
    private void runTask(DatabaseToDdlTask task, Map<String, List<TableRow>> expectedData) throws IOException {
        WriteDataToFileCommand subTask = new WriteDataToFileCommand();
        File tmpFile = File.createTempFile("data", ".xml");

        try {
            subTask.setOutputFile(tmpFile);
            task.addWriteDataToFile(subTask);
            task.setModelName("roundtriptest");
            task.execute();

            DataReader dataReader = new DataReader();
            final Map<String, List<TableRow>> readData = new HashMap<>();

            dataReader.setModel(getAdjustedModel());
            dataReader.setSink(new DataSink() {
                @Override
                public void addBean(TableRow bean) throws DataSinkException {
                    String key = ((TableModel) bean.getTableModel()).getTableName();
                    List<TableRow> beans = readData.computeIfAbsent(key, k -> new ArrayList<>());
                    beans.add(bean);
                }

                @Override
                public void end() throws DataSinkException {
                }

                @Override
                public void start() throws DataSinkException {
                }
            });
            dataReader.read(tmpFile);

            assertEquals("Not the same tables in the expected and actual data", expectedData.keySet(), readData.keySet());
        } finally {
            if (!tmpFile.delete()) {
                getLog().warn("Could not delete temporary file " + tmpFile.getAbsolutePath());
            }
        }
    }

    /**
     * Tests the task against an empty database.
     */
    @Test
    public void testEmptyDatabase() throws IOException {
        runTask(getDatabaseToDdlTaskInstance(), new HashMap<>());
    }

    /**
     * Tests against a simple model.
     */
    @Test
    public void testSimpleModel() throws IOException {
        final String modelXml = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='roundtriptest'>
              <table name='roundtrip'>
                <column name='pk' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='avalue' type='INTEGER'/>
              </table>
            </database>""";

        createDatabase(modelXml);

        List<TableRow> beans = new ArrayList<>();

        beans.add(insertRow("roundtrip", new Object[]{"test1", (1)}));
        beans.add(insertRow("roundtrip", new Object[]{"test2", null}));

        Map<String, List<TableRow>> expected = new HashMap<>();

        expected.put("roundtrip", beans);
        runTask(getDatabaseToDdlTaskInstance(), expected);
    }

    /**
     * Tests against a model with multiple tables and foreign keys.
     */
    @Test
    public void testComplexModel() throws IOException {
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

        List<TableRow> beans1 = new ArrayList<>();
        List<TableRow> beans2 = new ArrayList<>();
        List<TableRow> beans3 = new ArrayList<>();

        beans1.add(insertRow("Roundtrip_1", new Object[]{"test1", null}));
        beans2.add(insertRow("Roundtrip_2", new Object[]{(3), null}));
        beans3.add(insertRow("Roundtrip_3", new Object[]{(1), "test1"}));
        beans2.add(insertRow("Roundtrip_2", new Object[]{(2), (1)}));
        beans1.add(insertRow("Roundtrip_1", new Object[]{"test2", (1)}));
        beans3.add(insertRow("Roundtrip_3", new Object[]{(3), null}));
        beans3.add(insertRow("Roundtrip_3", new Object[]{(4), "test2"}));
        beans1.add(insertRow("Roundtrip_1", new Object[]{"test3", (3)}));
        beans3.add(insertRow("Roundtrip_3", new Object[]{(2), "test3"}));
        beans2.add(insertRow("Roundtrip_2", new Object[]{(1), (2)}));

        Map<String, List<TableRow>> expected = new HashMap<>();

        expected.put("Roundtrip_1", beans1);
        expected.put("Roundtrip_2", beans2);
        expected.put("Roundtrip_3", beans3);
        runTask(getDatabaseToDdlTaskInstance(), expected);
    }

}
