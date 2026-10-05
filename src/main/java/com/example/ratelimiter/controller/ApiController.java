package com.example.ratelimiter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sample API controller exposing a simple endpoint.
 */
@RestController
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    /**
     * Returns a greeting message.
     *
     * @return "Hello, World!"
     */
    @GetMapping("/api/hello")
    public String hello() {
        logger.info("Processing /api/hello request");
        return "Hello, World!";
    }
}