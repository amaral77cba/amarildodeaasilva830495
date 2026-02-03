package com.amarildo.seletivo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

//        System.out.println("### shouldNotFilter | path = " + path);
//        boolean skip = path.startsWith("/swagger-ui")
//                || path.startsWith("/v3/api-docs")
//                || path.equals("/swagger-ui.html")
//                || path.startsWith("/api/v1/login")
//                || path.startsWith("/api/v1/refresh");
//        System.out.println("### shouldNotFilter | skip = " + skip);

        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/api/v1/login")
                || path.startsWith("/api/v1/refresh");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //try {
            //System.out.println("### JwtAuthenticationFilter EXECUTANDO: " + request.getRequestURI());
            String authHeader = request.getHeader("Authorization");
            //System.out.println("### Authorization header: " + authHeader);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                String jwt = authHeader.substring(7);

                String username = jwtUtil.extrairUsername(jwt);
                //System.out.println("### JWT username extraído: " + username);

                if (jwtUtil.validarToken(jwt, username)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    Collections.emptyList()
                            );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        //} catch (Exception ex) {

        //    SecurityContextHolder.clearContext();
        //    authenticationEntryPoint.commence(request, response,
        //            new org.springframework.security.authentication.InsufficientAuthenticationException(
        //                    "Token inválido ou expirado"
        //            )
        //    );
        //}
    }
}
