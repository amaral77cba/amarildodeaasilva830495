package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.model.dto.ArtistaCreateDTO;
import com.amarildo.seletivo.model.dto.ArtistaUpdateDTO;
import com.amarildo.seletivo.model.enums.TipoArtista;
import com.amarildo.seletivo.model.Artista;
import com.amarildo.seletivo.service.ArtistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


import java.util.List;

@RestController
@RequestMapping("/api/v1/artistas")
public class ArtistaResourceV1 {

    @Autowired
    private ArtistaService artistaService;

    @GetMapping
    @Operation(
            summary = "Listar artistas",
            description = "Retorna a lista de todos os artistas cadastrados no sistema",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de artistas retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Artista.class)
                            )
                    ),
                    @ApiResponse(responseCode = "204", description = "Nenhum artista encontrado")
            }
    )
    @Tag(name = "Artistas", description = "Endpoints para gerenciamento de artistas")
    public ResponseEntity<List<Artista>> listarTodos() {
        return ResponseEntity.ok(artistaService.listarTodos());
    }

    @GetMapping("/{idenArtista}")
    @Operation(
            summary = "Buscar artista por ID",
            description = "Retorna os dados de um artista a partir do seu identificador",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Artista encontrado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Artista.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Artista não encontrado")
            }
    )
    @Tag(name = "Artistas", description = "Endpoints para gerenciamento de artistas")
    public ResponseEntity<Artista> buscarPorId(
            @Parameter(
                    description = "Identificador do artista",
                    example = "1",
                    required = true
            )
            @PathVariable Long idenArtista) {
        Artista artista = artistaService.buscarPorId(idenArtista);
        return ResponseEntity.ok(artista);
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(
            summary = "Listar artistas por tipo",
            description = "Retorna a lista de artistas filtrados pelo tipo informado (BANDA ou CANTOR)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de artistas retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = Artista.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Tipo de artista inválido")
            }
    )
    @Tag(name = "Artistas", description = "Endpoints para gerenciamento de artistas")
    public ResponseEntity<List<Artista>> listarPorTipo(
            @Parameter(
                    description = "Tipo do artista",
                    required = true,
                    example = "BANDA",
                    schema = @Schema(implementation = TipoArtista.class)
            )
            @PathVariable TipoArtista tipo) {
        return ResponseEntity.ok(artistaService.listarPorTipo(tipo));
    }

    @PostMapping
    @Operation(
            summary = "Criar artista",
            description = "Cadastra um novo artista no sistema",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Artista criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Artista.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição"),
                    @ApiResponse(responseCode = "409", description = "Artista já existente com o mesmo nome")
            }
    )
    @Tag(name = "Artistas", description = "Endpoints para gerenciamento de artistas")
    public ResponseEntity<Artista> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do artista a ser criado",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaCreateDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "nomeArtista": "Legião Urbana",
                                  "tipoArtista": "BANDA"
                                }
                                """
                            )
                    )
            )
            @Valid @RequestBody ArtistaCreateDTO dto) {
        Artista salvo = artistaService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

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
                    @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição"),
                    @ApiResponse(responseCode = "404", description = "Artista não encontrado")
            }
    )
    @Tag(name = "Artistas", description = "Endpoints para gerenciamento de artistas")
    public ResponseEntity<Artista> atualizar(
            @Parameter(
                    description = "Identificador do artista",
                    example = "1",
                    required = true
            )
            @PathVariable Long idenArtista,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do artista a serem atualizados",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaUpdateDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "nomeArtista": "Legião Urbana",
                                  "tipoArtista": "BANDA"
                                }
                                """
                            )
                    )
            )
            @Valid @RequestBody ArtistaUpdateDTO dto){
        Artista artistaSalva = artistaService.atualizar(idenArtista, dto);
        return ResponseEntity.ok(artistaSalva);
    }

}
