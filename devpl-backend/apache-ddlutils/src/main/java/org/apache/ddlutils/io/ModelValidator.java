package org.apache.ddlutils.io;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 * Helper class that validates a given document against the DdlUtils schema.
 */
public class ModelValidator {
    /**
     * Validates the given xml document using the Java XML validation framework.
     *
     * @param source The source object for the xml document
     * @throws DdlUtilsXMLException If the document could not be validated
     */
    public void validate(Source source) throws DdlUtilsXMLException {
        try {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = factory.newSchema(new StreamSource(getClass().getResourceAsStream("/database.xsd")));
            Validator validator = schema.newValidator();
            validator.validate(source);
        } catch (Exception ex) {
            throw new DdlUtilsXMLException(ex);
        }
    }
}
