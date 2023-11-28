package io.devpl.backend.utils;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class JarUtils {

    public static void main(String[] args) {

        String filepath = "D:\\Temp\\drivers\\mysql\\8.0.18\\mysql-connector-java-8.0.18.jar";

        File savedJarFile = new File(filepath);

        try (JarFile file = new JarFile(savedJarFile)) {
            Manifest manifest = file.getManifest();
            Runtime.Version version = file.getVersion();
            String comment = file.getComment();
            String name = file.getName();
            Enumeration<JarEntry> jarEntries = file.entries();

            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();

                if (jarEntry.isDirectory()) {
                    continue;
                }
                if (!jarEntry.getName().endsWith(".class")) {
                    continue;
                }

                String realName = jarEntry.getRealName();

                System.out.println(realName + "\t" + jarEntry.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
