package com.bromles.test_task_region_directory;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Главный класс сервера
 */
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
public class TestTaskApplication {

    /**
     * Точка запуска сервера
     *
     * @param args Тип: {@link String String[]}. Параметры командной строки при запуске приложения
     */
    public static void main(String[] args) {
        SpringApplication.run(TestTaskApplication.class, args);
    }

}
