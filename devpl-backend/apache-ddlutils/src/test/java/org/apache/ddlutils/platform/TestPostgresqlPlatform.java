package org.apache.ddlutils.platform;


import org.apache.ddlutils.TestPlatformBase;

/**
 * Tests the PostgreSQL platform.
 */
public class TestPostgresqlPlatform extends TestPlatformBase {

    protected String getDatabaseName() {
        return BuiltinDBType.POSTGRE_SQL.getName();
    }

    /**
     * Tests the column types.
     */
    public void testColumnTypes() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            DROP TABLE "coltype" CASCADE;
            CREATE TABLE "coltype"
            (
                "COL_ARRAY"           BYTEA,
                "COL_BIGINT"          BIGINT,
                "COL_BINARY"          BYTEA,
                "COL_BIT"             BOOLEAN,
                "COL_BLOB"            BYTEA,
                "COL_BOOLEAN"         BOOLEAN,
                "COL_CHAR"            CHAR(15),
                "COL_CLOB"            TEXT,
                "COL_DATALINK"        BYTEA,
                "COL_DATE"            DATE,
                "COL_DECIMAL"         NUMERIC(15,3),
                "COL_DECIMAL_NOSCALE" NUMERIC(15,0),
                "COL_DISTINCT"        BYTEA,
                "COL_DOUBLE"          DOUBLE PRECISION,
                "COL_FLOAT"           DOUBLE PRECISION,
                "COL_INTEGER"         INTEGER,
                "COL_JAVA_OBJECT"     BYTEA,
                "COL_LONGVARBINARY"   BYTEA,
                "COL_LONGVARCHAR"     TEXT,
                "COL_NULL"            BYTEA,
                "COL_NUMERIC"         NUMERIC(15,0),
                "COL_OTHER"           BYTEA,
                "COL_REAL"            REAL,
                "COL_REF"             BYTEA,
                "COL_SMALLINT"        SMALLINT,
                "COL_STRUCT"          BYTEA,
                "COL_TIME"            TIME,
                "COL_TIMESTAMP"       TIMESTAMP,
                "COL_TINYINT"         SMALLINT,
                "COL_VARBINARY"       BYTEA,
                "COL_VARCHAR"         VARCHAR(15)
            );
            """, getColumnTestDatabaseCreationSql());
    }

    /**
     * Tests the column constraints.
     */
    public void testColumnConstraints() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            DROP TABLE "constraints" CASCADE;
            DROP SEQUENCE "constraints_COL_K_AUTO_INCR_seq";
            DROP SEQUENCE "constraints_COL_AUTO_INCR_seq";
            CREATE SEQUENCE "constraints_COL_K_AUTO_INCR_seq";
            CREATE SEQUENCE "constraints_COL_AUTO_INCR_seq";
            CREATE TABLE "constraints"
            (
                "COL_PK"               VARCHAR(32),
                "COL_PK_AUTO_INCR"     INTEGER UNIQUE DEFAULT nextval('"constraints_COL_K_AUTO_INCR_seq"'),
                "COL_NOT_NULL"         BYTEA NOT NULL,
                "COL_NOT_NULL_DEFAULT" DOUBLE PRECISION DEFAULT -2.0 NOT NULL,
                "COL_DEFAULT"          CHAR(4) DEFAULT 'test',
                "COL_AUTO_INCR"        BIGINT UNIQUE DEFAULT nextval('"constraints_COL_AUTO_INCR_seq"'),
                PRIMARY KEY ("COL_PK", "COL_PK_AUTO_INCR")
            );
            """, getConstraintTestDatabaseCreationSql());
    }

    /**
     * Tests the table constraints.
     */
    public void testTableConstraints() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            ALTER TABLE "table3" DROP CONSTRAINT "testfk";
            ALTER TABLE "table2" DROP CONSTRAINT "table2_FK_COL_F_COL_FK_2_table1";
            DROP TABLE "table3" CASCADE;
            DROP TABLE "table2" CASCADE;
            DROP TABLE "table1" CASCADE;
            CREATE TABLE "table1"
            (
                "COL_PK_1"    VARCHAR(32) NOT NULL,
                "COL_PK_2"    INTEGER,
                "COL_INDEX_1" BYTEA NOT NULL,
                "COL_INDEX_2" DOUBLE PRECISION NOT NULL,
                "COL_INDEX_3" CHAR(4),
                PRIMARY KEY ("COL_PK_1", "COL_PK_2")
            );
            CREATE INDEX "testindex1" ON "table1" ("COL_INDEX_2");
            CREATE UNIQUE INDEX "testindex2" ON "table1" ("COL_INDEX_3", "COL_INDEX_1");
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
            ALTER TABLE "table2" ADD CONSTRAINT "table2_FK_COL_F_COL_FK_2_table1" FOREIGN KEY ("COL_FK_1", "COL_FK_2") REFERENCES "table1" ("COL_PK_2", "COL_PK_1");
            ALTER TABLE "table3" ADD CONSTRAINT "testfk" FOREIGN KEY ("COL_FK") REFERENCES "table2" ("COL_PK");
            """, getTableConstraintTestDatabaseCreationSql());
    }

    /**
     * Tests the proper escaping of character sequences where PostgreSQL requires it.
     */
    public void testCharacterEscaping() throws Exception {
        // PostgreSql specific database schema for testing escaping of character sequences
        final String schema = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='escapetest'>
              <table name='escapedcharacters'>
                <column name='COL_PK' type='INTEGER' primaryKey='true'/>
                <column name='COL_TEXT' type='VARCHAR' size='128' default='&#39; &#09; &#10; &#13; \\'/>
              </table>
            </database>""";

        assertEqualsIgnoringWhitespaces("""
            DROP TABLE "escapedcharacters" CASCADE;
            CREATE TABLE "escapedcharacters"
            (
                "COL_PK"   INTEGER,
                "COL_TEXT" VARCHAR(128) DEFAULT '\\' \\t \\n \\r \\\\',
                PRIMARY KEY ("COL_PK")
            );
            """, getDatabaseCreationSql(schema));
    }
}
