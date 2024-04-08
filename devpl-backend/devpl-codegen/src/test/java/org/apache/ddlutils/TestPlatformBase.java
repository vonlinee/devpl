package org.apache.ddlutils;


import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.SqlBuilder;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Base class for builder tests.
 *
 *
 */
public abstract class TestPlatformBase extends TestBase {
    /**
     * The tested platform.
     */
    private Platform _platform;
    /**
     * The writer that the builder of the platform writes to.
     */
    private StringWriter _writer;


    protected void setUp() throws Exception {
        _writer = new StringWriter();
        _platform = PlatformFactory.createNewPlatformInstance(getDatabaseName());
        _platform.getSqlBuilder().setWriter(_writer);
        if (_platform.getPlatformInfo().isDelimitedIdentifiersSupported()) {
            _platform.setDelimitedIdentifierModeOn(true);
        }
    }


    protected void tearDown() throws Exception {
        _platform = null;
        _writer = null;
    }

    /**
     * Returns the tested platform.
     *
     * @return The platform
     */
    protected Platform getPlatform() {
        return _platform;
    }

    /**
     * Returns the info object of the tested platform.
     *
     * @return The platform info object
     */
    protected PlatformInfo getPlatformInfo() {
        return getPlatform().getPlatformInfo();
    }

    /**
     * Returns the SQL builder of the tested platform.
     *
     * @return The builder object
     */
    protected SqlBuilder getSqlBuilder() {
        return getPlatform().getSqlBuilder();
    }

    /**
     * Returns the builder output so far.
     *
     * @return The output
     */
    protected String getBuilderOutput() {
        return _writer.toString();
    }

    /**
     * Returns the name of the tested database.
     *
     * @return The database name
     */
    protected abstract String getDatabaseName();

    /**
     * Returns the database creation SQL for the given database schema.
     *
     * @param schema The database schema XML
     * @return The SQL
     */
    protected String getDatabaseCreationSql(String schema) throws IOException {
        Database testDb = parseDatabaseFromString(schema);
        // we're turning the comment creation off to make testing easier
        getPlatform().setSqlCommentsOn(false);
        getPlatform().getSqlBuilder().createTables(testDb);
        return getBuilderOutput();
    }

    /**
     * Returns the SQL to create the test database for the column tests.
     *
     * @return The SQL
     */
    protected String getColumnTestDatabaseCreationSql() throws IOException {
        final String schema = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='datatypetest'>
              <table name='coltype'>
                <column name='COL_ARRAY'           type='ARRAY'/>
                <column name='COL_BIGINT'          type='BIGINT'/>
                <column name='COL_BINARY'          type='BINARY'/>
                <column name='COL_BIT'             type='BIT'/>
                <column name='COL_BLOB'            type='BLOB'/>
                <column name='COL_BOOLEAN'         type='BOOLEAN'/>
                <column name='COL_CHAR'            type='CHAR' size='15'/>
                <column name='COL_CLOB'            type='CLOB'/>
                <column name='COL_DATALINK'        type='DATALINK'/>
                <column name='COL_DATE'            type='DATE'/>
                <column name='COL_DECIMAL'         type='DECIMAL' size='15,3'/>
                <column name='COL_DECIMAL_NOSCALE' type='DECIMAL' size='15'/>
                <column name='COL_DISTINCT'        type='DISTINCT'/>
                <column name='COL_DOUBLE'          type='DOUBLE'/>
                <column name='COL_FLOAT'           type='FLOAT'/>
                <column name='COL_INTEGER'         type='INTEGER'/>
                <column name='COL_JAVA_OBJECT'     type='JAVA_OBJECT'/>
                <column name='COL_LONGVARBINARY'   type='LONGVARBINARY'/>
                <column name='COL_LONGVARCHAR'     type='LONGVARCHAR'/>
                <column name='COL_NULL'            type='NULL'/>
                <column name='COL_NUMERIC'         type='NUMERIC' size='15' />
                <column name='COL_OTHER'           type='OTHER'/>
                <column name='COL_REAL'            type='REAL'/>
                <column name='COL_REF'             type='REF'/>
                <column name='COL_SMALLINT'        type='SMALLINT' size='5'/>
                <column name='COL_STRUCT'          type='STRUCT'/>
                <column name='COL_TIME'            type='TIME'/>
                <column name='COL_TIMESTAMP'       type='TIMESTAMP'/>
                <column name='COL_TINYINT'         type='TINYINT'/>
                <column name='COL_VARBINARY'       type='VARBINARY' size='15'/>
                <column name='COL_VARCHAR'         type='VARCHAR' size='15'/>
              </table>
            </database>""";

        return getDatabaseCreationSql(schema);
    }

    /**
     * Returns the SQL to create the test database for the constraint tests.
     *
     * @return The SQL
     */
    protected String getConstraintTestDatabaseCreationSql() throws IOException {
        final String schema = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='columnconstraintstest'>
              <table name='constraints'>
                <column name='COL_PK' type='VARCHAR' size='32' primaryKey='true'/>
                <column name='COL_PK_AUTO_INCR' type='INTEGER' primaryKey='true' autoIncrement='true'/>
                <column name='COL_NOT_NULL' type='BINARY' size='100' required='true'/>
                <column name='COL_NOT_NULL_DEFAULT' type='DOUBLE' required='true' default='-2.0'/>
                <column name='COL_DEFAULT' type='CHAR' size='4' default='test'/>
                <column name='COL_AUTO_INCR' type='BIGINT' autoIncrement='true'/>
              </table>
            </database>""";

        return getDatabaseCreationSql(schema);
    }

    /**
     * Returns the SQL to create the test database for the table-level constraint tests.
     *
     * @return The SQL
     */
    protected String getTableConstraintTestDatabaseCreationSql() throws IOException {
        final String schema = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='tableconstraintstest'>
              <table name='table1'>
                <column name='COL_PK_1' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                <column name='COL_PK_2' type='INTEGER' primaryKey='true'/>
                <column name='COL_INDEX_1' type='BINARY' size='100' required='true'/>
                <column name='COL_INDEX_2' type='DOUBLE' required='true'/>
                <column name='COL_INDEX_3' type='CHAR' size='4'/>
                <index name='testindex1'>
                  <index-column name='COL_INDEX_2'/>
                </index>
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

        return getDatabaseCreationSql(schema);
    }

    /**
     * Returns the SQL to create the test database for testing character escaping.
     *
     * @return The SQL
     */
    protected String getCharEscapingTestDatabaseCreationSql() throws IOException {
        final String schema = """
            <?xml version='1.0' encoding='ISO-8859-1'?>
            <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='escapetest'>
              <table name='escapedcharacters'>
                <column name='COL_PK' type='INTEGER' primaryKey='true'/>
                <column name='COL_TEXT' type='VARCHAR' size='128' default='&#39;'/>
              </table>
            </database>""";

        return getDatabaseCreationSql(schema);
    }
}
