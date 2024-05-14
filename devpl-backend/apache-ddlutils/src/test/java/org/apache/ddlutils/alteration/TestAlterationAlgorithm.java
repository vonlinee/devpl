package org.apache.ddlutils.alteration;


import org.apache.ddlutils.Platform;
import org.apache.ddlutils.TestBase;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.TestPlatform;

import java.io.IOException;

/**
 * Tests the generation of the alteration statements.
 */
public class TestAlterationAlgorithm extends TestBase {
    /**
     * The tested platform.
     */
    private Platform _platform;

    protected void setUp() throws Exception {
        _platform = new TestPlatform();
        _platform.setSqlCommentsOn(false);
        _platform.setDelimitedIdentifierModeOn(true);
    }

    protected void tearDown() throws Exception {
        _platform = null;
    }

    /**
     * Returns the SQL for altering the first into the second database.
     *
     * @param currentSchema The current schema XML
     * @param desiredSchema The desired schema XML
     * @return The sql
     */
    protected String getAlterModelSQL(String currentSchema, String desiredSchema) throws IOException {
        Database currentModel = parseDatabaseFromString(currentSchema);
        Database desiredModel = parseDatabaseFromString(desiredSchema);

        return _platform.getAlterModelSql(currentModel, desiredModel);
    }

