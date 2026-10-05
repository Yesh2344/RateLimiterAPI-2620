package com.example.ratelimiter.filter;

import com.example.ratelimiter.exception.RateLimitExceededException;
import com.example.ratelimiter.config.RateLimiterConfig;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servlet filter that enforces per‑IP rate limiting using Bucket4j.
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);

    private final Bandwidth bandwidth;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public RateLimitingFilter(RateLimiterConfig config) {
        this.bandwidth = config.getBandwidth();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String clientIp = extractClientIp(request);
        Bucket bucket = buckets.computeIfAbsent(clientIp, k -> Bucket4j.builder().addLimit(bandwidth).build());

        if (bucket.tryConsume(1)) {
            logger.debug("Request allowed for IP {}", clientIp);
            filterChain.doFilter(request, response);
        } else {
            logger.warn("Rate limit exceeded for IP {}", clientIp);
            throw new RateLimitExceededException("Too many requests - please try again later.");
        }
    }

    /**
     * Extracts the client IP address taking into account common proxy headers.
     *
     * @param request the HTTP servlet request
     * @return the resolved client IP address
     */
    private String extractClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isEmpty()) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}