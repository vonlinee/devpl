package io.devpl.generator.mybatis;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * https://blog.csdn.net/u011679955/article/details/93341605
 */
public class CustomEntityResolver implements EntityResolver {

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        return null;
    }
}
