package io.devpl.sdk.io;

import java.nio.file.Path;

/**
 * @see java.io.FileFilter
 */
@FunctionalInterface
public interface PathFilter {

    boolean accept(Path path);
}
