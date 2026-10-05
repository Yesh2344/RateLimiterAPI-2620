package com.example.ratelimiter;

import com.example.ratelimiter.controller.ApiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Import necessary configuration and filter beans
@WebMvcTest(ApiController.class)
@Import({com.example.ratelimiter.filter.RateLimitingFilter.class,
         com.example.ratelimiter.config.RateLimiterConfig.class,
         com.example.ratelimiter.exception.GlobalExceptionHandler.class})
class RateLimitingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private com.example.ratelimiter.filter.RateLimitingFilter rateLimitingFilter;

    @BeforeEach
    void resetBuckets() {
        // Reflectively clear the internal bucket map to start fresh for each test
        try {
            var field = com.example.ratelimiter.filter.RateLimitingFilter.class
                    .getDeclaredField("buckets");
            field.setAccessible(true);
            ((java.util.Map<?, ?>) field.get(rateLimitingFilter)).clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
// was easier to read this way
    }

    @Test
    void whenWithinLimit_thenOk() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, World!")));
    }

    @Test
    void whenExceedingLimit_thenTooManyRequests() throws Exception {
        // Exhaust the bucket (default 100 per minute). We'll request 101 times.
        for (int i = 0; i < 101; i++) {
            mockMvc.perform(get("/api/hello"))
                    .andExpect(i < 100 ? status().isOk() : status().isTooManyRequests());
        }
    }
}