package io.fxtras.svg.tosvg;

import io.fxtras.svg.tosvg.converters.ConverterDelegate;
import io.fxtras.svg.tosvg.xml.XMLNodeUtilities;
import io.fxtras.svg.tosvg.xml.XMLRoot;
import javafx.scene.Node;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @since 1.0
 */
public class SVGConverter {
    private ConverterDelegate delegate = null;

    public SVGConverter() {
        delegate = new ConverterDelegate();
    }

    /**
     * Return the ConverterDelegate.
     *
     * @return the ConverterDelegate
     */
    public ConverterDelegate getConverterDelegate() {
        return delegate;
    }

    /**
     * Convert a JavaFX Node hierarchy.
     *
     * @param root the root Node
     * @param file the file
     */
    public void convert(Node root, File file) throws IOException {
        convert(root, file, new ConverterParameters());
    }

    /**
     * Convert a JavaFX Node hierarchy.
     *
     * @param root   the root Node
     * @param file   the file
     * @param params the conversion parameters
     */
    public void convert(Node root, File file, ConverterParameters params) throws IOException {
        delegate.setSVGFile(file);
        XMLRoot xmlRoot = new XMLRoot("svg");
        if (params.width > 0) {
            xmlRoot.addAttribute("width", params.width);
        } else {
            double width = root.getBoundsInLocal().getWidth();
            xmlRoot.addAttribute("width", width);
        }
        if (params.height > 0) {
            xmlRoot.addAttribute("height", params.height);
        } else {
            double height = root.getBoundsInLocal().getHeight();
            xmlRoot.addAttribute("height", height);
        }
        delegate.convertRoot(root, xmlRoot);

        XMLNodeUtilities.print(xmlRoot, 2, file);
    }

    /**
     * Convert a JavaFX Node hierarchy.
     *
     * @param root the root Node
     * @param url  the url
     */
    public void convert(Node root, URL url) throws IOException {
        convert(root, url, new ConverterParameters());
    }

    /**
     * Convert a JavaFX Node hierarchy.
     *
     * @param root   the root Node
     * @param url    the url
     * @param params the conversion parameters
     */
    public void convert(Node root, URL url, ConverterParameters params) throws IOException {
        delegate.setSVGFile(new File(url.getFile()));
        XMLRoot xmlRoot = new XMLRoot("svg");
        if (params.width > 0) {
            xmlRoot.addAttribute("width", params.width);
        }
        if (params.height > 0) {
            xmlRoot.addAttribute("height", params.height);
        }
        delegate.convertRoot(root, xmlRoot);

        XMLNodeUtilities.print(xmlRoot, 2, url);
    }
}
