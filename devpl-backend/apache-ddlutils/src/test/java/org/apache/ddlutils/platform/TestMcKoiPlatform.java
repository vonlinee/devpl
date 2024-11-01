package org.apache.ddlutils.platform;


import org.apache.ddlutils.TestPlatformBase;

/**
 * Tests the McKoi platform.
 */
public class TestMcKoiPlatform extends TestPlatformBase {

    protected String getDatabaseName() {
        return BuiltinDatabaseType.MCKOI.getName();
    }

    /**
     * Tests the column types.
     */
    public void testColumnTypes() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            DROP TABLE IF EXISTS "coltype";
            CREATE TABLE "coltype"
            (
                "COL_ARRAY"           BLOB,
                "COL_BIGINT"          BIGINT,
                "COL_BINARY"          BINARY(1024),
                "COL_BIT"             BOOLEAN,
                "COL_BLOB"            BLOB,
                "COL_BOOLEAN"         BOOLEAN,
                "COL_CHAR"            CHAR(15),
                "COL_CLOB"            CLOB,
                "COL_DATALINK"        BLOB,
                "COL_DATE"            DATE,
                "COL_DECIMAL"         DECIMAL(15,3),
                "COL_DECIMAL_NOSCALE" DECIMAL(15,0),
                "COL_DISTINCT"        BLOB,
                "COL_DOUBLE"          DOUBLE,
                "COL_FLOAT"           DOUBLE,
                "COL_INTEGER"         INTEGER,
                "COL_JAVA_OBJECT"     JAVA_OBJECT,
                "COL_LONGVARBINARY"   LONGVARBINARY,
                "COL_LONGVARCHAR"     LONGVARCHAR,
                "COL_NULL"            BLOB,
                "COL_NUMERIC"         NUMERIC(15,0),
                "COL_OTHER"           BLOB,
                "COL_REAL"            REAL,
                "COL_REF"             BLOB,
                "COL_SMALLINT"        SMALLINT,
                "COL_STRUCT"          BLOB,
                "COL_TIME"            TIME,
                "COL_TIMESTAMP"       TIMESTAMP,
                "COL_TINYINT"         TINYINT,
                "COL_VARBINARY"       VARBINARY(15),
                "COL_VARCHAR"         VARCHAR(15)
            );
            """, getColumnTestDatabaseCreationSql());
    }

    /**
     * Tests the column constraints.
     */
    public void testColumnConstraints() throws Exception {
        // note that this is not valid SQL as obviously only one auto increment field
        // can be defined for each table
        assertEqualsIgnoringWhitespaces("""
            DROP TABLE IF EXISTS "constraints";
            DROP SEQUENCE "seq_constraints_COL_PK_AUTO_INCR";
            DROP SEQUENCE "seq_constraints_COL_AUTO_INCR";
            CREATE SEQUENCE "seq_constraints_COL_PK_AUTO_INCR";
            CREATE SEQUENCE "seq_constraints_COL_AUTO_INCR";
            CREATE TABLE "constraints"
            (
                "COL_PK"               VARCHAR(32),
                "COL_PK_AUTO_INCR"     INTEGER DEFAULT NEXTVAL('seq_constraints_COL_PK_AUTO_INCR'),
                "COL_NOT_NULL"         BINARY(100) NOT NULL,
                "COL_NOT_NULL_DEFAULT" DOUBLE DEFAULT -2.0 NOT NULL,
                "COL_DEFAULT"          CHAR(4) DEFAULT 'test',
                "COL_AUTO_INCR"        BIGINT DEFAULT NEXTVAL('seq_constraints_COL_AUTO_INCR'),
                PRIMARY KEY ("COL_PK", "COL_PK_AUTO_INCR")
            );
            """, getConstraintTestDatabaseCreationSql());
    }

    /**
     * Tests the table constraints.
     */
    public void testTableConstraints() throws Exception {
        // The database schema for testing table constraints, i.e. foreign keys and indices.
        // This schema is adapted for McKoi which does not support non-unique indices
        final String schema = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='tableconstraintstest'>
              <table name='table1'>
                <column name='COL_PK_1' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='COL_PK_2' type='INTEGER' primaryKey='true'/>
                <column name='COL_INDEX_1' type='BINARY' size='100' required='true'/>
                <column name='COL_INDEX_2' type='DOUBLE' required='true'/>
                <column name='COL_INDEX_3' type='CHAR' size='4'/>
                <unique name='testindex1'>
                  <unique-column name='COL_INDEX_2'/>
                </unique>
                <unique name='testindex2'>
                  <unique-column name='COL_INDEX_3'/>
                  <unique-column name='COL_INDEX_1'/>
                </unique>
              </table>
              <table name='table2'>
                <column name='COL_PK' type='INTEGER' primaryKey='true'/>
                <column name='COL_FK_1' type='INTEGER'/>
                <column name='COL_FK_2' type='VARCHAR' size='32' required='true'/>
                <foreign-key foreignTable='table1'>
                  <reference local='COL_FK_1' foreign='COL_PK_2'/>
                  <reference local='COL_FK_2' foreign='COL_PK_1'/>
                </foreign-key>
              </table>
              <table name='table3'>
                <column name='COL_PK' type='VARCHAR' size='16' primaryKey='true'/>
                <column name='COL_FK' type='INTEGER' required='true'/>
                <foreign-key name='testfk' foreignTable='table2'>
                  <reference local='COL_FK' foreign='COL_PK'/>
                </foreign-key>
              </table>
            </database>""";

