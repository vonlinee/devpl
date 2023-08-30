package org.apache.ddlutils.model;

/**
 * schema包含的对象可以是表，列，数据类型，视图，存储过程，关系，主键，外键等
 * https://database.guide/schema-definitions-by-dbms/
 */
public interface SchemaObject {

    /**
     * the name of this Schema Object
     * @return name
     */
    String getName();
}
