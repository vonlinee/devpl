package io.devpl.codegen.generator.file;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * XML 文件
 *
 * @see org.mybatis.generator.api.GeneratedXmlFile
 */
public class GeneratedXmlFile extends GeneratedFile {

    TargetFile targetFile;

    @Override
    public TargetFile getFileType() {
        return targetFile;
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
        return "";
    }

    @Override
    public final String getExtension() {
        return "xml";
    }
}
