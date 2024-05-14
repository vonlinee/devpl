package org.apache.ddlutils.task;

import java.io.File;

public final class FileSet {

    private final File dir;
    private final FileScanner scanner;

    public FileSet(File dir, FileScanner scanner) {
        this.dir = dir;
        this.scanner = scanner;
    }

    public File getDir() {
        return dir;
    }

    public FileScanner getDirectoryScanner() {
        return scanner;
    }

    public String[] getIncludedFiles() {
        return scanner.getIncludedFiles();
    }
}
