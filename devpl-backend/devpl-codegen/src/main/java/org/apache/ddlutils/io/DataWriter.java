package org.apache.ddlutils.io;


import org.apache.ddlutils.io.converters.SqlTypeConverter;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.model.TableClass;
import org.apache.ddlutils.model.TableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Writes dyna beans matching a specified database model into an XML file.
 */
public class DataWriter extends PrettyPrintingXmlWriter {
    /**
     * Our log.
     */
    private final Logger _log = LoggerFactory.getLogger(DataWriter.class);

    /**
     * The converters.
     */
    private final ConverterConfiguration _converterConf = new ConverterConfiguration();

    /**
     * Creates a data writer instance using UTF-8 encoding.
     *
     * @param output The target to write the data XML to
     */
    public DataWriter(OutputStream output) throws DataWriterException {
        this(output, "UTF-8");
    }

    /**
     * Creates a data writer instance.
     *
     * @param output   The target to write the data XML to
     * @param encoding The encoding of the XML file
     */
    public DataWriter(OutputStream output, String encoding) throws DataWriterException {
        super(output, encoding);
    }

    /**
     * Creates a data writer instance using the specified writer. Note that the writer
     * needs to be configured using the specified encoding.
     *
     * @param output   The target to write the data XML to
     * @param encoding The encoding of the writer
     */
    public DataWriter(Writer output, String encoding) throws DataWriterException {
        super(output, encoding);
    }

    protected void throwException(Exception baseEx) throws DdlUtilsXMLException {
        throw new DataWriterException(baseEx);
    }

    /**
     * Returns the converter configuration of this data reader.
     *
     * @return The converter configuration
     */
    public ConverterConfiguration getConverterConfiguration() {
        return _converterConf;
    }

    /**
     * Writes the start of the XML document, including the start of the outermost
     * XML element (<code>data</code>).
     */
    public void writeDocumentStart() throws DdlUtilsXMLException {
        super.writeDocumentStart();
        writeElementStart(null, "data");
        printlnIfPrettyPrinting();
    }

    /**
     * Writes the end of the XML document, including the end of the outermost
     * XML element (<code>data</code>).
     */
    public void writeDocumentEnd() throws DdlUtilsXMLException {
        writeElementEnd();
        printlnIfPrettyPrinting();
        super.writeDocumentEnd();
    }

    /**
     * Writes the given bean.
     *
     * @param bean The bean to write
     */
    public void write(TableObject bean) throws DataWriterException {
        TableClass dynaClass = bean.getTableClass();
        Table table = dynaClass.getTable();
        TableXmlWriter tableWriter = new TableXmlWriter(table);
        List<ColumnXmlWriter> columnWriters = new ArrayList<>();

        for (int idx = 0; idx < table.getColumnCount(); idx++) {
            Column column = table.getColumn(idx);
            Object value = bean.getColumnValue(column.getName());
            SqlTypeConverter converter = _converterConf.getRegisteredConverter(table, column);
            String valueAsText = null;

            if (converter == null) {
                if (value != null) {
                    valueAsText = value.toString();
                }
            } else {
                valueAsText = converter.toString(value, column.getTypeCode());
            }
            if (valueAsText != null) {
                columnWriters.add(new ColumnXmlWriter(column, valueAsText));
            }
        }

        tableWriter.write(columnWriters, this);
    }

    /**
     * Writes the beans contained in the given iterator.
     *
     * @param beans The beans iterator
     */
    public void write(Iterator<TableObject> beans) throws DataWriterException {
        while (beans.hasNext()) {
            TableObject bean = beans.next();
            if (bean != null) {
                write(bean);
            }
        }
    }

    /**
     * Writes the beans contained in the given collection.
     *
     * @param beans The beans
     */
    public void write(Collection<TableObject> beans) throws DataWriterException {
        write(beans.iterator());
    }
}
