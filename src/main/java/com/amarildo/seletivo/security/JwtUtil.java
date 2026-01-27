package com.amarildo.seletivo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "8f3c1a9e5b2d4e7a9c6f1d8b0a4e2f7c9d5b1a8e3f6c4d2b7a9e5c1"; // mudar em produção
    private final long EXPIRATION_TIME = 5 * 60 * 1000; // 5 minutos em milissegundos

    public String gerarToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extrairUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validarToken(String token, String username) {
        String tokenUsername = extrairUsername(token);
        return (tokenUsername.equals(username) && !tokenExpirado(token));
    }

    private boolean tokenExpirado(String token) {
        Date exp = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration();
        return exp.before(new Date());
    }
}

