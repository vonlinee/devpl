package org.apache.ddlutils.io;

import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Generates the DTD for data xml files usable with a specified database model.
 */
public class DataDtdWriter {
    /**
     * Writes the DTD for data xml files for the given database model, to the specified writer.
     *
     * @param model  The database model
     * @param output The writer to write the DTD to
     */
    public void writeDtd(Database model, Writer output) throws IOException {
        PrintWriter writer = new PrintWriter(output);

        writer.println("<!-- DTD for XML data files for database " + model.getName() + " -->\n");
        writer.println("<!ELEMENT data (");
        for (int idx = 0; idx < model.getTableCount(); idx++) {
            Table table = model.getTable(idx);

            writer.print("    " + table.getName());
            if (idx < model.getTableCount() - 1) {
                writer.println(" |");
            } else {
                writer.println();
            }
        }
        writer.println(")*>");
        for (int idx = 0; idx < model.getTableCount(); idx++) {
            writeTableElement(model.getTable(idx), writer);
        }
    }

    /**
     * Writes the DTD element for the given table.
     *
     * @param table  The table
     * @param writer The writer to write the element to
     */
    private void writeTableElement(Table table, PrintWriter writer) throws IOException {
        writer.println("\n<!ELEMENT " + table.getName() + " EMPTY>");
        writer.println("<!ATTLIST " + table.getName());

        for (int idx = 0; idx < table.getColumnCount(); idx++) {
            writeColumnAttributeEntry(table.getColumn(idx), writer);
        }
        writer.println(">");
    }

    /**
     * Writes the DTD attribute entry for the given column.
     *
     * @param column The column
     * @param writer The writer to write the attribute entry to
     */
    private void writeColumnAttributeEntry(Column column, PrintWriter writer) throws IOException {
        writer.print("    <!--");
        if (column.isPrimaryKey()) {
            writer.print(" primary key,");
        }
        if (column.isAutoIncrement()) {
            writer.print(" auto increment,");
        }
        writer.print(" JDBC type: " + column.getType());
        if ((column.getSize() != null) && (!column.getSize().isEmpty())) {
            writer.print("(" + column.getSize() + ")");
        }
        writer.println(" -->");
        writer.print("    " + column.getName() + " CDATA ");
        if ((column.getDefaultValue() != null) && (!column.getDefaultValue().isEmpty())) {
            writer.println("\"" + column.getDefaultValue() + "\"");
        } else {
            writer.println(column.isRequired() ? "#REQUIRED" : "#IMPLIED");
        }
    }
}
