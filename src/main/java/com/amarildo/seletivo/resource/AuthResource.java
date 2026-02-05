package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Tag(name = "Autenticação", description = "Endpoints de login, refresh e autenticação JWT")
@RestController
public class AuthResource {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/api/v1/login")
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

            //Gera tokens
            String accessToken = jwtUtil.gerarAccessToken(username);
            //System.out.println("###accessToken:  " + accessToken);
            String refreshToken = jwtUtil.gerarRefreshToken(username);
            //System.out.println("###refreshToken: " + refreshToken);

            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false)   // use false se for ambiente local sem HTTPS
                    .path("/api/v1/refresh") // endpoint que vai usar o refresh token
                    .maxAge(24 * 60 * 60) // 1 dia em segundos
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(Map.of("accessToken", accessToken));
        }

        return ResponseEntity.status(401).body("Usuário ou senha inválidos");
    }


    @PostMapping("/api/v1/refresh")
    @Operation(
            summary = "Renova accessToken usando refreshToken",
            description = "Lê o refreshToken do cookie e retorna um novo accessToken",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Novo accessToken gerado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"accessToken\":\"eyJhbGciOiJIUzI1NiJ9...\"}"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Refresh token inválido ou expirado")
            }
    )
    public ResponseEntity<?> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken) {

        if (refreshToken == null) {
            return ResponseEntity.status(401).body("Refresh token não encontrado");
        }

        try {
            // Verifica se é realmente um refreshToken
            if (!jwtUtil.isRefreshToken(refreshToken)) {
                return ResponseEntity.status(401).body("Token inválido");
            }

            // Extrai usuário do refreshToken
            String username = jwtUtil.extrairUsername(refreshToken);

            // Gera novo accessToken
            String newAccessToken = jwtUtil.gerarAccessToken(username);

            // Retorna novo accessToken no body
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Refresh token inválido ou expirado");
        }
    }

}
