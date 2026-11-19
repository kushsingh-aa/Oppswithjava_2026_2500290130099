package com.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Spring Boot Application Entry Point for Question 3.
 * Run this class to launch the REST API server at http://localhost:8080
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class StudentApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentApplication.class, args);
        System.out.println("================================================================================");
        System.out.println("   SPRING BOOT REST API READY AT: http://localhost:8080/api/students");
        System.out.println("================================================================================");
    }
}
