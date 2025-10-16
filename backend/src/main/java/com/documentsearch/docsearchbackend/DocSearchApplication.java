package com.documentsearch.docsearchbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DocSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(DocSearchApplication.class, args);
    }

    @GetMapping("/")
    public String index() {
        return "Document Search API is up.";
    }
}

