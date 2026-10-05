package com.example.ratelimiter.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuration properties for the token bucket rate limiter.
 */
@Configuration
public class RateLimiterConfig {

    /**
     * Maximum number of tokens the bucket can hold.
     */
    @Value("${ratelimiter.capacity:100}")
    private long capacity;

    /**
     * Number of tokens added each refill period.
     */
    @Value("${ratelimiter.refillTokens:100}")
    private long refillTokens;

    /**
     * Refill period in ISO‑8601 duration format (e.g., PT1M for one minute).
     */
    @Value("${ratelimiter.refillPeriod:PT1M}")
    private Duration refillPeriod;

    /**
     * Builds a {@link Bandwidth} instance based on the configured properties.
     *
     * @return Bandwidth definition for Bucket4j
     */
    public Bandwidth getBandwidth() {
        Refill refill = Refill.greedy(refillTokens, refillPeriod);
        return Bandwidth.classic(capacity, refill);
    }
}