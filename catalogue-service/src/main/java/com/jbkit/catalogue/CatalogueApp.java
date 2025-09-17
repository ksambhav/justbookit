package com.jbkit.catalogue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import static org.springframework.boot.SpringApplication.run;

@Slf4j
@EnableCaching
@SpringBootApplication
public class CatalogueApp {

    public static void main(String[] args) {
        run(CatalogueApp.class, args);
    }
}
