package io.devpl.sdk.io;

import io.devpl.sdk.lang.RuntimeIOException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

/**
 * see Apache commons-io issue
 * <a href="https://issues.apache.org/jira/browse/IO-181">LineIterator should implement Iterable</a>
 */
public class IterableFile implements Iterable<String> {

    private final File file;

    public IterableFile(File file) {
        this.file = file;
    }

    @NotNull
    @Override
    public Iterator<String> iterator() {
        try {
            return new LineIterator(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeIOException(e);
        }
    }
}
