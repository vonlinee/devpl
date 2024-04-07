package io.devpl.codegen.generator.config;

import java.io.InputStream;

import io.devpl.codegen.generator.XmlConstants;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class ParserEntityResolver implements EntityResolver {

    public ParserEntityResolver() {
        super();
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        if (XmlConstants.GENERATOR_CONFIG_PUBLIC_ID
            .equalsIgnoreCase(publicId)) {
            InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(
                    "codegen-config.dtd"); //$NON-NLS-1$
            return new InputSource(is);
        }
        return null;
    }
}
