package io.devpl.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.*;
import java.nio.charset.StandardCharsets;

@EnableScheduling
@SpringBootApplication
public class DevplMain8088 {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplicationBuilder()
            .bannerMode(Banner.Mode.OFF)
            .sources(DevplMain8088.class)
            .main(DevplMain8088.class)
            .build();
        app.run(args);

        final String frontEndRootDir = new File("").getAbsolutePath() + "\\devpl-webui";
        System.out.println("cd " + frontEndRootDir + " && npm run dev");
        System.out.println("cd " + frontEndRootDir + " ; npm run dev");
    }
}