        assertEqualsIgnoringWhitespaces("""
            ALTER TABLE "table3" DROP CONSTRAINT "testfk";
            ALTER TABLE "table2" DROP CONSTRAINT "table2_FK_COL_FK_1_COL_FK_2_table1";
            DROP TABLE IF EXISTS "table3";
            DROP TABLE IF EXISTS "table2";
            DROP TABLE IF EXISTS "table1";
            CREATE TABLE "table1"
            (
                "COL_PK_1"    VARCHAR(32) NOT NULL,
                "COL_PK_2"    INTEGER,
                "COL_INDEX_1" BINARY(100) NOT NULL,
                "COL_INDEX_2" DOUBLE NOT NULL,
                "COL_INDEX_3" CHAR(4),
                PRIMARY KEY ("COL_PK_1", "COL_PK_2")
            );
            CREATE TABLE "table2"
            (
                "COL_PK"   INTEGER,
                "COL_FK_1" INTEGER,
                "COL_FK_2" VARCHAR(32) NOT NULL,
                PRIMARY KEY ("COL_PK")
            );
            CREATE TABLE "table3"
            (
                "COL_PK" VARCHAR(16),
                "COL_FK" INTEGER NOT NULL,
                PRIMARY KEY ("COL_PK")
            );
            ALTER TABLE "table2" ADD CONSTRAINT "table2_FK_COL_FK_1_COL_FK_2_table1" FOREIGN KEY ("COL_FK_1", "COL_FK_2") REFERENCES "table1" ("COL_PK_2", "COL_PK_1");
            ALTER TABLE "table3" ADD CONSTRAINT "testfk" FOREIGN KEY ("COL_FK") REFERENCES "table2" ("COL_PK");
            """, getDatabaseCreationSql(schema));
    }

    /**
     * Tests the proper escaping of character sequences where McKoi requires it.
     */
    public void testCharacterEscaping() throws Exception {
        // McKoi-specific schema
        final String schema = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='escapetest'>
              <table name='escapedcharacters'>
                <column name='COL_PK' type='INTEGER' primaryKey='true'/>
                <column name='COL_TEXT' type='VARCHAR' size='128' default='&#39; \\'/>
              </table>
            </database>""";

        assertEqualsIgnoringWhitespaces("""
            DROP TABLE IF EXISTS "escapedcharacters";
            CREATE TABLE "escapedcharacters"
            (
                "COL_PK"   INTEGER,
                "COL_TEXT" VARCHAR(128) DEFAULT '\\' \\\\',
                PRIMARY KEY ("COL_PK")
            );
            """, getDatabaseCreationSql(schema));
    }
}
