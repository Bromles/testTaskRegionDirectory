package com.bromles.testTaskForTelda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TestTaskTeldaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestTaskTeldaApplication.class, args);
    }

}
