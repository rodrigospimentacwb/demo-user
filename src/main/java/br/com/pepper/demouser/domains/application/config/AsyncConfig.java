package br.com.pepper.demouser.domains.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Value("${async-config.thread-pool.core-pool-size}")
    private int corePoolSize;

    @Value("${async-config.thread-pool.max-pool-size}")
    private int maxPoolSize;

    @Value("${async-config.thread-pool.queue-capacity}")
    private int queueCapacity;

    @Value("${async-config.thread-pool.thread-name-prefix}")
    private String threadNamePrefix;

    @Bean
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("UserProjectAsyncThread-");
        executor.initialize();
        return executor;
    }
}
