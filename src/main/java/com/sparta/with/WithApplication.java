package com.sparta.with;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WithApplication {

    public static void main(String[] args) {
        SpringApplication.run(WithApplication.class, args);
    }

}
