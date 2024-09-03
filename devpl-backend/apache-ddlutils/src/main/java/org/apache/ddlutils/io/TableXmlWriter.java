package org.apache.ddlutils.io;

import org.apache.ddlutils.model.Table;

import java.util.List;

/**
 * Base interface for different strategies to write the XML for a data bean for a specific table.
 */
public class TableXmlWriter extends ModelXmlWriter {
    private static final int AS_TAG_NAME = 0;
    private static final int AS_ATTRIBUTE = 1;
    private static final int AS_SUB_TAG = 2;

    private final String tableName;
    private final int formattingMethod;
    private final boolean base64Encoded;

    public TableXmlWriter(Table table) {
        if (XMLUtils.hasIllegalXMLCharacters(table.getName())) {
            tableName = XMLUtils.base64Encode(table.getName());
            formattingMethod = AS_SUB_TAG;
            base64Encoded = true;
        } else {
            tableName = table.getName();
            base64Encoded = false;
            if (tableName.length() > XMLUtils.MAX_NAME_LENGTH) {
                formattingMethod = AS_SUB_TAG;
            } else if ("table".equals(tableName) || !XMLUtils.isWellFormedXMLName(tableName)) {
                formattingMethod = AS_ATTRIBUTE;
            } else {
                formattingMethod = AS_TAG_NAME;
            }
        }
    }

    /**
     * Write the table data to XML to the given writer.
     *
     * @param columnXmlWriters A list of column xml writers for writing out the bean's values to XML
     * @param writer           The writer to write to
     */
    public void write(List<ColumnXmlWriter> columnXmlWriters, DataWriter writer) {
        writer.indentIfPrettyPrinting(1);
        if (formattingMethod == AS_TAG_NAME) {
            writer.writeElementStart(null, tableName);
        } else {
            writer.writeElementStart(null, "table");
        }
        if (formattingMethod == AS_ATTRIBUTE) {
            writer.writeAttribute(null, "table-name", tableName);
        }
        for (ColumnXmlWriter columnXmlWriter : columnXmlWriters) {
            columnXmlWriter.writeAttribute(writer);
        }

        boolean hasSubTags = false;

        if (formattingMethod == AS_SUB_TAG) {
            writer.printlnIfPrettyPrinting();
            writer.indentIfPrettyPrinting(2);
            writer.writeElementStart(null, "table-name");
            writeText(writer, tableName, base64Encoded);
            writer.writeElementEnd();
            hasSubTags = true;
        }
        for (ColumnXmlWriter columnXmlWriter : columnXmlWriters) {
            hasSubTags = columnXmlWriter.writeSubElement(writer) || hasSubTags;
        }
        if (hasSubTags) {
            writer.printlnIfPrettyPrinting();
            writer.indentIfPrettyPrinting(1);
        }
        writer.writeElementEnd();
        writer.printlnIfPrettyPrinting();
    }
}