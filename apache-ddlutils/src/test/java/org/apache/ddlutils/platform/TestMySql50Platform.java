package org.apache.ddlutils.platform;

import org.apache.ddlutils.TestPlatformBase;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.mysql.MySql50Platform;

/**
 * Tests the MySQL platform.
 * @version $Revision: 231110 $
 */
public class TestMySql50Platform extends TestPlatformBase {

    @Override
    protected String getDatabaseName() {
        return MySql50Platform.DATABASENAME;
    }

    /**
     * Tests the column types.
     */
    public void testColumnTypes() throws Exception {
        assertEqualsIgnoringWhitespaces(
            """
                DROP TABLE IF EXISTS `coltype`;
                CREATE TABLE `coltype`
                (
                    `COL_ARRAY`           LONGBLOB,
                    `COL_BIGINT`          BIGINT,
                    `COL_BINARY`          BINARY(254) NULL,
                    `COL_BIT`             TINYINT(1),
                    `COL_BLOB`            LONGBLOB NULL,
                    `COL_BOOLEAN`         TINYINT(1),
                    `COL_CHAR`            CHAR(15) NULL,
                    `COL_CLOB`            LONGTEXT NULL,
                    `COL_DATALINK`        MEDIUMBLOB,
                    `COL_DATE`            DATE,
                    `COL_DECIMAL`         DECIMAL(15,3),
                    `COL_DECIMAL_NOSCALE` DECIMAL(15,0),
                    `COL_DISTINCT`        LONGBLOB,
                    `COL_DOUBLE`          DOUBLE,
                    `COL_FLOAT`           DOUBLE,
                    `COL_INTEGER`         INTEGER,
                    `COL_JAVA_OBJECT`     LONGBLOB,
                    `COL_LONGVARBINARY`   MEDIUMBLOB NULL,
                    `COL_LONGVARCHAR`     MEDIUMTEXT NULL,
                    `COL_NULL`            MEDIUMBLOB,
                    `COL_NUMERIC`         DECIMAL(15,0),
                    `COL_OTHER`           LONGBLOB,
                    `COL_REAL`            FLOAT,
                    `COL_REF`             MEDIUMBLOB,
                    `COL_SMALLINT`        SMALLINT,
                    `COL_STRUCT`          LONGBLOB,
                    `COL_TIME`            TIME,
                    `COL_TIMESTAMP`       DATETIME,
                    `COL_TINYINT`         SMALLINT,
                    `COL_VARBINARY`       VARBINARY(15) NULL,
                    `COL_VARCHAR`         VARCHAR(15) NULL
                );
                """,
            getColumnTestDatabaseCreationSql());
    }

