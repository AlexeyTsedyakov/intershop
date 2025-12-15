package org.example.intershop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IntershopApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntershopApplication.class, args);
    }

}
