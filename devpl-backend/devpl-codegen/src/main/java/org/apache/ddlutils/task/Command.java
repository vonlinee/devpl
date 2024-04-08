package org.apache.ddlutils.task;

import org.apache.ddlutils.model.Database;
import org.apache.tools.ant.BuildException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for commands that work with a model.
 */
public abstract class Command {
    /**
     * The log.
     */
    protected final Logger _log = LoggerFactory.getLogger(getClass());

    /**
     * Whether to stop execution upon an error.
     */
    private boolean _failOnError = true;

    /**
     * Determines whether the command execution will be stopped upon an error.
     * Default value is <code>true</code>.
     *
     * @return <code>true</code> if the execution stops in case of an error
     */
    public boolean isFailOnError() {
        return _failOnError;
    }

    /**
     * Specifies whether the execution shall stop if an error has occurred during the task runs.
     *
     * @param failOnError <code>true</code> if the execution shall stop in case of an error
     *                    By default, execution will be stopped when an error is encountered.
     */
    public void setFailOnError(boolean failOnError) {
        _failOnError = failOnError;
    }

    /**
     * Handles the given exception according to the fail-on-error setting by either
     * re-throwing it (wrapped in a build exception) or only logging it.
     *
     * @param ex  The exception
     * @param msg The message to use unless this the exception is rethrown, and it is
     *            already a build exception
     */
    protected void handleException(Exception ex, String msg) throws BuildException {
        if (isFailOnError()) {
            if (ex instanceof BuildException) {
                throw (BuildException) ex;
            } else {
                throw new BuildException(msg, ex);
            }
        } else {
            _log.error(msg, ex);
        }
    }

    /**
     * Specifies whether this command requires a model, i.e. whether the second
     * argument in {@link #execute(DatabaseTaskBase, Database)} cannot be <code>null</code>.
     *
     * @return <code>true</code> if this command requires a model
     */
    public boolean isRequiringModel() {
        return true;
    }

    /**
     * Executes this command.
     *
     * @param task  The executing task
     * @param model The database model
     */
    public abstract void execute(DatabaseTaskBase task, Database model) throws BuildException;
}
