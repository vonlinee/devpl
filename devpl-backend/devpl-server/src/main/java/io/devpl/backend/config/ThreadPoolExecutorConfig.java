package io.devpl.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置。可与@EnableAsync、@Async结合使用
 */
@Configuration(proxyBeanMethods = false)
public class ThreadPoolExecutorConfig {

    @Bean(value = "common-pool")
    public ThreadPoolTaskExecutor commonThreadPool() {
        // CPU核心数
        int cpuCount = Runtime.getRuntime().availableProcessors();

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(cpuCount);
        // 线程池最大线程数
        executor.setMaxPoolSize(cpuCount * 2);
        // 当线程池的线程数量大于核心线程数时，空闲线程的空闲回收时间，默认60秒
        executor.setKeepAliveSeconds(60);
        // 最大排队线程数量
        executor.setQueueCapacity(100);
        // 拒绝策略：调用者运行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
