package io.devpl.generator.controller;

import io.devpl.generator.common.utils.Result;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * 测试用
 */
@Controller
@RequestMapping(value = "/api/test")
public class TestController {

    ExtensionLoader<MyPlugin> loader = new ExtensionLoader<>();

    @PostMapping(value = "/jar", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<?> test1(MultipartFile jarFile, Map<String, Object> map) {
        //  String jarFilePath, String jarFileSavePath, String classPath, MultipartFile jarFile

        String jarFilePath = (String) map.get("jarFilePath");
        String classPath = (String) map.get("classPath");
        String jarFileSavePath = (String) map.get("jarFileSavePath");

        Assert.hasText(jarFileSavePath, "保存路径不能为空");

        try {
            MyPlugin plugin = loader.LoadClass(jarFilePath, classPath, MyPlugin.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("无法加载jar包", e);
        }

        File savedJarFile = new File(jarFileSavePath);

        try (JarFile file = new JarFile(savedJarFile)) {
            Manifest manifest = file.getManifest();
            Runtime.Version version = file.getVersion();
            String comment = file.getComment();
            String name = file.getName();
            Enumeration<JarEntry> jarEntries = file.entries();

            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();

                String realName = jarEntry.getRealName();

                System.out.println(realName);
            }
            return Result.ok();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
