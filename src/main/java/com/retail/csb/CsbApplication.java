package com.retail.csb;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableAsync
@SpringBootApplication
public class CsbApplication {

    @Value("${config.threadpool.corepool.size}")
    private Integer corePoolSize;

    @Value("${config.threadpool.maxpool.size}")
    private Integer maxPoolSize;

    /**
     * {@link java.util.concurrent#Executor() Executor} This method also customizes
     * the Executor by defining a new bean. In Implementation, use @Async at method
     * level to make the method Asynchronous. Methods need to be public to
     * use @Async. Also, @Async annotated method calling @Async method will not
     * work.
     *
     * <pre>
     * {@code @Async public void updateData(String userId) throws
     * ApplicationException { logger.info("Updating details for User with {}",
     * userId); //your code goes here... } }
     * </pre>
     */
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }

    public static void main(String[] args) {
        log.info("Starting server");
        SpringApplication.run(CsbApplication.class, args);
    }

}
