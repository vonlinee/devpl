package io.devpl.backend.extension.compiler;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

public class SourceFileJavaFileObject extends SimpleJavaFileObject {
    /**
     * Construct a SimpleJavaFileObject of the given kind and with the
     * given URI.
     *
     * @param uri  the URI for this file object
     * @param kind the kind of this file object
     */
    protected SourceFileJavaFileObject(URI uri, Kind kind) {
        super(uri, kind);
    }
}
