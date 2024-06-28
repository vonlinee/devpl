package org.apache.ddlutils.task;

import org.apache.ddlutils.jdbc.PooledDataSourceWrapper;
import org.apache.ddlutils.platform.mysql.MySql5xPlatform;
import org.apache.ddlutils.util.DatabaseTestHelper;
import org.junit.jupiter.api.Test;

import java.io.File;

public class TestDatabaseToDDLTask {

    @Test
    public void testDatabaseToDDL() throws Exception {
        try (PooledDataSourceWrapper dataSource = DatabaseTestHelper.mysqlDataSource()) {
            MySql5xPlatform platform = new MySql5xPlatform();
            platform.setDataSource(dataSource);

            DatabaseToDdlTask task = new DatabaseToDdlTask();
            task.setShutdownDatabase(true);
            task.setPlatform(platform);
            task.addConfiguredDatabase(dataSource);
            task.setDatabaseName("devpl");

            DatabaseToDdlCommand command = new DatabaseToDdlCommand();
            command.setOutputFile(new File("D:/Temp/1.sql"));
            task.addCommand(command);
            task.execute();
        }
    }
}
