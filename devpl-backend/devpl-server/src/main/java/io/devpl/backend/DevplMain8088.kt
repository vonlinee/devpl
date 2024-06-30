package io.devpl.backend

import lombok.extern.slf4j.Slf4j
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.core.env.Environment
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import java.net.InetAddress
import java.net.UnknownHostException

@Slf4j
@EnableAsync
@EnableAspectJAutoProxy
@EnableScheduling
@SpringBootApplication
class DevplMain8088 : SpringBootServletInitializer() {
    /**
     * SpringBoot打WAR包部署tomcat
     */
    @Override
    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder {
        return builder.sources(DevplMain8088::class.java)
    }
}

fun println(context: ConfigurableApplicationContext) {
    val env: Environment = context.environment
    try {
        val ip = InetAddress.getLocalHost().hostAddress
        val port = env.getProperty("server.port")
        val contextPath = env.getProperty("server.servlet.context-path", "")
        println(
            """
            Application Devpl is running! Access URLs:
            > Local: 		http://localhost:${port}${contextPath}
            > External: 	https://${ip}:${ip}${contextPath}
        """.trimIndent()
        )
    } catch (e: UnknownHostException) {
        throw RuntimeException(e)
    }
}

fun main(args: Array<String>) {
    val context: ConfigurableApplicationContext =
        SpringApplicationBuilder()
            .bannerMode(Banner.Mode.OFF)
            .sources(DevplMain8088::class.java)
            .main(DevplMain8088::class.java)
            .build()
            .run(*args)
    println(context)
}
