package com.besafx.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    private final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

    @Bean(name = "threadSinglePool")
    public Executor threadSinglePool() {
        log.info("Prepare threadSinglePool...");
        CustomThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(500);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Single-");
        executor.setThreadGroupName("SingleGroup");
        executor.setBeanName("asyncExecutor-Single");
        executor.initialize();
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }

    @Bean(name = "threadMultiplePool")
    public Executor threadMultiplePool() {
        log.info("Prepare threadMultiplePool...");
        CustomThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(500);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Multiple-");
        executor.setThreadGroupName("MultipleGroup");
        executor.setBeanName("asyncExecutor-Multiple");
        executor.initialize();
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
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