    /**
     * Test where no change is made to the model.
     */
    public void testNoChange() throws IOException {
        final String modelXml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TABLEA'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='DOUBLE' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER' required='true'/>
                    <foreign-key name='TESTFK' foreignTable='TABLEA'>
                      <reference local='COLFK' foreign='COLPK'/>
                    </foreign-key>
                    <index name='TESTINDEX'>
                      <index-column name='COLFK'/>
                    </index>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            "",
            getAlterModelSQL(modelXml, modelXml));
    }

    /**
     * Tests the addition of a table.
     */
    public void testAddTable() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TABLEB"
                (
                    "COLPK" INTEGER NOT NULL,
                    PRIMARY KEY ("COLPK")
                );
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of a table that has an index.
     */
    public void testAddTableWithIndex() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COL' type='VARCHAR' size='64'/>
                    <index name='TESTINDEX'>
                      <index-column name='COL'/>
                    </index>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TABLEB"
                (
                    "COLPK" INTEGER NOT NULL,
                    "COL" VARCHAR(64),
                    PRIMARY KEY ("COLPK")
                );
                CREATE INDEX "TESTINDEX" ON "TABLEB" ("COL");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of a table that has a unique index.
     */
    public void testAddTableWithUniqueIndex() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COL' type='VARCHAR' size='64'/>
                    <unique name='TESTINDEX'>
                      <unique-column name='COL'/>
                    </unique>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TABLEB"
                (
                    "COLPK" INTEGER NOT NULL,
                    "COL" VARCHAR(64),
                    PRIMARY KEY ("COLPK")
                );
                CREATE UNIQUE INDEX "TESTINDEX" ON "TABLEB" ("COL");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of a table that has a foreign key to an existing one.
     */
    public void testAddTableWithForeignKey() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TABLEB"
                (
                    "COLPK" INTEGER NOT NULL,
                    "COLFK" INTEGER,
                    PRIMARY KEY ("COLPK")
                );
                ALTER TABLE "TABLEB" ADD CONSTRAINT "TESTFK" FOREIGN KEY ("COLFK") REFERENCES "TableA" ("ColPK");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of two tables that have foreign key to each other.
     */
    public void testAddTablesWithForeignKeys() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColFK' type='DOUBLE'/>
                    <foreign-key name='TESTFK' foreignTable='TABLEB'>
                      <reference local='ColFK' foreign='COLPK'/>
                    </foreign-key>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='DOUBLE' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    "ColFK" DOUBLE,
                    PRIMARY KEY ("ColPK")
                );
                CREATE TABLE "TABLEB"
                (
                    "COLPK" DOUBLE NOT NULL,
                    "COLFK" INTEGER,
                    PRIMARY KEY ("COLPK")
                );
                ALTER TABLE "TableA" ADD CONSTRAINT "TESTFK" FOREIGN KEY ("ColFK") REFERENCES "TABLEB" ("COLPK");
                ALTER TABLE "TABLEB" ADD CONSTRAINT "TESTFK" FOREIGN KEY ("COLFK") REFERENCES "TableA" ("ColPK");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of a table.
     */
    public void testRemoveTable() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            "DROP TABLE \"TableA\";\n",
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of a table with an index.
     */
    public void testRemoveTableWithIndex() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='VARCHAR' size='64'/>
                    <index name='TestIndex'>
                      <index-column name='Col'/>
                    </index>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            "DROP TABLE \"TableA\";\n",
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of a table with a foreign key to an existing table.
     */
    public void testRemoveTableWithForeignKey() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColFK' type='VARCHAR' size='64'/>
                    <foreign-key name='TESTFK' foreignTable='TABLEB'>
                      <reference local='ColFK' foreign='COLPK'/>
                    </foreign-key>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='VARCHAR' size='64' primaryKey='true' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TABLEB'>
                    <column name='COLPK' type='VARCHAR' size='64' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE "TableA" DROP CONSTRAINT "TESTFK";
                DROP TABLE "TableA";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of a table that is referenced by a foreign key of an existing table.
     */
    public void testRemoveTableReferencedByForeignKey() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColFK' type='VARCHAR' size='64'/>
                    <foreign-key name='TESTFK' foreignTable='TABLEB'>
                      <reference local='ColFK' foreign='COLPK'/>
                    </foreign-key>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='VARCHAR' size='64' primaryKey='true' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColFK' type='VARCHAR' size='64'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE "TableA" DROP CONSTRAINT "TESTFK";
                DROP TABLE "TABLEB";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of two tables that have foreign key to each other.
     */
    public void testRemoveTablesWithForeignKeys() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColFK' type='DOUBLE'/>
                    <foreign-key name='TESTFK' foreignTable='TABLEB'>
                      <reference local='ColFK' foreign='COLPK'/>
                    </foreign-key>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='DOUBLE' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE "TableA" DROP CONSTRAINT "TESTFK";
                ALTER TABLE "TABLEB" DROP CONSTRAINT "TESTFK";
                DROP TABLE "TableA";
                DROP TABLE "TABLEB";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of an index to an existing table.
     */
    public void testAddIndex() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='VARCHAR' size='64'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='VARCHAR' size='64'/>
                    <index name='TestIndex'>
                      <index-column name='Col'/>
                    </index>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            "CREATE INDEX \"TestIndex\" ON \"TableA\" (\"Col\");\n",
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of a unique index to an existing table.
     */
    public void testAddUniqueIndex() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='VARCHAR' size='64'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='VARCHAR' size='64'/>
                    <unique name='TestIndex'>
                      <unique-column name='Col'/>
                    </unique>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            "CREATE UNIQUE INDEX \"TestIndex\" ON \"TableA\" (\"Col\");\n",
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of an index from a table.
     */
    public void testRemoveIndex() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='VARCHAR' size='64'/>
                    <index name='TestIndex'>
                      <index-column name='Col'/>
                    </index>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='VARCHAR' size='64'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            "DROP INDEX \"TestIndex\" ON \"TableA\";\n",
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of a unique index from a table.
     */
    public void testRemoveUniqueIndex() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='VARCHAR' size='64'/>
                    <unique name='TestIndex'>
                      <unique-column name='Col'/>
                    </unique>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='VARCHAR' size='64'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            "DROP INDEX \"TestIndex\" ON \"TableA\";\n",
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of a primary key to an existing table.
     */
    public void testAddPrimaryKey() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' required='true'/>
                    <column name='ColPK2' type='VARCHAR' size='64' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColPK2' type='VARCHAR' size='64' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            "ALTER TABLE \"TableA\" ADD CONSTRAINT \"TableA_PK\" PRIMARY KEY (\"ColPK1\",\"ColPK2\");\n",
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of a primary key and a column to an existing table.
     */
    public void testAddPrimaryKeyAndColumn() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' required='true'/>
                    <column name='ColPK2' type='VARCHAR' size='64' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColPK2' type='VARCHAR' size='64' primaryKey='true' required='true'/>
                    <column name='Col' type='DOUBLE'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE "TableA" ADD COLUMN "Col" DOUBLE;
                ALTER TABLE "TableA" ADD CONSTRAINT "TableA_PK" PRIMARY KEY ("ColPK1","ColPK2");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of a primary key from an existing table.
     */
    public void testRemovePrimaryKey() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColPK2' type='VARCHAR' size='64' primaryKey='true' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' required='true'/>
                    <column name='ColPK2' type='VARCHAR' size='64' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK1" INTEGER NOT NULL,
                    "ColPK2" VARCHAR(64) NOT NULL
                );
                INSERT INTO "TableA_" ("ColPK1", "ColPK2") SELECT "ColPK1", "ColPK2" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK1" INTEGER NOT NULL,
                    "ColPK2" VARCHAR(64) NOT NULL
                );
                INSERT INTO "TableA" ("ColPK1", "ColPK2") SELECT "ColPK1", "ColPK2" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of a column to an existing table.
     */
    public void testAddColumn() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='VARCHAR' size='64'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            "ALTER TABLE \"TableA\" ADD COLUMN \"Col\" VARCHAR(64);\n",
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of a column from an existing table.
     */
    public void testRemoveColumn() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='VARCHAR' size='64'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK") SELECT "ColPK" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK") SELECT "ColPK" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of a primary key column to an existing table.
     */
    public void testAddPrimaryKeyColumn() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColPK2' type='VARCHAR' primaryKey='true' size='64'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK1" INTEGER NOT NULL,
                    "ColPK2" VARCHAR(64),
                    PRIMARY KEY ("ColPK1","ColPK2")
                );
                INSERT INTO "TableA_" ("ColPK1") SELECT "ColPK1" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK1" INTEGER NOT NULL,
                    "ColPK2" VARCHAR(64),
                    PRIMARY KEY ("ColPK1","ColPK2")
                );
                INSERT INTO "TableA" ("ColPK1", "ColPK2") SELECT "ColPK1", "ColPK2" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of a primary key column from an existing table.
     */
    public void testRemovePrimaryKeyColumn() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColPK2' type='VARCHAR' primaryKey='true' size='64'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK1" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK1")
                );
                INSERT INTO "TableA_" ("ColPK1") SELECT "ColPK1" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK1" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK1")
                );
                INSERT INTO "TableA" ("ColPK1") SELECT "ColPK1" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of columns to the primary key of a table and the foreign key
     * of another table referencing it.
     */
    public void testAddColumnsToPrimaryAndForeignKeys() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='DOUBLE' primaryKey='true' required='true'/>
                    <column name='COLFK1' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK1' foreign='ColPK1'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColPK2' type='DOUBLE' primaryKey='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='DOUBLE' primaryKey='true' required='true'/>
                    <column name='COLFK1' type='INTEGER'/>
                    <column name='COLFK2' type='DOUBLE'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK1' foreign='ColPK1'/>
                      <reference local='COLFK2' foreign='ColPK2'/>
                    </foreign-key>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE "TABLEB" DROP CONSTRAINT "TESTFK";
                CREATE TABLE "TableA_"
                (
                    "ColPK1" INTEGER NOT NULL,
                    "ColPK2" DOUBLE,
                    PRIMARY KEY ("ColPK1","ColPK2")
                );
                INSERT INTO "TableA_" ("ColPK1") SELECT "ColPK1" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK1" INTEGER NOT NULL,
                    "ColPK2" DOUBLE,
                    PRIMARY KEY ("ColPK1","ColPK2")
                );
                INSERT INTO "TableA" ("ColPK1","ColPK2") SELECT "ColPK1","ColPK2" FROM "TableA_";
                DROP TABLE "TableA_";
                ALTER TABLE "TABLEB" ADD COLUMN "COLFK2" DOUBLE;
                ALTER TABLE "TABLEB" ADD CONSTRAINT "TESTFK" FOREIGN KEY ("COLFK1","COLFK2") REFERENCES "TableA" ("ColPK1","ColPK2");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of columns from the primary key of a table and the foreign key
     * of another table referencing it.
     */
    public void testRemoveColumnsFromPrimaryAndForeignKeys() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColPK2' type='DOUBLE' primaryKey='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='DOUBLE' primaryKey='true' required='true'/>
                    <column name='COLFK1' type='INTEGER'/>
                    <column name='COLFK2' type='DOUBLE'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK1' foreign='ColPK1'/>
                      <reference local='COLFK2' foreign='ColPK2'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='DOUBLE' primaryKey='true' required='true'/>
                    <column name='COLFK1' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK1' foreign='ColPK1'/>
                    </foreign-key>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE "TABLEB" DROP CONSTRAINT "TESTFK";
                CREATE TABLE "TableA_"
                (
                    "ColPK1" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK1")
                );
                INSERT INTO "TableA_" ("ColPK1") SELECT "ColPK1" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK1" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK1")
                );
                INSERT INTO "TableA" ("ColPK1") SELECT "ColPK1" FROM "TableA_";
                DROP TABLE "TableA_";
                CREATE TABLE "TABLEB_"
                (
                    "COLPK" DOUBLE NOT NULL,
                    "COLFK1" INTEGER,
                    PRIMARY KEY ("COLPK")
                );
                INSERT INTO "TABLEB_" ("COLPK","COLFK1") SELECT "COLPK","COLFK1" FROM "TABLEB";
                DROP TABLE "TABLEB";
                CREATE TABLE "TABLEB"
                (
                    "COLPK" DOUBLE NOT NULL,
                    "COLFK1" INTEGER,
                    PRIMARY KEY ("COLPK")
                );
                INSERT INTO "TABLEB" ("COLPK","COLFK1") SELECT "COLPK","COLFK1" FROM "TABLEB_";
                DROP TABLE "TABLEB_";
                ALTER TABLE "TABLEB" ADD CONSTRAINT "TESTFK" FOREIGN KEY ("COLFK1") REFERENCES "TableA" ("ColPK1");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of an index column to an existing table.
     */
    public void testAddIndexColumn() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col1' type='DOUBLE'/>
                    <index name='TESTINDEX'>
                      <index-column name='Col1'/>
                    </index>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col1' type='DOUBLE'/>
                    <column name='Col2' type='VARCHAR' size='64'/>
                    <index name='TESTINDEX'>
                      <index-column name='Col1'/>
                      <index-column name='Col2'/>
                    </index>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                DROP INDEX "TESTINDEX" ON "TableA";
                ALTER TABLE "TableA" ADD COLUMN "Col2" VARCHAR(64);
                CREATE INDEX "TESTINDEX" ON "TableA" ("Col1","Col2");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of an index column from an existing table.
     */
    public void testRemoveIndexColumn() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col1' type='DOUBLE'/>
                    <column name='Col2' type='VARCHAR' size='64'/>
                    <index name='TESTINDEX'>
                      <index-column name='Col1'/>
                      <index-column name='Col2'/>
                    </index>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col1' type='DOUBLE'/>
                    <index name='TESTINDEX'>
                      <index-column name='Col1'/>
                    </index>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                DROP INDEX "TESTINDEX" ON "TableA";
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col1" DOUBLE,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col1") SELECT "ColPK","Col1" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col1" DOUBLE,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK","Col1") SELECT "ColPK","Col1" FROM "TableA_";
                DROP TABLE "TableA_";
                CREATE INDEX "TESTINDEX" ON "TableA" ("Col1");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of a unique index column to an existing table.
     */
    public void testAddUniqueIndexColumn() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col1' type='DOUBLE'/>
                    <unique name='TESTINDEX'>
                      <unique-column name='Col1'/>
                    </unique>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col1' type='DOUBLE'/>
                    <column name='Col2' type='VARCHAR' size='64'/>
                    <unique name='TESTINDEX'>
                      <unique-column name='Col1'/>
                      <unique-column name='Col2'/>
                    </unique>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                DROP INDEX "TESTINDEX" ON "TableA";
                ALTER TABLE "TableA" ADD COLUMN "Col2" VARCHAR(64);
                CREATE UNIQUE INDEX "TESTINDEX" ON "TableA" ("Col1","Col2");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of a unique index column from an existing table.
     */
    public void testRemoveUniqueIndexColumn() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col1' type='DOUBLE'/>
                    <column name='Col2' type='VARCHAR' size='64'/>
                    <unique name='TESTINDEX'>
                      <unique-column name='Col1'/>
                      <unique-column name='Col2'/>
                    </unique>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col1' type='DOUBLE'/>
                    <unique name='TESTINDEX'>
                      <unique-column name='Col1'/>
                    </unique>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                DROP INDEX "TESTINDEX" ON "TableA";
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col1" DOUBLE,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col1") SELECT "ColPK","Col1" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col1" DOUBLE,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK","Col1") SELECT "ColPK","Col1" FROM "TableA_";
                DROP TABLE "TableA_";
                CREATE UNIQUE INDEX "TESTINDEX" ON "TableA" ("Col1");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }


    /**
     * Tests the addition of a column to a table with an index.
     */
    public void testAddColumnToTableWithIndex() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col1' type='DOUBLE'/>
                    <index name='TESTINDEX'>
                      <index-column name='Col1'/>
                    </index>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col1' type='DOUBLE'/>
                    <column name='Col2' type='VARCHAR' size='64'/>
                    <index name='TESTINDEX'>
                      <index-column name='Col1'/>
                    </index>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            "ALTER TABLE \"TableA\" ADD COLUMN \"Col2\" VARCHAR(64);\n",
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of a column to a table that has a foreign key.
     */
    public void testAddColumnToTableWithForeignKey() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER'/>
                    <column name='COL' type='DOUBLE'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            "ALTER TABLE \"TABLEB\" ADD COLUMN \"COL\" DOUBLE;\n",
            getAlterModelSQL(model1Xml, model2Xml));
    }

    // TODO: insert column (not add) into table (also with index/foreign key)

    /**
     * Tests the addition of a column to a table that is referenced by a foreign key.
     */
    public void testAddColumnToTableReferencedByForeignKey() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='DOUBLE'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            "ALTER TABLE \"TableA\" ADD COLUMN \"Col\" DOUBLE;\n",
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of a column to a table that is referenced by a foreign key.
     */
    public void testInsertColumnToTableReferencedByForeignKey() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='Col' type='DOUBLE'/>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE "TABLEB" DROP CONSTRAINT "TESTFK";
                CREATE TABLE "TableA_"
                (
                    "Col" DOUBLE,
                    "ColPK" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK") SELECT "ColPK" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "Col" DOUBLE,
                    "ColPK" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("Col","ColPK") SELECT "Col","ColPK" FROM "TableA_";
                DROP TABLE "TableA_";
                ALTER TABLE "TABLEB" ADD CONSTRAINT "TESTFK" FOREIGN KEY ("COLFK") REFERENCES "TableA" ("ColPK");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of an existing column to a primary key.
     */
    public void testAddExistingColumnToPrimaryKey() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColPK2' type='DOUBLE' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColPK2' type='DOUBLE' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK1" INTEGER NOT NULL,
                    "ColPK2" DOUBLE NOT NULL,
                    PRIMARY KEY ("ColPK1","ColPK2")
                );
                INSERT INTO "TableA_" ("ColPK1","ColPK2") SELECT "ColPK1","ColPK2" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK1" INTEGER NOT NULL,
                    "ColPK2" DOUBLE NOT NULL,
                    PRIMARY KEY ("ColPK1","ColPK2")
                );
                INSERT INTO "TableA" ("ColPK1","ColPK2") SELECT "ColPK1","ColPK2" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of a column from a primary key.
     */
    public void testRemoveColumnFromPrimaryKey() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColPK2' type='DOUBLE' primaryKey='true' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' required='true'/>
                    <column name='ColPK2' type='DOUBLE' primaryKey='true' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK1" INTEGER NOT NULL,
                    "ColPK2" DOUBLE NOT NULL,
                    PRIMARY KEY ("ColPK2")
                );
                INSERT INTO "TableA_" ("ColPK1","ColPK2") SELECT "ColPK1","ColPK2" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK1" INTEGER NOT NULL,
                    "ColPK2" DOUBLE NOT NULL,
                    PRIMARY KEY ("ColPK2")
                );
                INSERT INTO "TableA" ("ColPK1","ColPK2") SELECT "ColPK1","ColPK2" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of existing columns to a primary and the referencing foreign key.
     */
    public void testAddExistingColumnsToPrimaryAndForeignKey() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColPK2' type='DOUBLE' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK1' type='DOUBLE'/>
                    <column name='COLFK2' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK2' foreign='ColPK1'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColPK2' type='DOUBLE' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK1' type='DOUBLE'/>
                    <column name='COLFK2' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK2' foreign='ColPK1'/>
                      <reference local='COLFK1' foreign='ColPK2'/>
                    </foreign-key>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE "TABLEB" DROP CONSTRAINT "TESTFK";
                CREATE TABLE "TableA_"
                (
                    "ColPK1" INTEGER NOT NULL,
                    "ColPK2" DOUBLE NOT NULL,
                    PRIMARY KEY ("ColPK1","ColPK2")
                );
                INSERT INTO "TableA_" ("ColPK1","ColPK2") SELECT "ColPK1","ColPK2" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK1" INTEGER NOT NULL,
                    "ColPK2" DOUBLE NOT NULL,
                    PRIMARY KEY ("ColPK1","ColPK2")
                );
                INSERT INTO "TableA" ("ColPK1","ColPK2") SELECT "ColPK1","ColPK2" FROM "TableA_";
                DROP TABLE "TableA_";
                ALTER TABLE "TABLEB" ADD CONSTRAINT "TESTFK" FOREIGN KEY ("COLFK2","COLFK1") REFERENCES "TableA" ("ColPK1","ColPK2");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of existing columns from a primary and the referencing foreign key.
     */
    public void testRemoveExistingColumnsFromPrimaryAndForeignKey() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColPK2' type='DOUBLE' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK1' type='DOUBLE'/>
                    <column name='COLFK2' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK2' foreign='ColPK1'/>
                      <reference local='COLFK1' foreign='ColPK2'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK1' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='ColPK2' type='DOUBLE' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK1' type='DOUBLE'/>
                    <column name='COLFK2' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK2' foreign='ColPK1'/>
                    </foreign-key>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE "TABLEB" DROP CONSTRAINT "TESTFK";
                CREATE TABLE "TableA_"
                (
                    "ColPK1" INTEGER NOT NULL,
                    "ColPK2" DOUBLE NOT NULL,
                    PRIMARY KEY ("ColPK1")
                );
                INSERT INTO "TableA_" ("ColPK1","ColPK2") SELECT "ColPK1","ColPK2" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK1" INTEGER NOT NULL,
                    "ColPK2" DOUBLE NOT NULL,
                    PRIMARY KEY ("ColPK1")
                );
                INSERT INTO "TableA" ("ColPK1","ColPK2") SELECT "ColPK1","ColPK2" FROM "TableA_";
                DROP TABLE "TableA_";
                ALTER TABLE "TABLEB" ADD CONSTRAINT "TESTFK" FOREIGN KEY ("COLFK2") REFERENCES "TableA" ("ColPK1");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of an existing column to an index.
     */
    public void testAddExistingColumnToIndex() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col1' type='DOUBLE'/>
                    <column name='Col2' type='VARCHAR' size='64'/>
                    <unique name='TESTINDEX'>
                      <unique-column name='Col1'/>
                    </unique>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col1' type='DOUBLE'/>
                    <column name='Col2' type='VARCHAR' size='64'/>
                    <unique name='TESTINDEX'>
                      <unique-column name='Col1'/>
                      <unique-column name='Col2'/>
                    </unique>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                DROP INDEX "TESTINDEX" ON "TableA";
                CREATE UNIQUE INDEX "TESTINDEX" ON "TableA" ("Col1","Col2");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the addition of an existing column from an index.
     */
    public void testRemoveExistingColumnFromIndex() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col1' type='DOUBLE'/>
                    <column name='Col2' type='VARCHAR' size='64'/>
                    <index name='TESTINDEX'>
                      <index-column name='Col1'/>
                      <index-column name='Col2'/>
                    </index>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col1' type='DOUBLE'/>
                    <column name='Col2' type='VARCHAR' size='64'/>
                    <index name='TESTINDEX'>
                      <index-column name='Col1'/>
                    </index>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                DROP INDEX "TESTINDEX" ON "TableA";
                CREATE INDEX "TESTINDEX" ON "TableA" ("Col1");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the removal of a column from a table referenced by a foreign key.
     */
    public void testRemoveColumnFromTableReferencedByForeignKey() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='DOUBLE'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE "TABLEB" DROP CONSTRAINT "TESTFK";
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK") SELECT "ColPK" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK") SELECT "ColPK" FROM "TableA_";
                DROP TABLE "TableA_";
                ALTER TABLE "TABLEB" ADD CONSTRAINT "TESTFK" FOREIGN KEY ("COLFK") REFERENCES "TableA" ("ColPK");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of a column's datatype.
     */
    public void testChangeColumnDatatype() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='DOUBLE' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" DOUBLE NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" DOUBLE NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of a primary key column's datatype.
     */
    public void testChangePrimaryKeyColumnDatatype() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='DOUBLE' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" DOUBLE NOT NULL,
                    "Col" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" DOUBLE NOT NULL,
                    "Col" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of an index column's datatype.
     */
    public void testChangeIndexColumnDatatype() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' required='true'/>
                    <index name='TestIndex'>
                      <index-column name='Col'/>
                    </index>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='DOUBLE' required='true'/>
                    <index name='TestIndex'>
                      <index-column name='Col'/>
                    </index>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" DOUBLE NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" DOUBLE NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                CREATE INDEX "TestIndex" ON "TableA" ("Col");
                INSERT INTO "TableA" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of the datatype of the columns of a primary key and the referencing foreign key.
     */
    public void testChangePrimaryAndForeignKeyColumnsDatatype() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='DOUBLE' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='DOUBLE'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE "TABLEB" DROP CONSTRAINT "TESTFK";
                CREATE TABLE "TableA_"
                (
                    "ColPK" DOUBLE NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK") SELECT "ColPK" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" DOUBLE NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK") SELECT "ColPK" FROM "TableA_";
                DROP TABLE "TableA_";
                CREATE TABLE "TABLEB_"
                (
                    "COLPK" INTEGER NOT NULL,
                    "COLFK" DOUBLE,
                    PRIMARY KEY ("COLPK")
                );
                INSERT INTO "TABLEB_" ("COLPK","COLFK") SELECT "COLPK","COLFK" FROM "TABLEB";
                DROP TABLE "TABLEB";
                CREATE TABLE "TABLEB"
                (
                    "COLPK" INTEGER NOT NULL,
                    "COLFK" DOUBLE,
                    PRIMARY KEY ("COLPK")
                );
                INSERT INTO "TABLEB" ("COLPK","COLFK") SELECT "COLPK","COLFK" FROM "TABLEB_";
                DROP TABLE "TABLEB_";
                ALTER TABLE "TABLEB" ADD CONSTRAINT "TESTFK" FOREIGN KEY ("COLFK") REFERENCES "TableA" ("ColPK");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of a column's size.
     */
    public void testChangeColumnSize() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='VARCHAR' size='32' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='VARCHAR' size='64' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" VARCHAR(64) NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" VARCHAR(64) NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of a primary key column's size.
     */
    public void testChangePrimaryKeyColumnSize() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='DECIMAL' size='15,2' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='DECIMAL' size='30,4' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" DECIMAL(30,4) NOT NULL,
                    "Col" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" DECIMAL(30,4) NOT NULL,
                    "Col" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of an index column's size.
     */
    public void testChangeIndexColumnSize() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='DECIMAL' size='10,4' required='true'/>
                    <index name='TestIndex'>
                      <index-column name='Col'/>
                    </index>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='DECIMAL' size='15,2' required='true'/>
                    <index name='TestIndex'>
                      <index-column name='Col'/>
                    </index>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" DECIMAL(15,2) NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" DECIMAL(15,2) NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                CREATE INDEX "TestIndex" ON "TableA" ("Col");
                INSERT INTO "TableA" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of the size of the columns of a primary key and the referencing foreign key.
     */
    public void testChangePrimaryAndForeignKeyColumnsSize() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='VARCHAR' size='32' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='VARCHAR' size='32'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='VARCHAR' size='64' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='VARCHAR' size='64'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE "TABLEB" DROP CONSTRAINT "TESTFK";
                CREATE TABLE "TableA_"
                (
                    "ColPK" VARCHAR(64) NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK") SELECT "ColPK" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" VARCHAR(64) NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK") SELECT "ColPK" FROM "TableA_";
                DROP TABLE "TableA_";
                CREATE TABLE "TABLEB_"
                (
                    "COLPK" INTEGER NOT NULL,
                    "COLFK" VARCHAR(64),
                    PRIMARY KEY ("COLPK")
                );
                INSERT INTO "TABLEB_" ("COLPK","COLFK") SELECT "COLPK","COLFK" FROM "TABLEB";
                DROP TABLE "TABLEB";
                CREATE TABLE "TABLEB"
                (
                    "COLPK" INTEGER NOT NULL,
                    "COLFK" VARCHAR(64),
                    PRIMARY KEY ("COLPK")
                );
                INSERT INTO "TABLEB" ("COLPK","COLFK") SELECT "COLPK","COLFK" FROM "TABLEB_";
                DROP TABLE "TABLEB_";
                ALTER TABLE "TABLEB" ADD CONSTRAINT "TESTFK" FOREIGN KEY ("COLFK") REFERENCES "TableA" ("ColPK");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of a column's default value.
     */
    public void testChangeColumnDefault() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='VARCHAR' size='32' default='test 1' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='VARCHAR' size='32' default='test 2' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" VARCHAR(32) DEFAULT 'test 2' NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" VARCHAR(32) DEFAULT 'test 2' NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of a primary key column's default value.
     */
    public void testChangePrimaryKeyColumnDefault() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='DECIMAL' size='15,2' default='2.0' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='DECIMAL' size='15,2' default='4.0' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" DECIMAL(15,2) DEFAULT 4.0 NOT NULL,
                    "Col" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" DECIMAL(15,2) DEFAULT 4.0 NOT NULL,
                    "Col" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of a index column's default value.
     */
    public void testChangeIndexColumnDefault() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='DATE' default='2000-01-02' required='true'/>
                    <index name='TestIndex'>
                      <index-column name='Col'/>
                    </index>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='DATE' default='2001-02-03' required='true'/>
                    <index name='TestIndex'>
                      <index-column name='Col'/>
                    </index>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" DATE DEFAULT '2001-02-03' NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" DATE DEFAULT '2001-02-03' NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                CREATE INDEX "TestIndex" ON "TableA" ("Col");
                INSERT INTO "TableA" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of the default value of the columns of a primary key and the referencing foreign key.
     */
    public void testChangePrimaryAndForeignKeyColumnsDefault() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' default='0' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER' default='1'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' default='1' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER' default='0'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE "TABLEB" DROP CONSTRAINT "TESTFK";
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER DEFAULT 1 NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK") SELECT "ColPK" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER DEFAULT 1 NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK") SELECT "ColPK" FROM "TableA_";
                DROP TABLE "TableA_";
                CREATE TABLE "TABLEB_"
                (
                    "COLPK" INTEGER NOT NULL,
                    "COLFK" INTEGER DEFAULT 0,
                    PRIMARY KEY ("COLPK")
                );
                INSERT INTO "TABLEB_" ("COLPK","COLFK") SELECT "COLPK","COLFK" FROM "TABLEB";
                DROP TABLE "TABLEB";
                CREATE TABLE "TABLEB"
                (
                    "COLPK" INTEGER NOT NULL,
                    "COLFK" INTEGER DEFAULT 0,
                    PRIMARY KEY ("COLPK")
                );
                INSERT INTO "TABLEB" ("COLPK","COLFK") SELECT "COLPK","COLFK" FROM "TABLEB_";
                DROP TABLE "TABLEB_";
                ALTER TABLE "TABLEB" ADD CONSTRAINT "TESTFK" FOREIGN KEY ("COLFK") REFERENCES "TableA" ("ColPK");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of a column's auto-increment attribute.
     */
    public void testChangeColumnAutoIncrement() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' autoIncrement='true' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' autoIncrement='false' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of a primary key column's auto-increment attribute.
     */
    public void testChangePrimaryKeyColumnAutoIncrement() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' autoIncrement='false' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' autoIncrement='true' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL IDENTITY,
                    "Col" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL IDENTITY,
                    "Col" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of an index column's auto-increment attribute.
     */
    public void testChangeIndexColumnAutoIncrement() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' autoIncrement='false' required='true'/>
                    <index name='TestIndex'>
                      <index-column name='Col'/>
                    </index>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' autoIncrement='true' required='true'/>
                    <index name='TestIndex'>
                      <index-column name='Col'/>
                    </index>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" INTEGER NOT NULL IDENTITY,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" INTEGER NOT NULL IDENTITY,
                    PRIMARY KEY ("ColPK")
                );
                CREATE INDEX "TestIndex" ON "TableA" ("Col");
                INSERT INTO "TableA" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of the auto-increment attribute of the columns of a primary key
     * and the referencing foreign key.
     */
    public void testChangePrimaryAndForeignKeyColumnsAutoIncrement() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' autoIncrement='true' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' autoIncrement='false' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE "TABLEB" DROP CONSTRAINT "TESTFK";
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK") SELECT "ColPK" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK") SELECT "ColPK" FROM "TableA_";
                DROP TABLE "TableA_";
                ALTER TABLE "TABLEB" ADD CONSTRAINT "TESTFK" FOREIGN KEY ("COLFK") REFERENCES "TableA" ("ColPK");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of a column's required attribute.
     */
    public void testChangeColumnRequired() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' required='false'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" INTEGER,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" INTEGER,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of a primary key column's required attribute.
     */
    public void testChangePrimaryKeyColumnRequired() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' required='true'/>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='false'/>
                    <column name='Col' type='INTEGER' required='true'/>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER,
                    "Col" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER,
                    "Col" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of an index column's required attribute.
     */
    public void testChangeIndexColumnRequired() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' required='false'/>
                    <index name='TestIndex'>
                      <index-column name='Col'/>
                    </index>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='Col' type='INTEGER' required='true'/>
                    <index name='TestIndex'>
                      <index-column name='Col'/>
                    </index>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    "Col" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                CREATE INDEX "TestIndex" ON "TableA" ("Col");
                INSERT INTO "TableA" ("ColPK","Col") SELECT "ColPK","Col" FROM "TableA_";
                DROP TABLE "TableA_";
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }

    /**
     * Tests the change of the required attribute of the columns of a primary key
     * and the referencing foreign key.
     */
    public void testChangePrimaryAndForeignKeyColumnsRequired() throws IOException {
        final String model1Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='false'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER' required='true'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";
        final String model2Xml =
            """
                <?xml version='1.0' encoding='ISO-8859-1'?>
                <database xmlns='http://db.apache.org/ddlutils/schema/1.1' name='test'>
                  <table name='TableA'>
                    <column name='ColPK' type='INTEGER' primaryKey='true' required='true'/>
                  </table>
                  <table name='TABLEB'>
                    <column name='COLPK' type='INTEGER' primaryKey='true' required='true'/>
                    <column name='COLFK' type='INTEGER' required='false'/>
                    <foreign-key name='TESTFK' foreignTable='TableA'>
                      <reference local='COLFK' foreign='ColPK'/>
                    </foreign-key>
                  </table>
                </database>""";

        assertEqualsIgnoringWhitespaces(
            """
                ALTER TABLE "TABLEB" DROP CONSTRAINT "TESTFK";
                CREATE TABLE "TableA_"
                (
                    "ColPK" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA_" ("ColPK") SELECT "ColPK" FROM "TableA";
                DROP TABLE "TableA";
                CREATE TABLE "TableA"
                (
                    "ColPK" INTEGER NOT NULL,
                    PRIMARY KEY ("ColPK")
                );
                INSERT INTO "TableA" ("ColPK") SELECT "ColPK" FROM "TableA_";
                DROP TABLE "TableA_";
                CREATE TABLE "TABLEB_"
                (
                    "COLPK" INTEGER NOT NULL,
                    "COLFK" INTEGER,
                    PRIMARY KEY ("COLPK")
                );
                INSERT INTO "TABLEB_" ("COLPK","COLFK") SELECT "COLPK","COLFK" FROM "TABLEB";
                DROP TABLE "TABLEB";
                CREATE TABLE "TABLEB"
                (
                    "COLPK" INTEGER NOT NULL,
                    "COLFK" INTEGER,
                    PRIMARY KEY ("COLPK")
                );
                INSERT INTO "TABLEB" ("COLPK","COLFK") SELECT "COLPK","COLFK" FROM "TABLEB_";
                DROP TABLE "TABLEB_";
                ALTER TABLE "TABLEB" ADD CONSTRAINT "TESTFK" FOREIGN KEY ("COLFK") REFERENCES "TableA" ("ColPK");
                """,
            getAlterModelSQL(model1Xml, model2Xml));
    }
}
