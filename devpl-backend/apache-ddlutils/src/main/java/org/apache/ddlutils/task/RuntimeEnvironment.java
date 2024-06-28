package org.apache.ddlutils.task;

public interface RuntimeEnvironment {

    /**
     * classloader runtime environment
     *
     * @param parent      parent classloader
     * @param parentFirst parent classloader first
     * @return ClassLoader
     */
    ClassLoader getClassloader(ClassLoader parent, final boolean parentFirst);
}
