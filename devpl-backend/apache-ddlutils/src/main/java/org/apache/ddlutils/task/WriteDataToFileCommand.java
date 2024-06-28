package org.apache.ddlutils.task;

import org.apache.ddlutils.DdlUtilsTaskException;
import org.apache.ddlutils.model.Database;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Reads the data currently in the table in the live database (as specified by the
 * enclosing task), and writes it as XML to a file.
 */
public class WriteDataToFileCommand extends ConvertingDatabaseCommand {
    /**
     * The file to output the data to.
     */
    private File _outputFile;
    /**
     * The character encoding to use.
     */
    private String _encoding;

    /**
     * Whether DdlUtils should search for the schema of the tables. @deprecated
     */
    private boolean _determineSchema;

    /**
     * Specifies the file to write the data XML to.
     *
     * @param outputFile The output file
     */
    public void setOutputFile(File outputFile) {
        _outputFile = outputFile;
    }

    /**
     * Specifies the encoding of the XML file.
     *
     * @param encoding The encoding
     *                 The default encoding is <code>UTF-8</code>.
     */
    public void setEncoding(String encoding) {
        _encoding = encoding;
    }

    /**
     * Specifies whether DdlUtils should try to find the schema of the tables when reading data
     * from a live database.
     *
     * @param determineSchema Whether to try to find the table's schemas
     */
    public void setDetermineSchema(boolean determineSchema) {
        _determineSchema = determineSchema;
    }

    @Override
    public void execute(DatabaseTask task, Database model) throws DdlUtilsTaskException {
        try {
            getDataIO().setDetermineSchema(_determineSchema);
            getDataIO().writeDataToXML(getPlatform(), model,
                new FileOutputStream(_outputFile), _encoding);
            _log.info("Written data XML to file" + _outputFile.getAbsolutePath());
        } catch (Exception ex) {
            handleException(ex, ex.getMessage());
        }
    }
}
