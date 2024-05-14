package org.apache.ddlutils.io;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * Helper class that writes XML data with or without pretty printing.
 */
public class PrettyPrintingXmlWriter implements XmlWriter {
    /**
     * The indentation string.
     */
    private static final String INDENT_STRING = "  ";
    /**
     * The output encoding.
     */
    private final String _encoding;
    /**
     * The xml writer.
     */
    private XMLStreamWriter _writer;
    /**
     * Whether we're pretty-printing.
     */
    private boolean _prettyPrinting = true;

    /**
     * Creates a xml writer instance using UTF-8 encoding.
     *
     * @param output The target to write the data XML to
     */
    public PrettyPrintingXmlWriter(OutputStream output) throws DdlUtilsXMLException {
        this(output, StandardCharsets.UTF_8.name());
    }

    /**
     * Creates a xml writer instance.
     *
     * @param output   The target to write the data XML to
     * @param encoding The encoding of the XML file
     */
    public PrettyPrintingXmlWriter(OutputStream output, String encoding) throws DdlUtilsXMLException {
        BufferedOutputStream bufferedOutput;
        if (output instanceof BufferedOutputStream) {
            bufferedOutput = (BufferedOutputStream) output;
        } else {
            bufferedOutput = new BufferedOutputStream(output);
        }
        _encoding = encoding == null || encoding.isEmpty() ? "UTF-8" : encoding;
        try {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            _writer = factory.createXMLStreamWriter(bufferedOutput, _encoding);
        } catch (XMLStreamException ex) {
            throwException(ex);
        }
    }

    /**
     * Creates a xml writer instance using the specified writer. Note that the writer
     * needs to be configured using the specified encoding.
     *
     * @param output   The target to write the data XML to
     * @param encoding The encoding of the writer
     */
    public PrettyPrintingXmlWriter(Writer output, String encoding) throws DdlUtilsXMLException {
        BufferedWriter bufferedWriter;
        if (output instanceof BufferedWriter) {
            bufferedWriter = (BufferedWriter) output;
        } else {
            bufferedWriter = new BufferedWriter(output);
        }
        _encoding = encoding;
        try {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            _writer = factory.createXMLStreamWriter(bufferedWriter);
        } catch (XMLStreamException ex) {
            throwException(ex);
        }
    }

    /**
     * Returnd the encoding used by this xml writer.
     *
     * @return The encoding
     */
    @Override
    public String getEncoding() {
        return _encoding;
    }

    /**
     * Rethrows the given exception, wrapped in a {@link DdlUtilsXMLException}. This
     * method allows subclasses to throw their own subclasses of this exception.
     *
     * @param baseEx The original exception
     * @throws DdlUtilsXMLException The wrapped exception
     */
    @Override
    public void throwException(Exception baseEx) throws DdlUtilsXMLException {
        throw new DdlUtilsXMLException(baseEx);
    }

    /**
     * Determines whether the output shall be pretty-printed.
     *
     * @return <code>true</code> if the output is pretty-printed
     */
    @Override
    public boolean isPrettyPrinting() {
        return _prettyPrinting;
    }

    /**
     * Specifies whether the output shall be pretty-printed.
     *
     * @param prettyPrinting <code>true</code> if the output is pretty-printed
     */
    @Override
    public void setPrettyPrinting(boolean prettyPrinting) {
        _prettyPrinting = prettyPrinting;
    }

    /**
     * Sets the default namespace.
     *
     * @param uri The namespace uri
     */
    @Override
    public void setDefaultNamespace(String uri) throws DdlUtilsXMLException {
        try {
            _writer.setDefaultNamespace(uri);
        } catch (XMLStreamException ex) {
            throwException(ex);
        }
    }

    /**
     * Prints a newline if we're pretty-printing.
     */
    @Override
    public void printlnIfPrettyPrinting() throws DdlUtilsXMLException {
        if (_prettyPrinting) {
            try {
                _writer.writeCharacters("\n");
            } catch (XMLStreamException ex) {
                throwException(ex);
            }
        }
    }

