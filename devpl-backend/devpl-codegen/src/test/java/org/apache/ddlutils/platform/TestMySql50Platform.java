package org.apache.ddlutils.platform;

import org.apache.ddlutils.TestAgainstLiveDatabaseBase;
import org.apache.ddlutils.jdbc.PooledDataSourceWrapper;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.ModelHelper;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TableClass;
import org.apache.ddlutils.platform.mysql.MySql5xModelReader;
import org.apache.ddlutils.platform.mysql.MySql5xPlatform;
import org.apache.ddlutils.util.PojoMap;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Tests the MySQL platform.
 */
public class TestMySql50Platform extends TestAgainstLiveDatabaseBase {

    @Override
    protected String getDatabaseName() {
        return DBTypeEnum.MYSQL5.getName();
    }

    @Test
    public void testInsertSqlWithRealTable() {
        MySql5xPlatform platform = new MySql5xPlatform();

        MySql5xModelReader reader = new MySql5xModelReader(platform);

        try (PooledDataSourceWrapper dataSource = new PooledDataSourceWrapper()) {
            dataSource.setUrl("jdbc:mysql://localhost:3306/devpl?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true");
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUsername("root");
            dataSource.setPassword("123456");

            platform.setDataSource(dataSource);

            try (Connection connection = dataSource.getConnection()) {
                Database database = reader.getDatabase(connection, "devpl");

                SqlBuilder sqlBuilder = platform.getSqlBuilder();

                System.out.println("sql builder => " + sqlBuilder.getClass());
                StringWriter stringWriter = new StringWriter();

                sqlBuilder.setWriter(stringWriter);

                Table table = database.getTable(0);

                getLog().info("table => {}", table.getName());

                PojoMap parameters = new PojoMap();
                sqlBuilder.createTable(database, table, parameters);

                TableClass tableClass = TableClass.newInstance(table);

                Map<String, Object> columnValueMap = ModelHelper.emptyColumnValueMap(tableClass);
                columnValueMap.put("id", "ssdd");
                String insertSql = sqlBuilder.getInsertSql(table, columnValueMap, false);

                System.out.println(insertSql);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUpdateSqlWithRealTable() {
        MySql5xPlatform platform = new MySql5xPlatform();

        System.out.println(platform.getModelReader());

        JdbcModelReader reader = platform.getModelReader();
        try (PooledDataSourceWrapper dataSource = new PooledDataSourceWrapper()) {
            dataSource.setUrl("jdbc:mysql://localhost:3306/devpl?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true");
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUsername("root");
            dataSource.setPassword("123456");

            platform.setDataSource(dataSource);

            try (Connection connection = dataSource.getConnection()) {
                Database database = reader.getDatabase(connection, "devpl");

                SqlBuilder sqlBuilder = platform.getSqlBuilder();

                System.out.println("sql builder => " + sqlBuilder.getClass());
                StringWriter stringWriter = new StringWriter();

                sqlBuilder.setWriter(stringWriter);

                Table table = database.getTable(0);

                getLog().info("table => {}", table.getName());

                PojoMap parameters = new PojoMap();
                sqlBuilder.createTable(database, table, parameters);

                TableClass tableClass = TableClass.newInstance(table);

                Map<String, Object> columnValueMap = ModelHelper.emptyColumnValueMap(tableClass);
                columnValueMap.put("id", "ssdd");
                String insertSql = sqlBuilder.getUpdateSql(table, columnValueMap, false);

                System.out.println(insertSql);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests the column types.
     */
    @Test
    public void testColumnTypes() throws Exception {
        assertEqualsIgnoringWhitespaces("""
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
            """, getColumnTestDatabaseCreationSql());
    }

    /**
     * Tests the column constraints.
     */
    @Test
    public void testColumnConstraints() throws Exception {
        // MySql-specific schema
        final String schema = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='columnconstraintstest'>
              <table name='constraints'>
                <column name='COL_PK' type='VARCHAR' size='32' primaryKey='true'/>
                <column name='COL_PK_AUTO_INCR' type='INTEGER' primaryKey='true'/>
                <column name='COL_NOT_NULL' type='BINARY' size='100' required='true'/>
                <column name='COL_NOT_NULL_DEFAULT' type='DOUBLE' required='true' default='-2.0'/>
                <column name='COL_DEFAULT' type='CHAR' size='4' default='test'/>
                <column name='COL_AUTO_INCR' type='BIGINT'/>
              </table>
            </database>""";

        assertEqualsIgnoringWhitespaces("""
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
            """, getDatabaseCreationSql(schema));
    }

    /**
     * Tests the table constraints.
     */
    @Test
    public void testTableConstraints() throws Exception {
        assertEqualsIgnoringWhitespaces("""
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
            """, getTableConstraintTestDatabaseCreationSql());
    }

    /**
     * Tests the usage of creation parameters.
     */
    @Test
    public void testCreationParameters1() throws Exception {
        // MySql-specfic schema
        final String schema = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='columnconstraintstest'>
              <table name='constraints'>
                <column name='COL_PK' type='VARCHAR' size='32' primaryKey='true'/>
                <column name='COL_PK_AUTO_INCR' type='INTEGER' primaryKey='true'/>
                <column name='COL_NOT_NULL' type='BINARY' size='100' required='true'/>
                <column name='COL_NOT_NULL_DEFAULT' type='DOUBLE' required='true' default='-2.0'/>
                <column name='COL_DEFAULT' type='CHAR' size='4' default='test'/>
                <column name='COL_AUTO_INCR' type='BIGINT'/>
              </table>
            </database>""";

        Database testDb = parseDatabaseFromString(schema);
        CreationParameters params = new CreationParameters();

        params.addParameter(testDb.getTable(0), "ROW_FORMAT", "COMPRESSED");
        params.addParameter(null, "ENGINE", "INNODB");

        getPlatform().setSqlCommentsOn(false);
        getPlatform().getSqlBuilder().createTables(testDb, params, true);

        assertEqualsIgnoringWhitespaces("""
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
            """, getBuilderOutput());
    }

    /**
     * Tests the proper escaping of character sequences where MySQL requires it.
     */
    @Test
    public void testCharacterEscaping() throws Exception {
        // MySql-specific schema
        final String schema = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='escapetest'>
              <table name='escapedcharacters'>
                <column name='COL_PK' type='INTEGER' primaryKey='true'/>
                <column name='COL_TEXT' type='VARCHAR' size='128' default='_ &#39; " &#10; &#13; &#09; \\ &#37;'/>
              </table>
            </database>""";

        assertEqualsIgnoringWhitespaces("""
            DROP TABLE IF EXISTS `escapedcharacters`;
            CREATE TABLE `escapedcharacters`
            (
                `COL_PK`   INTEGER,
                `COL_TEXT` VARCHAR(128) DEFAULT '\\_ \\' \\" \\n \\r \\t \\\\ \\%' NULL,
                PRIMARY KEY (`COL_PK`)
            );
            """, getDatabaseCreationSql(schema));
    }
}
