package sample;

import org.apache.ddlutils.DatabasePlatform;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.platform.SqlBuildContext;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.platform.mysql.MySql50Platform;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

public class Sample {

    public static void main(String[] args) throws IOException {

        final String schema =
            "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                "<database xmlns='" + DatabaseIO.DDLUTILS_NAMESPACE + "' name='columnconstraintstest'>\n" +
                "  <table name='user'>\n" +
                "    <column name='name' type='varchar' size='32' primaryKey='true' description='主键'/>\n" +
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

        Table table = database.getTable(0);

        Index index = new UniqueIndex();
        index.setName("idx_001");
        index.addColumn(new IndexColumn(table.getColumn(3)));

        table.addIndex(index);

        // we're turning the comment creation off to make testing easier
        // disable the comment
        DatabasePlatform platform = new MySql50Platform();
        platform.setSqlCommentsOn(false);

        SqlBuilder sqlBuilder = platform.getSqlBuilder();

        StringWriter sw = new StringWriter();

        sqlBuilder.setWriter(sw);

        SqlBuildContext context = new SqlBuildContext();

        context.addGlobalParam("engine", "InnoDB");
        context.addGlobalParam("encoding", "utf8mb4");

        sqlBuilder.createTables(database, context, true);

        System.out.println(sw);
    }
}
