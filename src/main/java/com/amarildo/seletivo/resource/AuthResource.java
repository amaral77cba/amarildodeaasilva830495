package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthResource {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        String username = body.get("usuario");
        String password = body.get("senha");

        if ("amarildo".equals(username) && "123456".equals(password)) {
            String token = jwtUtil.gerarToken(username);
            return ResponseEntity.ok(Map.of("accessToken", token));
        }

        return ResponseEntity.status(401).body("Usuário ou senha inválidos");
    }

}
