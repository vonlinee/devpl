package org.apache.ddlutils.io;

public interface XmlWriter {
    /**
     * Returned the encoding used by this xml writer.
     *
     * @return The encoding
     */
    String getEncoding();

    /**
     * Rethrows the given exception, wrapped in a {@link DdlUtilsXMLException}. This
     * method allows subclasses to throw their own subclasses of this exception.
     *
     * @param baseEx The original exception
     * @throws DdlUtilsXMLException The wrapped exception
     */
    void throwException(Exception baseEx) throws DdlUtilsXMLException;

    /**
     * Determines whether the output shall be pretty-printed.
     *
     * @return <code>true</code> if the output is pretty-printed
     */
    boolean isPrettyPrinting();

    /**
     * Specifies whether the output shall be pretty-printed.
     *
     * @param prettyPrinting <code>true</code> if the output is pretty-printed
     */
    void setPrettyPrinting(boolean prettyPrinting);

    /**
     * Sets the default namespace.
     *
     * @param uri The namespace uri
     */
    void setDefaultNamespace(String uri) throws DdlUtilsXMLException;

    /**
     * Prints a newline if we're pretty-printing.
     */
    void printlnIfPrettyPrinting() throws DdlUtilsXMLException;

    /**
     * Prints the indentation if we're pretty-printing.
     *
     * @param level The indentation level
     */
    void indentIfPrettyPrinting(int level) throws DdlUtilsXMLException;

    /**
     * Writes the start of the XML document, i.e. the "<?xml?>" section and the start of the
     * root node.
     */
    void writeDocumentStart() throws DdlUtilsXMLException;

    /**
     * Writes the end of the XML document, i.e. end of the root node.
     */
    void writeDocumentEnd() throws DdlUtilsXMLException;

    /**
     * Writes a xmlns attribute to the stream.
     *
     * @param prefix       The prefix for the namespace, use <code>null</code> or an empty string for the default namespace
     * @param namespaceUri The namespace uri, can be <code>null</code>
     */
    void writeNamespace(String prefix, String namespaceUri) throws DdlUtilsXMLException;

    /**
     * Writes the start of the indicated XML element.
     *
     * @param namespaceUri The namespace uri, can be <code>null</code>
     * @param localPart    The local part of the element's qname
     */
    void writeElementStart(String namespaceUri, String localPart) throws DdlUtilsXMLException;

    /**
     * Writes the end of the current XML element.
     */
    void writeElementEnd() throws DdlUtilsXMLException;

    /**
     * Writes an XML attribute.
     *
     * @param namespaceUri The namespace uri, can be <code>null</code>
     * @param localPart    The local part of the attribute's qname
     * @param value        The value; if <code>null</code> then no attribute is written
     */
    void writeAttribute(String namespaceUri, String localPart, String value) throws DdlUtilsXMLException;

    /**
     * Writes a CDATA segment.
     *
     * @param data The data to write
     */
    void writeCData(String data) throws DdlUtilsXMLException;

    /**
     * Writes a text segment.
     *
     * @param data The data to write
     */
    void writeCharacters(String data) throws DdlUtilsXMLException;
}