    /**
     * Prints the indentation if we're pretty-printing.
     *
     * @param level The indentation level
     */
    @Override
    public void indentIfPrettyPrinting(int level) throws DdlUtilsXMLException {
        if (_prettyPrinting) {
            try {
                for (int idx = 0; idx < level; idx++) {
                    _writer.writeCharacters(INDENT_STRING);
                }
            } catch (XMLStreamException ex) {
                throwException(ex);
            }
        }
    }

    /**
     * Writes the start of the XML document, i.e. the "<?xml?>" section and the start of the
     * root node.
     */
    @Override
    public void writeDocumentStart() throws DdlUtilsXMLException {
        try {
            _writer.writeStartDocument(_encoding, "1.0");
            printlnIfPrettyPrinting();
        } catch (XMLStreamException ex) {
            throwException(ex);
        }
    }

    /**
     * Writes the end of the XML document, i.e. end of the root node.
     */
    @Override
    public void writeDocumentEnd() throws DdlUtilsXMLException {
        try {
            _writer.writeEndDocument();
            _writer.flush();
            _writer.close();
        } catch (XMLStreamException ex) {
            throwException(ex);
        }
    }

    /**
     * Writes a xmlns attribute to the stream.
     *
     * @param prefix       The prefix for the namespace, use <code>null</code> or an empty string for the default namespace
     * @param namespaceUri The namespace uri, can be <code>null</code>
     */
    @Override
    public void writeNamespace(String prefix, String namespaceUri) throws DdlUtilsXMLException {
        try {
            if ((prefix == null) || (prefix.isEmpty())) {
                _writer.writeDefaultNamespace(namespaceUri);
            } else {
                _writer.writeNamespace(prefix, namespaceUri);
            }
        } catch (XMLStreamException ex) {
            throwException(ex);
        }
    }

    /**
     * Writes the start of the indicated XML element.
     *
     * @param namespaceUri The namespace uri, can be <code>null</code>
     * @param localPart    The local part of the element's qname
     */
    @Override
    public void writeElementStart(String namespaceUri, String localPart) throws DdlUtilsXMLException {
        try {
            if (namespaceUri == null) {
                _writer.writeStartElement(localPart);
            } else {
                _writer.writeStartElement(namespaceUri, localPart);
            }
        } catch (XMLStreamException ex) {
            throwException(ex);
        }
    }

    /**
     * Writes the end of the current XML element.
     */
    @Override
    public void writeElementEnd() throws DdlUtilsXMLException {
        try {
            _writer.writeEndElement();
        } catch (XMLStreamException ex) {
            throwException(ex);
        }
    }

    /**
     * Writes an XML attribute.
     *
     * @param namespaceUri The namespace uri, can be <code>null</code>
     * @param localPart    The local part of the attribute's qname
     * @param value        The value; if <code>null</code> then no attribute is written
     */
    @Override
    public void writeAttribute(String namespaceUri, String localPart, String value) throws DdlUtilsXMLException {
        if (value != null) {
            try {
                if (namespaceUri == null) {
                    _writer.writeAttribute(localPart, value);
                } else {
                    _writer.writeAttribute(namespaceUri, localPart, value);
                }
            } catch (XMLStreamException ex) {
                throwException(ex);
            }
        }
    }

    /**
     * Writes a CDATA segment.
     *
     * @param data The data to write
     */
    @Override
    public void writeCData(String data) throws DdlUtilsXMLException {
        if (data != null) {
            try {
                _writer.writeCData(data);
            } catch (XMLStreamException ex) {
                throwException(ex);
            }
        }
    }

    /**
     * Writes a text segment.
     *
     * @param data The data to write
     */
    @Override
    public void writeCharacters(String data) throws DdlUtilsXMLException {
        if (data != null) {
            try {
                _writer.writeCharacters(data);
            } catch (XMLStreamException ex) {
                throwException(ex);
            }
        }
    }
}
