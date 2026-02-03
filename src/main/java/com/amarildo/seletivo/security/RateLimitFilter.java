package com.amarildo.seletivo.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")
                || path.equals("/swagger-ui/index.html")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/api/v1/login")
                || path.startsWith("/api/v1/refresh");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String key = resolveKey(request);

        Bucket bucket = buckets.computeIfAbsent(key, this::newBucket);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter()
                    .write("Rate limit excedido: máximo de 10 requisições por minuto");
        }
    }

    private Bucket newBucket(String key) {
        return Bucket4j.builder()
                .addLimit(
                        Bandwidth.classic(
                                10,
                                Refill.intervally(10, Duration.ofMinutes(1))
                        )
                )
                .build();
    }

    private String resolveKey(HttpServletRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // Usuário autenticado via JWT
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // username do JWT
        }

        // Fallback para IP (caso não esteja autenticado)
        return request.getRemoteAddr();
    }
}