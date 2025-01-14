package org.apache.ddlutils.task;

import java.io.File;

/**
 * An interface used to describe the actions required of any type of
 * directory scanner.
 */
public interface FileScanner {
    /**
     * Adds default exclusions to the current exclusions set.
     */
    void addDefaultExcludes();

    /**
     * Returns the base directory to be scanned.
     * This is the directory which is scanned recursively.
     *
     * @return the base directory to be scanned
     */
    File getBaseDir();

    /**
     * Returns the names of the directories which matched at least one of the
     * include patterns and at least one of the exclude patterns.
     * The names are relative to the base directory.
     *
     * @return the names of the directories which matched at least one of the
     * include patterns and at least one of the exclude patterns.
     */
    String[] getExcludedDirectories();

    /**
     * Returns the names of the files which matched at least one of the
     * include patterns and at least one of the exclude patterns.
     * The names are relative to the base directory.
     *
     * @return the names of the files which matched at least one of the
     * include patterns and at least one of the exclude patterns.
     */
    String[] getExcludedFiles();

    /**
     * Returns the names of the directories which matched at least one of the
     * include patterns and none of the exclude patterns.
     * The names are relative to the base directory.
     *
     * @return the names of the directories which matched at least one of the
     * include patterns and none of the exclude patterns.
     */
    String[] getIncludedDirectories();

    /**
     * Returns the names of the files which matched at least one of the
     * include patterns and none of the exclude patterns.
     * The names are relative to the base directory.
     *
     * @return the names of the files which matched at least one of the
     * include patterns and none of the exclude patterns.
     */
    String[] getIncludedFiles();

    /**
     * Returns the names of the directories which matched none of the include
     * patterns. The names are relative to the base directory.
     *
     * @return the names of the directories which matched none of the include
     * patterns.
     */
    String[] getNotIncludedDirectories();

    /**
     * Returns the names of the files which matched none of the include
     * patterns. The names are relative to the base directory.
     *
     * @return the names of the files which matched none of the include
     * patterns.
     */
    String[] getNotIncludedFiles();

    /**
     * Scans the base directory for files which match at least one include
     * pattern and don't match any exclude patterns.
     *
     * @throws IllegalStateException if the base directory was set
     *                               incorrectly (i.e. if it is <code>null</code>, doesn't exist,
     *                               or isn't a directory).
     */
    void scan() throws IllegalStateException;

    /**
     * Sets the base directory to be scanned. This is the directory which is
     * scanned recursively. All '/' and '\' characters should be replaced by
     * <code>File.separatorChar</code>, so the separator used need not match
     * <code>File.separatorChar</code>.
     *
     * @param basedir The base directory to scan.
     *                Must not be <code>null</code>.
     */
    void setBasedir(String basedir);

    /**
     * Sets the base directory to be scanned. This is the directory which is
     * scanned recursively.
     *
     * @param basedir The base directory for scanning.
     *                Should not be <code>null</code>.
     */
    void setBasedir(File basedir);

    /**
     * Sets the list of exclude patterns to use.
     *
     * @param excludes A list of exclude patterns.
     *                 May be <code>null</code>, indicating that no files
     *                 should be excluded. If a non-<code>null</code> list is
     *                 given, all elements must be non-<code>null</code>.
     */
    void setExcludes(String[] excludes);

    /**
     * Sets the list of include patterns to use.
     *
     * @param includes A list of include patterns.
     *                 May be <code>null</code>, indicating that all files
     *                 should be included. If a non-<code>null</code>
     *                 list is given, all elements must be
     *                 non-<code>null</code>.
     */
    void setIncludes(String[] includes);

    /**
     * Sets whether the file system should be regarded as case-sensitive.
     *
     * @param isCaseSensitive whether the file system should be
     *                        regarded as a case-sensitive one
     */
    void setCaseSensitive(boolean isCaseSensitive);
}
