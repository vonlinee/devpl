package org.apache.ddlutils.task;

import org.apache.ddlutils.DdlUtilsTaskException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.CreationParameters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 将数据库转换为SQL
 */
public class DatabaseToDdlCommand extends Command {

    private File outputFile;
    private CreationParameters parameters;

    public CreationParameters getParameters() {
        return parameters;
    }

    public void setParameters(CreationParameters parameters) {
        this.parameters = parameters;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public void execute(DatabaseTask task, Database model) throws DdlUtilsTaskException {
        if (outputFile == null) {
            if (isFailOnError()) {
                throw new DdlUtilsTaskException("output file is null");
            } else {
                _log.error("cannot execute command {}, cause the output file is null", this.getClass().getName());
            }
            return;
        }
        Platform platform = task.getPlatform();
        _log.info("executing with platform {}", platform);
        String sql = platform.getCreateDatabaseSql(model, parameters, true, true);
        if (!outputFile.exists()) {
            try {
                boolean res = outputFile.createNewFile();
            } catch (IOException e) {
                throw new DdlUtilsTaskException(e);
            }
        }
        try (FileWriter fw = new FileWriter(outputFile, StandardCharsets.UTF_8)) {
            fw.write(sql);
        } catch (IOException e) {
            throw new DdlUtilsTaskException(e);
        }
    }

    @Override
    public boolean isRequiringModel() {
        return true;
    }
}
