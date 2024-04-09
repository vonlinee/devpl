package io.devpl.codegen.generator.config;

import io.devpl.codegen.generator.XmlConstants;
import io.devpl.codegen.generator.config.Configuration;
import io.devpl.codegen.generator.config.ParserEntityResolver;
import io.devpl.codegen.generator.config.xml.GeneratorConfigurationParser;
import io.devpl.codegen.generator.config.xml.ParserErrorHandler;
import io.devpl.codegen.generator.config.xml.XMLParserException;
import io.devpl.codegen.util.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigurationParser {

    private final List<String> warnings;
    private final List<String> parseErrors;
    private final Properties extraProperties;

    public ConfigurationParser(List<String> warnings) {
        this(null, warnings);
    }

    /**
     * This constructor accepts a properties object which may be used to specify
     * an additional property set.  Typically, this property set will be Ant or Maven properties
     * specified in the build.xml file or the POM.
     *
     * <p>If there are name collisions between the different property sets, they will be
     * resolved in this order:
     *
     * <ol>
     *   <li>System properties take highest precedence</li>
     *   <li>Properties specified in the &lt;properties&gt; configuration
     *       element are next</li>
     *   <li>Properties specified in this "extra" property set are
     *       lowest precedence.</li>
     * </ol>
     *
     * @param extraProperties an (optional) set of properties used to resolve property
     *                        references in the configuration file
     * @param warnings        any warnings are added to this array
     */
    public ConfigurationParser(Properties extraProperties, List<String> warnings) {
        super();
        this.extraProperties = extraProperties;
        this.warnings = warnings == null ? new ArrayList<>() : warnings;
        this.parseErrors = new ArrayList<>();
    }

    public Configuration parseConfiguration(File inputFile) throws IOException,
        XMLParserException {
        FileReader fr = new FileReader(inputFile);
        return parseConfiguration(fr);
    }

    public Configuration parseConfiguration(Reader reader) throws IOException,
        XMLParserException {
        InputSource is = new InputSource(reader);
        return parseConfiguration(is);
    }

    public Configuration parseConfiguration(InputStream inputStream)
        throws IOException, XMLParserException {
        InputSource is = new InputSource(inputStream);
        return parseConfiguration(is);
    }

    private Configuration parseConfiguration(InputSource inputSource)
        throws IOException, XMLParserException {
        parseErrors.clear();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        factory.setValidating(true);

        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new ParserEntityResolver());

            ParserErrorHandler handler = new ParserErrorHandler(warnings, parseErrors);
            builder.setErrorHandler(handler);

            Document document = null;
            try {
                document = builder.parse(inputSource);
            } catch (SAXParseException e) {
                throw new XMLParserException(parseErrors);
            } catch (SAXException e) {
                if (e.getException() == null) {
                    parseErrors.add(e.getMessage());
                } else {
                    parseErrors.add(e.getException().getMessage());
                }
            }

            if (document == null || !parseErrors.isEmpty()) {
                throw new XMLParserException(parseErrors);
            }

            Configuration config;
            Element rootNode = document.getDocumentElement();
            DocumentType docType = document.getDoctype();
            if (rootNode.getNodeType() == Node.ELEMENT_NODE
                && docType.getPublicId().equals(
                XmlConstants.GENERATOR_CONFIG_PUBLIC_ID)) {
                config = parseGeneratorConfiguration(rootNode);
            } else {
                throw new XMLParserException(Messages.getString("RuntimeError.5"));
            }

            if (!parseErrors.isEmpty()) {
                throw new XMLParserException(parseErrors);
            }

            return config;
        } catch (ParserConfigurationException e) {
            parseErrors.add(e.getMessage());
            throw new XMLParserException(parseErrors);
        }
    }

    private Configuration parseGeneratorConfiguration(Element rootNode)
        throws XMLParserException {
        GeneratorConfigurationParser parser = new GeneratorConfigurationParser(
            extraProperties);
        return parser.parseConfiguration(rootNode);
    }
}
