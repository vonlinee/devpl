package org.apache.ddlutils.platform;


import org.apache.ddlutils.TestPlatformBase;

/**
 * Tests the Firebird platform.
 *
 * @version $Revision: 231110 $
 */
public class TestFirebirdPlatform extends TestPlatformBase {

    protected String getDatabaseName() {
        return DBTypeEnum.FIREBIRD.getName();
    }

    /**
     * Tests the column types.
     */
    public void testColumnTypes() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            DROP TABLE "coltype";
            CREATE TABLE "coltype"
            (
                "COL_ARRAY"           BLOB ,
                "COL_BIGINT"          BIGINT,
                "COL_BINARY"          BLOB,
                "COL_BIT"             SMALLINT,
                "COL_BLOB"            BLOB ,
                "COL_BOOLEAN"         SMALLINT,
                "COL_CHAR"            CHAR(15),
                "COL_CLOB"            BLOB SUB_TYPE TEXT,
                "COL_DATALINK"        BLOB,
                "COL_DATE"            DATE,
                "COL_DECIMAL"         DECIMAL(15,3),
                "COL_DECIMAL_NOSCALE" DECIMAL(15,0),
                "COL_DISTINCT"        BLOB,
                "COL_DOUBLE"          DOUBLE PRECISION,
                "COL_FLOAT"           DOUBLE PRECISION,
                "COL_INTEGER"         INTEGER,
                "COL_JAVA_OBJECT"     BLOB,
                "COL_LONGVARBINARY"   BLOB,
                "COL_LONGVARCHAR"     BLOB SUB_TYPE TEXT,
                "COL_NULL"            BLOB,
                "COL_NUMERIC"         NUMERIC(15,0),
                "COL_OTHER"           BLOB,
                "COL_REAL"            FLOAT,
                "COL_REF"             BLOB,
                "COL_SMALLINT"        SMALLINT,
                "COL_STRUCT"          BLOB,
                "COL_TIME"            TIME,
                "COL_TIMESTAMP"       TIMESTAMP,
                "COL_TINYINT"         SMALLINT,
                "COL_VARBINARY"       BLOB(15),
                "COL_VARCHAR"         VARCHAR(15)
            );
            """, getColumnTestDatabaseCreationSql());
    }

    /**
     * Tests the column constraints.
     */
    public void testColumnConstraints() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            DROP TRIGGER "trg_constraints_OL_PK_AUTO_INCR";
            DROP GENERATOR "gen_constraints_OL_PK_AUTO_INCR";
            DROP TRIGGER "trg_constraints_COL_AUTO_INCR";
            DROP GENERATOR "gen_constraints_COL_AUTO_INCR";
            DROP TABLE "constraints";
            CREATE TABLE "constraints"
            (
                "COL_PK"               VARCHAR(32),
                "COL_PK_AUTO_INCR"     INTEGER,
                "COL_NOT_NULL"         BLOB(100) NOT NULL,
                "COL_NOT_NULL_DEFAULT" DOUBLE PRECISION DEFAULT -2.0 NOT NULL,
                "COL_DEFAULT"          CHAR(4) DEFAULT 'test',
                "COL_AUTO_INCR"        BIGINT,
                PRIMARY KEY ("COL_PK", "COL_PK_AUTO_INCR")
            );
            CREATE GENERATOR "gen_constraints_OL_PK_AUTO_INCR";
            CREATE TRIGGER "trg_constraints_OL_PK_AUTO_INCR" FOR "constraints"
            ACTIVE BEFORE INSERT POSITION 0 AS
            BEGIN IF (NEW."COL_PK_AUTO_INCR" IS NULL) THEN NEW."COL_PK_AUTO_INCR" = GEN_ID("gen_constraints_OL_PK_AUTO_INCR", 1); END;
            CREATE GENERATOR "gen_constraints_COL_AUTO_INCR";
            CREATE TRIGGER "trg_constraints_COL_AUTO_INCR" FOR "constraints"
            ACTIVE BEFORE INSERT POSITION 0 AS
            BEGIN IF (NEW."COL_AUTO_INCR" IS NULL) THEN NEW."COL_AUTO_INCR" = GEN_ID("gen_constraints_COL_AUTO_INCR", 1); END;
            """, getConstraintTestDatabaseCreationSql());
    }

    /**
     * Tests the table constraints.
     */
    public void testTableConstraints() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            ALTER TABLE "table3" DROP CONSTRAINT "testfk";
            ALTER TABLE "table2" DROP CONSTRAINT "table2_FK_COL_F_COL_FK_2_table1";
            DROP TABLE "table3";
            DROP TABLE "table2";
            DROP TABLE "table1";
            CREATE TABLE "table1"
            (
                "COL_PK_1"    VARCHAR(32) NOT NULL,
                "COL_PK_2"    INTEGER,
                "COL_INDEX_1" BLOB(100) NOT NULL,
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
     * Tests the proper escaping of character sequences where Firebird requires it.
     */
    public void testCharacterEscaping() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            DROP TABLE "escapedcharacters";
            CREATE TABLE "escapedcharacters"
            (
                "COL_PK"   INTEGER,
                "COL_TEXT" VARCHAR(128) DEFAULT '''',
                PRIMARY KEY ("COL_PK")
            );
            """, getCharEscapingTestDatabaseCreationSql());
    }
}
