package sample;

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.platform.mysql.MySql50Platform;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class Main {

    public static void main(String[] args) throws IOException {

        final String schema =
            "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='columnconstraintstest'>\n" +
                "  <table name='user'>\n" +
                "    <column name='name' type='VARCHAR' size='32' primaryKey='true' description='主键'/>\n" +
                "    <column name='age' type='INTEGER' primaryKey='false'/>\n" +
                "    <column name='sex' type='BINARY' size='100' required='true'/>\n" +
                "    <column name='class_name' type='DOUBLE' required='true' default='-2.0'/>\n" +
                "    <column name='col' type='CHAR' size='4' default='test'/>\n" +
                "    <column name='slkd' type='BIGINT'/>\n" +
                "  </table>\n" +
                "</database>";

        DatabaseIO dbIO = new DatabaseIO();
        dbIO.setValidateXml(false);
        Database database = dbIO.read(new StringReader(schema));

        // we're turning the comment creation off to make testing easier
        // disable the comment
        DatabasePlatform platform = new MySql50Platform();
        platform.setSqlCommentsOn(true);

        SqlBuilder sqlBuilder = platform.getSqlBuilder();

        StringWriter sw = new StringWriter();

        sqlBuilder.setWriter(sw);
        sqlBuilder.createTables(database, null, true);

        System.out.println(sw);
    }
}
