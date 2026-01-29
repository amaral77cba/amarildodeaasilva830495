package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Operation(
            summary = "Autenticação de usuário",
            description = "Valida usuário e senha e retorna um token JWT",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login realizado com sucesso, retorna JWT",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"accessToken\":\"eyJhbGciOiJIUzI1NiJ9...\"}"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Usuário ou senha inválidos"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida")
            }
    )
    @Tag(name = "Autenticação", description = "Endpoint de autenticação e geração de token JWT")
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais do usuário",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"usuario\":\"amarildo\", \"senha\":\"123456\"}"
                            )
                    )
            )
            @RequestBody Map<String, String> body) {

        String username = body.get("usuario");
        String password = body.get("senha");

        if ("amarildo".equals(username) && "123456".equals(password)) {
            String token = jwtUtil.gerarToken(username);
            return ResponseEntity.ok(Map.of("accessToken", token));
        }

        return ResponseEntity.status(401).body("Usuário ou senha inválidos");
    }


}
