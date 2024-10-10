package org.apache.ddlutils.task;

import org.apache.ddlutils.DdlUtilsTaskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Task {

    public static final int MSG_ERR = 1;
    public static final int MSG_INFO = 2;
    public static final int MSG_WARN = 3;

    /**
     * the environment when task is executing.
     */
    protected TaskRuntimeEnvironment environment;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public abstract void execute() throws DdlUtilsTaskException;

    public final void log(String message, int level) {
        if (level == Task.MSG_INFO) {
            logger.info(message);
        } else if (level == Task.MSG_ERR) {
            logger.error(message);
        } else if (level == Task.MSG_WARN) {
            logger.warn(message);
        }
    }

    public void setRuntimeEnvironment(TaskRuntimeEnvironment environment) {
        this.environment = environment;
    }

    public TaskRuntimeEnvironment getRuntimeEnvironment() {
        return environment;
    }

    public final void log(String msg, int level, Object... args) {
        msg = String.format(msg, args);
        log(msg, level);
    }
}
