package io.devpl.codegen.generator;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * XML 文件
 */
public class GeneratedXmlFile extends GeneratedFile {

    @Override
    public TargetFile getFileType() {
        return null;
    }

    @Override
    public void write(Writer writer, Charset charset) throws IOException {

    }

    @Override
    public String getFormattedContent() {
        return null;
    }

    @Override
    public String getAbsolutePath() {
        return null;
    }

    @Override
    public final String getExtension() {
        return "xml";
    }
}
