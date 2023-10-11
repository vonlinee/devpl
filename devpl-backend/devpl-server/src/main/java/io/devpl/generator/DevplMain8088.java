package io.devpl.generator;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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
        ConfigurableApplicationContext context = app.run(args);

        String absolutePath = new File(new File("").getAbsoluteFile().getParent(), "devpl-web-ui").getAbsolutePath();

        String command = "cd " + absolutePath + " && npm run dev";
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

        try {
            String[] commands = {"cmd", "/c", absolutePath};
            Process process;
            if (isWindows) {
                process = Runtime.getRuntime()
                    .exec(commands);
            } else {
                process = Runtime.getRuntime()
                    .exec(String.format("/bin/sh -c ls %s", absolutePath));
            }

            

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.exit(0);
    }
}
