package org.apache.ddlutils.platform;


import org.apache.ddlutils.TestPlatformBase;

/**
 * Tests the Oracle 9 platform.
 *
 * @version $Revision: 231110 $
 */
public class TestOracle9Platform extends TestPlatformBase {

    protected String getDatabaseName() {
        return DBTypeEnum.ORACLE9.getName();
    }

    /**
     * Tests the column types.
     */
    public void testColumnTypes() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            DROP TABLE "coltype" CASCADE CONSTRAINTS;
            CREATE TABLE "coltype"
            (
                "COL_ARRAY"           BLOB,
                "COL_BIGINT"          NUMBER(38),
                "COL_BINARY"          RAW(254),
                "COL_BIT"             NUMBER(1),
                "COL_BLOB"            BLOB,
                "COL_BOOLEAN"         NUMBER(1),
                "COL_CHAR"            CHAR(15),
                "COL_CLOB"            CLOB,
                "COL_DATALINK"        BLOB,
                "COL_DATE"            DATE,
                "COL_DECIMAL"         NUMBER(15,3),
                "COL_DECIMAL_NOSCALE" NUMBER(15,0),
                "COL_DISTINCT"        BLOB,
                "COL_DOUBLE"          DOUBLE PRECISION,
                "COL_FLOAT"           FLOAT,
                "COL_INTEGER"         INTEGER,
                "COL_JAVA_OBJECT"     BLOB,
                "COL_LONGVARBINARY"   BLOB,
                "COL_LONGVARCHAR"     CLOB,
                "COL_NULL"            BLOB,
                "COL_NUMERIC"         NUMBER(15,0),
                "COL_OTHER"           BLOB,
                "COL_REAL"            REAL,
                "COL_REF"             BLOB,
                "COL_SMALLINT"        NUMBER(5),
                "COL_STRUCT"          BLOB,
                "COL_TIME"            DATE,
                "COL_TIMESTAMP"       TIMESTAMP,
                "COL_TINYINT"         NUMBER(3),
                "COL_VARBINARY"       RAW(15),
                "COL_VARCHAR"         VARCHAR2(15)
            );
            """, getColumnTestDatabaseCreationSql());
    }

    /**
     * Tests the column constraints.
     */
    public void testColumnConstraints() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            DROP TRIGGER "trg_constraints_L_PK_AUTO_INCR";
            DROP SEQUENCE "seq_constraints_L_PK_AUTO_INCR";
            DROP TRIGGER "trg_constraints_COL_AUTO_INCR";
            DROP SEQUENCE "seq_constraints_COL_AUTO_INCR";
            DROP TABLE "constraints" CASCADE CONSTRAINTS;
            CREATE SEQUENCE "seq_constraints_L_PK_AUTO_INCR";
            CREATE SEQUENCE "seq_constraints_COL_AUTO_INCR";
            CREATE TABLE "constraints"
            (
                "COL_PK"               VARCHAR2(32),
                "COL_PK_AUTO_INCR"     INTEGER,
                "COL_NOT_NULL"         RAW(100) NOT NULL,
                "COL_NOT_NULL_DEFAULT" DOUBLE PRECISION DEFAULT -2.0 NOT NULL,
                "COL_DEFAULT"          CHAR(4) DEFAULT 'test',
                "COL_AUTO_INCR"        NUMBER(38),
                PRIMARY KEY ("COL_PK", "COL_PK_AUTO_INCR")
            );
            CREATE OR REPLACE TRIGGER "trg_constraints_L_PK_AUTO_INCR" BEFORE INSERT ON "constraints" FOR EACH ROW WHEN (new."COL_PK_AUTO_INCR" IS NULL)
            BEGIN SELECT "seq_constraints_L_PK_AUTO_INCR".nextval INTO :new."COL_PK_AUTO_INCR" FROM dual; END;;
            CREATE OR REPLACE TRIGGER "trg_constraints_COL_AUTO_INCR" BEFORE INSERT ON "constraints" FOR EACH ROW WHEN (new."COL_AUTO_INCR" IS NULL)
            BEGIN SELECT "seq_constraints_COL_AUTO_INCR".nextval INTO :new."COL_AUTO_INCR" FROM dual; END;;
            """, getConstraintTestDatabaseCreationSql());
    }

    /**
     * Tests the table constraints.
     */
    public void testTableConstraints() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            DROP TABLE "table3" CASCADE CONSTRAINTS;
            DROP TABLE "table2" CASCADE CONSTRAINTS;
            DROP TABLE "table1" CASCADE CONSTRAINTS;
            CREATE TABLE "table1"
            (
                "COL_PK_1"    VARCHAR2(32) NOT NULL,
                "COL_PK_2"    INTEGER,
                "COL_INDEX_1" RAW(100) NOT NULL,
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
                "COL_FK_2" VARCHAR2(32) NOT NULL,
                PRIMARY KEY ("COL_PK")
            );
            CREATE TABLE "table3"
            (
                "COL_PK" VARCHAR2(16),
                "COL_FK" INTEGER NOT NULL,
                PRIMARY KEY ("COL_PK")
            );
            ALTER TABLE "table2" ADD CONSTRAINT "table2_FK_COL_F_OL_FK_2_table1" FOREIGN KEY ("COL_FK_1", "COL_FK_2") REFERENCES "table1" ("COL_PK_2", "COL_PK_1");
            ALTER TABLE "table3" ADD CONSTRAINT "testfk" FOREIGN KEY ("COL_FK") REFERENCES "table2" ("COL_PK");
            """, getTableConstraintTestDatabaseCreationSql());
    }

    /**
     * Tests the proper escaping of character sequences where Oracle requires it.
     */
    public void testCharacterEscaping() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            DROP TABLE "escapedcharacters" CASCADE CONSTRAINTS;
            CREATE TABLE "escapedcharacters"
            (
                "COL_PK"   INTEGER,
                "COL_TEXT" VARCHAR2(128) DEFAULT '''',
                PRIMARY KEY ("COL_PK")
            );
            """, getCharEscapingTestDatabaseCreationSql());
    }
}
