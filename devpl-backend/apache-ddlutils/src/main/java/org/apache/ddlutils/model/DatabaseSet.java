package org.apache.ddlutils.model;

import java.util.List;

/**
 * 数据库集合，通常代表一个数据库连接的所有数据库
 *
 * @see Database
 */
public class DatabaseSet extends SchemaObject {

    private List<Database> databases;
}
