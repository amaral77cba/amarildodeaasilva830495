package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.model.Artista;
import com.amarildo.seletivo.model.dto.ArtistaUpdateDTO;
import com.amarildo.seletivo.service.ArtistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/artistas")
public class ArtistaResourceV2 {

    @Autowired
    private ArtistaService artistaService;

    @PutMapping("/{idenArtista}")
    @Operation(
            summary = "Atualizar artista",
            description = "Atualiza os dados de um artista existente a partir do seu identificador",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Artista atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Artista.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos para requisição"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Artista não encontrado"
                    )
            }
    )
    @Tag(name = "Artistas", description = "Endpoints para gerenciamento de artistas")
    public ResponseEntity<Artista> atualizarV2(
            @Parameter(
                    description = "Identificador do artista",
                    required = true,
                    example = "1"
            )
            @PathVariable Long idenArtista,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do artista a serem atualizados",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaUpdateDTO.class)
                    )
            )
            @Valid @RequestBody ArtistaUpdateDTO dto) {

        return ResponseEntity.ok(artistaService.atualizarv2(idenArtista, dto));
    }
}

