package org.apache.ddlutils.task;

import org.apache.ddlutils.io.DataDtdWriter;
import org.apache.ddlutils.model.Database;
import org.apache.tools.ant.BuildException;

import java.io.File;
import java.io.FileWriter;

/**
 * Creates a DTD that specifies the layout for data XML files.<br/>
 * This sub-task does not require a database connection, so the <code>dataSource</code>
 * sub element of the enclosing task can be omitted.
 *
 * @ant.task name="writeDtdToFile"
 */
public class WriteDtdToFileCommand extends Command {
    /**
     * The file to output the DTD to.
     */
    private File _outputFile;

    /**
     * Specifies the name of the file to write the DTD to.
     *
     * @param outputFile The output file
     */
    public void setOutputFile(File outputFile) {
        _outputFile = outputFile;
    }

    @Override
    public void execute(DatabaseTaskBase task, Database model) throws BuildException {
        if (_outputFile == null) {
            throw new BuildException("No output file specified");
        }
        if (_outputFile.exists() && !_outputFile.canWrite()) {
            throw new BuildException("Cannot overwrite output file " + _outputFile.getAbsolutePath());
        }

        try {
            FileWriter outputWriter = new FileWriter(_outputFile);
            DataDtdWriter dtdWriter = new DataDtdWriter();

            dtdWriter.writeDtd(model, outputWriter);
            outputWriter.close();
            _log.info("Written DTD to " + _outputFile.getAbsolutePath());
        } catch (Exception ex) {
            handleException(ex, "Failed to write to output file " + _outputFile.getAbsolutePath());
        }
    }
}
