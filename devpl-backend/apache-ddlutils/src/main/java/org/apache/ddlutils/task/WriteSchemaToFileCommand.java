package org.apache.ddlutils.task;

import org.apache.ddlutils.DdlUtilsTaskException;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;

import java.io.File;
import java.io.FileWriter;

/**
 * Reads the schema of the live database (as specified in the enclosing task), and writes
 * it as XML to a file.
 */
public class WriteSchemaToFileCommand extends Command {
    /**
     * The file to output the schema to.
     */
    private File _outputFile;

    /**
     * Specifies the name of the file to write the schema XML to.
     *
     * @param outputFile The output file
     */
    public void setOutputFile(File outputFile) {
        _outputFile = outputFile;
    }

    @Override
    public void execute(DatabaseTask task, Database model) throws DdlUtilsTaskException {
        if (_outputFile == null) {
            throw new DdlUtilsTaskException("No output file specified");
        }
        if (_outputFile.exists() && !_outputFile.canWrite()) {
            throw new DdlUtilsTaskException("Cannot overwrite output file " + _outputFile.getAbsolutePath());
        }
        try (FileWriter outputWriter = new FileWriter(_outputFile)) {
            DatabaseIO dbIO = new DatabaseIO();
            dbIO.write(model, outputWriter);
            _log.info("Written schema to " + _outputFile.getAbsolutePath());
        } catch (Exception ex) {
            handleException(ex, "Failed to write to output file " + _outputFile.getAbsolutePath());
        }
    }
}
