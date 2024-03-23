package io.devpl.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@EnableAsync
@EnableAspectJAutoProxy
@EnableScheduling
@SpringBootApplication
public class DevplMain8088 extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplicationBuilder()
            .bannerMode(Banner.Mode.OFF)
            .sources(DevplMain8088.class)
            .main(DevplMain8088.class)
            .build();
        ConfigurableApplicationContext context = app.run(args);

        println(context);
    }

    public static void println(ConfigurableApplicationContext context) {
        Environment env = context.getEnvironment();
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            String port = env.getProperty("server.port");
            String contextPath = env.getProperty("server.servlet.context-path", "");
            System.out.printf("""
                ----------------------------------------------------------
                Application Devpl is running! Access URLs:
                > Local: 		http://localhost:%s%s
                > External: 	http://%s:%s%s
                ----------------------------------------------------------
                %n""", port, contextPath, ip, port, contextPath);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * SpringBoot打WAR包部署tomcat
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DevplMain8088.class);
    }
}
