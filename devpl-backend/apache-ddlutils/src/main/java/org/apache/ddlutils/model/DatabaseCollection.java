package org.apache.ddlutils.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库集合，通常代表一个数据库连接的所有数据库
 *
 * @see Database
 */
public class DatabaseCollection extends SchemaObject {

    private List<Database> databases;

    public void addDatabase(Database database) {
        if (database == null) {
            this.databases = new ArrayList<>();
        }
        this.databases.add(database);
    }

    public List<Database> getDatabases() {
        return databases;
    }
}
