package org.apache.ddlutils.task;

import org.apache.ddlutils.TestAgainstLiveDatabaseBase;
import org.apache.ddlutils.jdbc.PooledDataSourceWrapper;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;

public class DumpMetadataTaskTest extends TestAgainstLiveDatabaseBase {

    @Test
    public void testDumpMetadata() throws Exception {
        DumpMetadataTask task = new DumpMetadataTask();
        System.setProperty(JDBC_PROPERTIES_PROPERTY, "jdbc.properties.mysql57");
        DataSource dataSource = getDataSourceFromProperties();

        task.setConfiguredDatabase(PooledDataSourceWrapper.wrap(dataSource));
        task.setDumpTables(true);
        task.setOutputFile(new File("D:/Temp/2.xml"));
        task.execute();
    }
}
