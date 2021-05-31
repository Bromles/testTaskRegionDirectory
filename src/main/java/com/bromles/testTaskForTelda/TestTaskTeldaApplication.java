package com.bromles.testTaskForTelda;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@OpenAPIDefinition(
        info = @Info(
                title = "Region dictionary",
                version = "1.0",
                description = "Dictionary of regions",
                contact = @Contact(url = "https://github.com/Bromles", name = "Alexandr Ushakov")
        )
)
@SpringBootApplication
@EnableCaching
public class TestTaskTeldaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestTaskTeldaApplication.class, args);
    }

}
