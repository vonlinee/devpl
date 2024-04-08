package org.apache.ddlutils.io;


import java.util.List;

/**
 * Base class providing helper functions for writing model elements to XML.
 */
public abstract class ModelXmlWriter {
    protected void writeText(DataWriter writer, String value, boolean isBase64Encoded) {
        if (isBase64Encoded) {
            writer.writeAttribute(null, "base64", "true");
            writer.writeCharacters(value);
        } else {
            List<Integer> cutPoints = XMLUtils.findCDataCutPoints(value);

            // if the content contains special characters, we have to apply base64 encoding to it
            assert cutPoints != null;
            if (cutPoints.isEmpty()) {
                writer.writeCharacters(value);
            } else {
                int lastPos = 0;
                for (int curPos : cutPoints) {
                    writer.writeCData(value.substring(lastPos, curPos));
                    lastPos = curPos;
                }
                if (lastPos < value.length()) {
                    writer.writeCData(value.substring(lastPos));
                }
            }
        }
    }
}
