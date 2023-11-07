package com.backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class BackendApplication {

    public static void main(String[] args) {
        String env = System.getenv("APP_ENV");
        if (env != null && env.equals("production")) {
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPwd = System.getenv("DB_PWD");
            if (dbUrl != null && !dbUrl.isEmpty()
                    && dbUser != null && !dbUser.isEmpty()
                    && dbPwd != null && !dbPwd.isEmpty()) {
                System.setProperty("spring.datasource.url", dbUrl);
                System.setProperty("spring.datasource.username", dbUser);
                System.setProperty("spring.datasource.password", dbPwd);
            } else {
                throw new RuntimeException("DB_URL, DB_USER or DB_PWD environment variables not set!");
            }
        }

        SpringApplication.run(BackendApplication.class, args);
    }

}
