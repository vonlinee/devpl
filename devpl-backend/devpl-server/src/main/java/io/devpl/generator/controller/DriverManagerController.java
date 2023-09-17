package io.devpl.generator.controller;

import io.devpl.generator.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 数据库驱动管理
 */
@Slf4j
@Controller
@ResponseBody
@RequestMapping("/api/jdbc/driver")
public class DriverManagerController {

    /**
     * 上传驱动jar包
     * @param file 查询参数
     * @return 分页查询结果
     */
    @PostMapping(value = "/upload")
    public Result<?> uploadDriver(MultipartFile file) {
        Path path = Path.of("D:/Temp/jarfiles", file.getOriginalFilename());
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        findClass(path.toFile(), Objects::isNull);

        return Result.ok(String.valueOf(file));
    }

    public void findClass(File file, Predicate<JarEntry> condition) {
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if (jarEntry.isDirectory()) {
                    continue;
                }
                if (!jarEntry.getName().endsWith(".class")) {
                    continue;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 已注册的驱动类名
     * @return 驱动类名列表
     */
    @GetMapping(value = "/list/registered")
    public Result<List<String>> listRegisteredDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        List<String> classNames = new ArrayList<>();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            Class<? extends Driver> clazz = driver.getClass();
            classNames.add(clazz.getName());
        }
        return Result.ok(classNames);
    }
}
