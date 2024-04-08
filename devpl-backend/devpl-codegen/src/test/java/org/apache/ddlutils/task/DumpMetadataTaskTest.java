package org.apache.ddlutils.task;

import org.apache.ddlutils.jdbc.PooledDataSourceWrapper;
import org.junit.Test;

public class DumpMetadataTaskTest {

    @Test
    public void testDumpMetadata() throws Exception {
        DumpMetadataTask task = new DumpMetadataTask();

        try (PooledDataSourceWrapper dataSource = new PooledDataSourceWrapper()) {

            task.addConfiguredDatabase(dataSource);

        }


    }
}
