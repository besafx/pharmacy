package com.besafx.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    private final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

    @Bean(name = "threadSinglePool")
    public Executor threadSinglePool() {
        log.info("Prepare threadSinglePool...");
        return initThreadPool(1, 1, 500, "Single-");
    }

    @Bean(name = "threadMultiplePool")
    public Executor threadMultiplePool() {
        log.info("Prepare threadMultiplePool...");
        return initThreadPool(10, 10, 500, "Multiple-");
    }

    private ThreadPoolTaskExecutor initThreadPool(int corePoolSize, int maxPoolSize, int queueCapacity, String prefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix(prefix);
        return executor;
    }

}
