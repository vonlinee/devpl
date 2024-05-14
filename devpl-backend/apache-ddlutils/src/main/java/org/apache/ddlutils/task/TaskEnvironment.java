package org.apache.ddlutils.task;

public interface TaskEnvironment {

    ClassLoader getClassloader(ClassLoader parent, final boolean parentFirst);
}
