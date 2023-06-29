package io.devpl.generator.service;

import io.devpl.generator.domain.FileNode;

import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成
 */
public interface CodeGenService {

    void downloadCode(Long tableId, ZipOutputStream zip);

    void generatorCode(Long tableId);

    List<FileNode> getFileTree(String workPath);

    String getFileContent(String path);
}