    /**
     * Tests the column constraints.
     */
    public void testColumnConstraints() throws Exception {
        // MySql-specfic schema
        final String schema =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='columnconstraintstest'>
                  <table name='constraints'>
                    <column name='COL_PK' type='VARCHAR' size='32' primaryKey='true' description='主键'/>
                    <column name='COL_PK_AUTO_INCR' type='INTEGER' primaryKey='true'/>
                    <column name='COL_NOT_NULL' type='BINARY' size='100' required='true'/>
                    <column name='COL_NOT_NULL_DEFAULT' type='DOUBLE' required='true' default='-2.0'/>
                    <column name='COL_DEFAULT' type='CHAR' size='4' default='test'/>
                    <column name='COL_AUTO_INCR' type='BIGINT'/>
                  </table>
                </database>
                """;

        String ddl = getDatabaseCreationSql(schema);

        assertEqualsIgnoringWhitespaces(
            """
                DROP TABLE IF EXISTS `constraints`;
                CREATE TABLE `constraints`
                (
                    `COL_PK`               VARCHAR(32) NULL,
                    `COL_PK_AUTO_INCR`     INTEGER,
                    `COL_NOT_NULL`         BINARY(100) NOT NULL,
                    `COL_NOT_NULL_DEFAULT` DOUBLE DEFAULT -2.0 NOT NULL,
                    `COL_DEFAULT`          CHAR(4) DEFAULT 'test' NULL,
                    `COL_AUTO_INCR`        BIGINT,
                    PRIMARY KEY (`COL_PK`, `COL_PK_AUTO_INCR`)
                );
                """, ddl);
    }

    /**
     * Tests the table constraints.
     */
    public void testTableConstraints() throws Exception {
        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE `table3` DROP FOREIGN KEY `testfk`;
                ALTER TABLE `table2` DROP FOREIGN KEY `table2_FK_COL_FK_1_COL_FK_2_table1`;
                DROP TABLE IF EXISTS `table3`;
                DROP TABLE IF EXISTS `table2`;
                DROP TABLE IF EXISTS `table1`;
                CREATE TABLE `table1`
                (
                    `COL_PK_1`    VARCHAR(32) NOT NULL,
                    `COL_PK_2`    INTEGER,
                    `COL_INDEX_1` BINARY(100) NOT NULL,
                    `COL_INDEX_2` DOUBLE NOT NULL,
                    `COL_INDEX_3` CHAR(4) NULL,
                    PRIMARY KEY (`COL_PK_1`, `COL_PK_2`)
                );
                CREATE INDEX `testindex1` ON `table1` (`COL_INDEX_2`);
                CREATE UNIQUE INDEX `testindex2` ON `table1` (`COL_INDEX_3`, `COL_INDEX_1`);
                CREATE TABLE `table2`
                (
                    `COL_PK`   INTEGER,
                    `COL_FK_1` INTEGER,
                    `COL_FK_2` VARCHAR(32) NOT NULL,
                    PRIMARY KEY (`COL_PK`)
                );
                CREATE TABLE `table3`
                (
                    `COL_PK` VARCHAR(16) NULL,
                    `COL_FK` INTEGER NOT NULL,
                    PRIMARY KEY (`COL_PK`)
                );
                ALTER TABLE `table2` ADD CONSTRAINT `table2_FK_COL_FK_1_COL_FK_2_table1` FOREIGN KEY (`COL_FK_1`, `COL_FK_2`) REFERENCES `table1` (`COL_PK_2`, `COL_PK_1`) ON DELETE NO ACTION ON UPDATE NO ACTION;
                ALTER TABLE `table3` ADD CONSTRAINT `testfk` FOREIGN KEY (`COL_FK`) REFERENCES `table2` (`COL_PK`) ON DELETE NO ACTION ON UPDATE NO ACTION;
                """,
            getTableConstraintTestDatabaseCreationSql());
    }

    /**
     * Tests the usage of creation parameters.
     */
    public void testCreationParameters1() throws Exception {
        // MySql-specfic schema
        final String schema =
            "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='columnconstraintstest'>\n" +
                "  <table name='constraints'>\n" +
                "    <column name='COL_PK' type='VARCHAR' size='32' primaryKey='true'/>\n" +
                "    <column name='COL_PK_AUTO_INCR' type='INTEGER' primaryKey='true'/>\n" +
                "    <column name='COL_NOT_NULL' type='BINARY' size='100' required='true'/>\n" +
                "    <column name='COL_NOT_NULL_DEFAULT' type='DOUBLE' required='true' default='-2.0'/>\n" +
                "    <column name='COL_DEFAULT' type='CHAR' size='4' default='test'/>\n" +
                "    <column name='COL_AUTO_INCR' type='BIGINT'/>\n" +
                "  </table>\n" +
                "</database>";

        Database testDb = parseDatabaseFromString(schema);
        SqlBuildContext params = new SqlBuildContext();

        params.addParameter(testDb.getTable(0),
            "ROW_FORMAT",
            "COMPRESSED");
        params.addParameter(null,
            "ENGINE",
            "INNODB");

        getPlatform().setSqlCommentsOn(false);

        SqlBuilder sqlBuilder = getPlatform().getSqlBuilder();

        sqlBuilder.createTables(testDb, params, true);

        System.out.println(getBuilderOutput());

        assertEqualsIgnoringWhitespaces(
            """
                DROP TABLE IF EXISTS `constraints`;
                CREATE TABLE `constraints`
                (
                    `COL_PK`               VARCHAR(32) NULL,
                    `COL_PK_AUTO_INCR`     INTEGER,
                    `COL_NOT_NULL`         BINARY(100) NOT NULL,
                    `COL_NOT_NULL_DEFAULT` DOUBLE DEFAULT -2.0 NOT NULL,
                    `COL_DEFAULT`          CHAR(4) DEFAULT 'test' NULL,
                    `COL_AUTO_INCR`        BIGINT,
                    PRIMARY KEY (`COL_PK`, `COL_PK_AUTO_INCR`)
                ) ENGINE=INNODB ROW_FORMAT=COMPRESSED;
                """,
            getBuilderOutput());
    }

    /**
     * Tests the proper escaping of character sequences where MySQL requires it.
     */
    public void testCharacterEscaping() throws Exception {
        // MySql-specific schema
        final String schema =
            "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='escapetest'>\n" +
                "  <table name='escapedcharacters'>\n" +
                "    <column name='COL_PK' type='INTEGER' primaryKey='true'/>\n" +
                "    <column name='COL_TEXT' type='VARCHAR' size='128' default='_ &#39; \" &#10; &#13; &#09; \\ &#37;'/>\n" +
                "  </table>\n" +
                "</database>";

        assertEqualsIgnoringWhitespaces(
            """
                DROP TABLE IF EXISTS `escapedcharacters`;
                CREATE TABLE `escapedcharacters`
                (
                    `COL_PK`   INTEGER,
                    `COL_TEXT` VARCHAR(128) DEFAULT '\\_ \\' \\" \\n \\r \\t \\\\ \\%' NULL,
                    PRIMARY KEY (`COL_PK`)
                );
                """,
            getDatabaseCreationSql(schema));
    }
}
