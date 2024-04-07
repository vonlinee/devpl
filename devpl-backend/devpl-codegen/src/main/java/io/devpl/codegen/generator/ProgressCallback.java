package io.devpl.codegen.generator;

/**
 * This interface can be implemented to return progress information from the file generation process.
 *
 * <p>During the execution of code generation, there are three main operations:
 * database introspection, code generation based on the results of
 * introspection, and then merging/saving generated files. Methods
 * in this interface accordingly and in this order:
 * <ol>
 * <li>introspectionStarted(int)</li>
 * <li>(Repeatedly) startTask(String)</li>
 * <li>generationStarted(int)</li>
 * <li>(Repeatedly) startTask(String)</li>
 * <li>saveStarted(int)</li>
 * <li>(Repeatedly) startTask(String)</li>
 * <li>done()</li>
 * </ol>
 *
 * <p>Periodically, the <code>checkCancel()</code> method will be called to see if the
 * method should be canceled.
 *
 * <p>For planning purposes, the most common use case will have a ratio of 20%
 * introspection tasks, 40% generation tasks, and 40% save tasks.
 */
public interface ProgressCallback {
    /**
     * Called to note the start of the introspection phase, and to note the
     * maximum number of startTask messages that will be sent for the
     * introspection phase.
     *
     * @param totalTasks the maximum number of times startTask will be called for the
     *                   introspection phase.
     */
    default void introspectionStarted(int totalTasks) {
    }

    /**
     * Called to note the start of the generation phase, and to note the maximum
     * number of startTask messages that will be sent for the generation phase.
     *
     * @param totalTasks the maximum number of times startTask will be called for the
     *                   generation phase.
     */
    default void generationStarted(int totalTasks) {
    }

    /**
     * Called to note the start of the file saving phase, and to note the
     * maximum number of startTask messages that will be sent for the file
     * saving phase.
     *
     * @param totalTasks the maximum number of times startTask will be called for the
     *                   file saving phase.
     */
    default void saveStarted(int totalTasks) {
    }

    /**
     * Called to denote the beginning of a save task.
     *
     * @param taskName a descriptive name of the current work step
     */
    default void startTask(String taskName) {
    }

    /**
     * This method is called when all generated files have been saved.
     */
    default void done() {
    }

    /**
     * The method is called periodically during a long-running method.
     * If the implementation throws <code>InterruptedException</code> then
     * the method will be canceled. Any files that have already been saved will
     * remain on the file system.
     *
     * @throws InterruptedException if the operation should be halted
     */
    default void checkCancel() throws InterruptedException {
    }

    default void terminated() {

    }

    class NoOp implements ProgressCallback {
    }
}
