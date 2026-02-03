package com.amarildo.seletivo.config;

import com.amarildo.seletivo.security.JwtAuthenticationEntryPoint;
import com.amarildo.seletivo.security.JwtAuthenticationFilter;
import com.amarildo.seletivo.security.RateLimitFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private RateLimitFilter rateLimitFilter;

    @Value("${security.jwt.enabled:true}")
    private boolean jwtEnabled;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //http.csrf(csrf -> csrf.disable());

        //if (jwtEnabled) {

            http
                    .csrf(csrf -> csrf.disable())
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/api/v1/login",
                                    "/api/v1/refresh",
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/ws/**"
                            ).permitAll()
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //} else {
        //    http
        //            .authorizeHttpRequests(auth -> auth
        //                    .anyRequest().permitAll()
        //            );
        //}

        return http.build();
    }
}
