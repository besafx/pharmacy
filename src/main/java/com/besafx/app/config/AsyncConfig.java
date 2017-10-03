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

    @Bean(name = "threadPoolEmailSender")
    public Executor threadPoolTaskExecutor() {
        log.info("Prepare threadPoolEmailSender...");
        return initThreadPool(1, 1, 500, "EmailSender-");
    }

    @Bean(name = "threadPoolFileUploader")
    public Executor threadPoolFileUploader() {
        log.info("Prepare threadPoolFileUploader...");
        return initThreadPool(5, 10, 500, "FileUploader-");
    }

    @Bean(name = "threadPoolFileSharing")
    public Executor threadPoolFileSharing() {
        log.info("Prepare threadPoolFileSharing...");
        return initThreadPool(5, 10, 500, "FileSharing-");
    }

    @Bean(name = "threadPoolReportGenerator")
    public Executor threadPoolReportGenerator() {
        log.info("Prepare threadPoolReportGenerator...");
        return initThreadPool(1, 1, 500, "FilePDFGenerate-");
    }

    @Bean(name = "ByteGenerate")
    public Executor ByteGenerate() {
        log.info("Prepare ByteGenerate...");
        return initThreadPool(1, 1, 500, "ByteGenerate-");
    }

    @Bean(name = "threadPoolBank")
    public Executor threadPoolBank() {
        CustomThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(500);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Bank-");
        executor.setThreadGroupName("BankGroup");
        executor.setBeanName("asyncExecutor-Bank");
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
