package io.devpl.backend.utils;

import io.devpl.backend.domain.ProjectModule;
import io.devpl.backend.tools.MavenProjectParser;

import java.io.File;

public class ProjectUtils {

    public static ProjectModule parse(File entryFile) {
        if (entryFile.isDirectory()) {
            File[] files = entryFile.listFiles(f -> f.getName().equals("pom.xml") || f.getName().equals("build.grade"));
            if (files == null || files.length == 0) {
                return null;
            }
            entryFile = files[0];
        }

        if ("pom.xml".equals(entryFile.getName())) {
            return new MavenProjectParser().analyse(entryFile);
        }
        return null;
    }
}
