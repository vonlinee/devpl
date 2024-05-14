package org.apache.ddlutils.platform;


import org.apache.ddlutils.TestPlatformBase;

/**
 * Tests the Sybase platform.
 */
public class TestSybasePlatform extends TestPlatformBase {

    protected String getDatabaseName() {
        return DBTypeEnum.SYBASE.getName();
    }

    /**
     * Tests the column types.
     */
    public void testColumnTypes() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            SET quoted_identifier on;
            SET quoted_identifier on;
            IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'coltype')
            BEGIN
                DROP TABLE "coltype"
            END;
            SET quoted_identifier on;
            CREATE TABLE "coltype"
            (
                "COL_ARRAY"           IMAGE NULL,
                "COL_BIGINT"          DECIMAL(19,0) NULL,
                "COL_BINARY"          BINARY(254) NULL,
                "COL_BIT"             SMALLINT NULL,
                "COL_BLOB"            IMAGE NULL,
                "COL_BOOLEAN"         SMALLINT NULL,
                "COL_CHAR"            CHAR(15) NULL,
                "COL_CLOB"            TEXT NULL,
                "COL_DATALINK"        IMAGE NULL,
                "COL_DATE"            DATETIME NULL,
                "COL_DECIMAL"         DECIMAL(15,3) NULL,
                "COL_DECIMAL_NOSCALE" DECIMAL(15,0) NULL,
                "COL_DISTINCT"        IMAGE NULL,
                "COL_DOUBLE"          DOUBLE PRECISION NULL,
                "COL_FLOAT"           DOUBLE PRECISION NULL,
                "COL_INTEGER"         INT NULL,
                "COL_JAVA_OBJECT"     IMAGE NULL,
                "COL_LONGVARBINARY"   IMAGE NULL,
                "COL_LONGVARCHAR"     TEXT NULL,
                "COL_NULL"            IMAGE NULL,
                "COL_NUMERIC"         NUMERIC(15,0) NULL,
                "COL_OTHER"           IMAGE NULL,
                "COL_REAL"            REAL NULL,
                "COL_REF"             IMAGE NULL,
                "COL_SMALLINT"        SMALLINT NULL,
                "COL_STRUCT"          IMAGE NULL,
                "COL_TIME"            DATETIME NULL,
                "COL_TIMESTAMP"       DATETIME NULL,
                "COL_TINYINT"         SMALLINT NULL,
                "COL_VARBINARY"       VARBINARY(15) NULL,
                "COL_VARCHAR"         VARCHAR(15) NULL
            );
            """, getColumnTestDatabaseCreationSql());
    }

    /**
     * Tests the column constraints.
     */
    public void testColumnConstraints() throws Exception {
        // this is not valid sql as a table can have only one identity column at most
        assertEqualsIgnoringWhitespaces("""
            SET quoted_identifier on;
            SET quoted_identifier on;
            IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'constraints')
            BEGIN
                DROP TABLE "constraints"
            END;
            SET quoted_identifier on;
            CREATE TABLE "constraints"
            (
                "COL_PK"               VARCHAR(32) NULL,
                "COL_PK_AUTO_INCR"     INT IDENTITY,
                "COL_NOT_NULL"         BINARY(100) NOT NULL,
                "COL_NOT_NULL_DEFAULT" DOUBLE PRECISION DEFAULT -2.0 NOT NULL,
                "COL_DEFAULT"          CHAR(4) DEFAULT 'test' NULL,
                "COL_AUTO_INCR"        DECIMAL(19,0) IDENTITY,
                PRIMARY KEY ("COL_PK", "COL_PK_AUTO_INCR")
            );
            """, getConstraintTestDatabaseCreationSql());
    }

    /**
     * Tests the table constraints.
     */
    public void testTableConstraints() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            SET quoted_identifier on;
            IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'RI' AND name = 'testfk')
                ALTER TABLE "table3" DROP CONSTRAINT "testfk";
            SET quoted_identifier on;
            IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'RI' AND name = 'table2_FK_COL_L_FK_2_table1')
                ALTER TABLE "table2" DROP CONSTRAINT "table2_FK_COL_L_FK_2_table1";
            SET quoted_identifier on;
            SET quoted_identifier on;
            IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'table3')
            BEGIN
                DROP TABLE "table3"
            END;
            SET quoted_identifier on;
            IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'table2')
            BEGIN
                DROP TABLE "table2"
            END;
            SET quoted_identifier on;
            IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'table1')
            BEGIN
                DROP TABLE "table1"
            END;
            SET quoted_identifier on;
            CREATE TABLE "table1"
            (
                "COL_PK_1"    VARCHAR(32) NOT NULL,
                "COL_PK_2"    INT NULL,
                "COL_INDEX_1" BINARY(100) NOT NULL,
                "COL_INDEX_2" DOUBLE PRECISION NOT NULL,
                "COL_INDEX_3" CHAR(4) NULL,
                PRIMARY KEY ("COL_PK_1", "COL_PK_2")
            );
            CREATE INDEX "testindex1" ON "table1" ("COL_INDEX_2");
            CREATE UNIQUE INDEX "testindex2" ON "table1" ("COL_INDEX_3", "COL_INDEX_1");
            SET quoted_identifier on;
            CREATE TABLE "table2"
            (
                "COL_PK"   INT NULL,
                "COL_FK_1" INT NULL,
                "COL_FK_2" VARCHAR(32) NOT NULL,
                PRIMARY KEY ("COL_PK")
            );
            SET quoted_identifier on;
            CREATE TABLE "table3"
            (
                "COL_PK" VARCHAR(16) NULL,
                "COL_FK" INT NOT NULL,
                PRIMARY KEY ("COL_PK")
            );
            ALTER TABLE "table2" ADD CONSTRAINT "table2_FK_COL_L_FK_2_table1" FOREIGN KEY ("COL_FK_1", "COL_FK_2") REFERENCES "table1" ("COL_PK_2", "COL_PK_1");
            ALTER TABLE "table3" ADD CONSTRAINT "testfk" FOREIGN KEY ("COL_FK") REFERENCES "table2" ("COL_PK");
            """, getTableConstraintTestDatabaseCreationSql());
    }

    /**
     * Tests the proper escaping of character sequences where Cloudscape requires it.
     */
    public void testCharacterEscaping() throws Exception {
        assertEqualsIgnoringWhitespaces("""
            SET quoted_identifier on;
            SET quoted_identifier on;
            IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'escapedcharacters')
            BEGIN
                DROP TABLE "escapedcharacters"
            END;
            SET quoted_identifier on;
            CREATE TABLE "escapedcharacters"
            (
                "COL_PK"   INT NULL,
                "COL_TEXT" VARCHAR(128) DEFAULT '''' NULL,
                PRIMARY KEY ("COL_PK")
            );
            """, getCharEscapingTestDatabaseCreationSql());
    }
}
