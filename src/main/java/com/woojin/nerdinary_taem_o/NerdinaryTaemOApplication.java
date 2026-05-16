package com.woojin.nerdinary_taem_o;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NerdinaryTaemOApplication {

    public static void main(String[] args) {
        SpringApplication.run(NerdinaryTaemOApplication.class, args);
    }

}
