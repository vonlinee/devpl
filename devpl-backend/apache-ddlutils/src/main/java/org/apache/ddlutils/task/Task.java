package org.apache.ddlutils.task;

import org.apache.ddlutils.DdlUtilsTaskException;

public abstract class Task {

    public abstract void execute() throws DdlUtilsTaskException;

    public void log(String message, int level) {

    }

    public final void log(String msg, int level, Object... args) {
        msg = String.format(msg, args);
        log(msg, level);
    }
}
