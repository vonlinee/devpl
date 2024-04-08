package org.apache.ddlutils.platform;


import org.apache.ddlutils.TestPlatformBase;

/**
 * Tests the MaxDB platform.
 */
public class TestMaxDbPlatform extends TestPlatformBase {

    protected String getDatabaseName() {
        return DBTypeEnum.MAXDB.getName();
    }

    /**
     * Tests the column types.
     */
    public void testColumnTypes() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            DROP TABLE "coltype" CASCADE;
            CREATE TABLE "coltype"
            (
                "COL_ARRAY"           LONG BYTE,
                "COL_BIGINT"          FIXED(38,0),
                "COL_BINARY"          CHAR(254) BYTE,
                "COL_BIT"             BOOLEAN,
                "COL_BLOB"            LONG BYTE,
                "COL_BOOLEAN"         BOOLEAN,
                "COL_CHAR"            CHAR(15),
                "COL_CLOB"            LONG,
                "COL_DATALINK"        LONG BYTE,
                "COL_DATE"            DATE,
                "COL_DECIMAL"         FIXED(15,3),
                "COL_DECIMAL_NOSCALE" FIXED(15,0),
                "COL_DISTINCT"        LONG BYTE,
                "COL_DOUBLE"          FLOAT(38),
                "COL_FLOAT"           FLOAT(38),
                "COL_INTEGER"         INTEGER,
                "COL_JAVA_OBJECT"     LONG BYTE,
                "COL_LONGVARBINARY"   LONG BYTE,
                "COL_LONGVARCHAR"     LONG,
                "COL_NULL"            LONG BYTE,
                "COL_NUMERIC"         FIXED(15,0),
                "COL_OTHER"           LONG BYTE,
                "COL_REAL"            FLOAT(16),
                "COL_REF"             LONG BYTE,
                "COL_SMALLINT"        SMALLINT,
                "COL_STRUCT"          LONG BYTE,
                "COL_TIME"            TIME,
                "COL_TIMESTAMP"       TIMESTAMP,
                "COL_TINYINT"         SMALLINT,
                "COL_VARBINARY"       VARCHAR(15) BYTE,
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
            CREATE TABLE "constraints"
            (
                "COL_PK"               VARCHAR(32),
                "COL_PK_AUTO_INCR"     INTEGER DEFAULT SERIAL(1),
                "COL_NOT_NULL"         CHAR(100) BYTE NOT NULL,
                "COL_NOT_NULL_DEFAULT" FLOAT(38) DEFAULT -2.0 NOT NULL,
                "COL_DEFAULT"          CHAR(4) DEFAULT 'test',
                "COL_AUTO_INCR"        FIXED(38,0) DEFAULT SERIAL(1),
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
            ALTER TABLE "table2" DROP CONSTRAINT "table2_FK_COL_FK_COL_FK_2_table1";
            DROP TABLE "table3" CASCADE;
            DROP TABLE "table2" CASCADE;
            DROP TABLE "table1" CASCADE;
            CREATE TABLE "table1"
            (
                "COL_PK_1"    VARCHAR(32) NOT NULL,
                "COL_PK_2"    INTEGER,
                "COL_INDEX_1" CHAR(100) BYTE NOT NULL,
                "COL_INDEX_2" FLOAT(38) NOT NULL,
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
            ALTER TABLE "table2" ADD CONSTRAINT "table2_FK_COL_FK_COL_FK_2_table1" FOREIGN KEY ("COL_FK_1", "COL_FK_2") REFERENCES "table1" ("COL_PK_2", "COL_PK_1");
            ALTER TABLE "table3" ADD CONSTRAINT "testfk" FOREIGN KEY ("COL_FK") REFERENCES "table2" ("COL_PK");
            """, getTableConstraintTestDatabaseCreationSql());
    }

    /**
     * Tests the proper escaping of character sequences where MaxDb requires it.
     */
    public void testCharacterEscaping() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            DROP TABLE "escapedcharacters" CASCADE;
            CREATE TABLE "escapedcharacters"
            (
                "COL_PK"   INTEGER,
                "COL_TEXT" VARCHAR(128) DEFAULT '''',
                PRIMARY KEY ("COL_PK")
            );
            """, getCharEscapingTestDatabaseCreationSql());
    }
}
