package io.devpl.sdk.io;


import java.io.File;

/**
 * Keeps track of files awaiting deletion, and deletes them when an associated
 * marker object is reclaimed by the garbage collector.
 * <p>
 * This utility creates a background thread to handle file deletion.
 * Each file to be deleted is registered with a handler object.
 * When the handler object is garbage collected, the file is deleted.
 * <p>
 * In an environment with multiple class loaders (a servlet container, for
 * example), you should consider stopping the background thread if it is no
 * longer needed. This is done by invoking the method
 * {@link #exitWhenFinished}, typically in (javax.servlet.ServletContextListener#contextDestroyed)
 * or similar.
 *
 * @author Noel Bergman
 * @author Martin Cooper
 */
public class FileCleaner {
    /**
     * The instance to use for the deprecated, static methods.
     */
    static final FileCleaningTracker theInstance = new FileCleaningTracker();

    //-----------------------------------------------------------------------

    /**
     * Track the specified file, using the provided marker, deleting the file
     * when the marker instance is garbage collected.
     * The {@link FileDeleteStrategy#NORMAL normal} deletion strategy will be used.
     *
     * @param file   the file to be tracked, not null
     * @param marker the marker object used to track the file, not null
     * @throws NullPointerException if the file is null
     */
    public static void track(File file, Object marker) {
        theInstance.track(file, marker);
    }

    /**
     * Track the specified file, using the provided marker, deleting the file
     * when the marker instance is garbage collected.
     * The speified deletion strategy is used.
     *
     * @param file           the file to be tracked, not null
     * @param marker         the marker object used to track the file, not null
     * @param deleteStrategy the strategy to delete the file, null means normal
     * @throws NullPointerException if the file is null
     */
    public static void track(File file, Object marker, FileDeleteStrategy deleteStrategy) {
        theInstance.track(file, marker, deleteStrategy);
    }

    /**
     * Track the specified file, using the provided marker, deleting the file
     * when the marker instance is garbage collected.
     * The {@link FileDeleteStrategy#NORMAL normal} deletion strategy will be used.
     *
     * @param path   the full path to the file to be tracked, not null
     * @param marker the marker object used to track the file, not null
     * @throws NullPointerException if the path is null
     */
    public static void track(String path, Object marker) {
        theInstance.track(path, marker);
    }

    /**
     * Track the specified file, using the provided marker, deleting the file
     * when the marker instance is garbage collected.
     * The speified deletion strategy is used.
     *
     * @param path           the full path to the file to be tracked, not null
     * @param marker         the marker object used to track the file, not null
     * @param deleteStrategy the strategy to delete the file, null means normal
     * @throws NullPointerException if the path is null
     */
    public static void track(String path, Object marker, FileDeleteStrategy deleteStrategy) {
        theInstance.track(path, marker, deleteStrategy);
    }

    //-----------------------------------------------------------------------

    /**
     * Retrieve the number of files currently being tracked, and therefore
     * awaiting deletion.
     *
     * @return the number of files being tracked
     */
    public static int getTrackCount() {
        return theInstance.getTrackCount();
    }

    /**
     * Call this method to cause the file cleaner thread to terminate when
     * there are no more objects being tracked for deletion.
     * <p>
     * In a simple environment, you don't need this method as the file cleaner
     * thread will simply exit when the JVM exits. In a more complex environment,
     * with multiple class loaders (such as an application server), you should be
     * aware that the file cleaner thread will continue running even if the class
     * loader it was started from terminates. This can consitute a memory leak.
     * <p>
     * For example, suppose that you have developed a web application, which
     * contains the commons-io jar file in your WEB-INF/lib directory. In other
     * words, the FileCleaner class is loaded through the class loader of your
     * web application. If the web application is terminated, but the servlet
     * container is still running, then the file cleaner thread will still exist,
     * posing a memory leak.
     * <p>
     * This method allows the thread to be terminated. Simply call this method
     * in the resource cleanup code, such as {@link javax.servlet.ServletContextListener#contextDestroyed}.
     * One called, no new objects can be tracked by the file cleaner.
     */
    public static synchronized void exitWhenFinished() {
        theInstance.exitWhenFinished();
    }

    /**
     * Returns the singleton instance, which is used by the deprecated, static methods.
     * This is mainly useful for code, which wants to support the new
     * {@link FileCleaningTracker} class while maintain compatibility with the
     * deprecated {@link FileCleaner}.
     */
    public static FileCleaningTracker getInstance() {
        return theInstance;
    }
}
