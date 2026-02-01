package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.model.enums.TipoArtista;
import com.amarildo.seletivo.model.dto.ArtistaAlbumCreateDTO;
import com.amarildo.seletivo.model.dto.ArtistaAlbumListDTO;
import com.amarildo.seletivo.model.dto.ArtistaAlbumResponseDTO;
import com.amarildo.seletivo.service.ArtistaAlbumService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/artistas-albuns")
public class ArtistaAlbumResource {

    @Autowired
    private ArtistaAlbumService artistaAlbumService;


    @Operation(
            summary = "Listar vinculos de artistas com seus álbuns",
            description = "Retorna a lista de artistas com seus respectivos álbuns cadastrados no sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de artistas e álbuns retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ArtistaAlbumListDTO.class)
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Não autorizado - Token JWT ausente, inválido ou expirado"
            ),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @Tag(name = "Artista x Álbum", description = "Endpoints para gerenciamento do vínculo entre artistas e álbuns")
    @GetMapping
    public ResponseEntity<List<ArtistaAlbumListDTO>> listarTodos() {

        return ResponseEntity.ok(artistaAlbumService.listarTodos());

    }

    @GetMapping("/{idenArtistaAlbum}")
    @Operation(
            summary = "Buscar vínculo artista–álbum por identificador",
            description = "Retorna os dados do vínculo entre artista e álbum a partir do identificador informado",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Vínculo artista–álbum encontrado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ArtistaAlbumListDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Vínculo artista–álbum não encontrado"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado ou token inválido")
            }
    )
    @Tag(name = "Artista x Álbum", description = "Endpoints para gerenciamento do vínculo entre artistas e álbuns")
    public ResponseEntity<ArtistaAlbumListDTO> buscarPorId(
            @Parameter(
                    description = "Identificador do vínculo artista–álbum",
                    example = "1",
                    required = true
            )
            @PathVariable Long idenArtistaAlbum) {

        return ResponseEntity.ok(artistaAlbumService.buscarPorId(idenArtistaAlbum));
    }

    @Operation(
            summary = "Cadastrar artista e álbum",
            description = "Cria um novo artista com seu respectivo álbum a partir dos dados informados."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Artista e álbum cadastrados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaAlbumResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição"),
            @ApiResponse(responseCode = "401", description = "Não autorizado - Token JWT ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @Tag(name = "Artista x Álbum", description = "Endpoints para gerenciamento do vínculo entre artistas e álbuns")
    @PostMapping
    public ResponseEntity<ArtistaAlbumResponseDTO> salvar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para cadastro do artista e álbum",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaAlbumCreateDTO.class)
                    )
            )
            @Valid @RequestBody ArtistaAlbumCreateDTO artistaAlbumCreateDTO) {

        ArtistaAlbumResponseDTO response = artistaAlbumService.salvar(artistaAlbumCreateDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Hidden //ocultado pois o endpoint da consulta parametrizada jah faz a mesma funcao desta por-tipo
    @GetMapping("/por-tipo")
    @Operation(
            summary = "Filtrar álbuns por tipo de artista",
            description = "Retorna uma lista de álbuns vinculados a artistas do tipo informado (CANTOR ou BANDA)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaAlbumListDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Tipo de artista inválido"),
            @ApiResponse(responseCode = "401", description = "Não autorizado - Token JWT ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @Tag(name = "Artista x Álbum", description = "Endpoints para gerenciamento do vínculo entre artistas e álbuns")
    public ResponseEntity<List<ArtistaAlbumListDTO>> listarPorTipo(
            @Parameter(
                    description = "Tipo do artista",
                    required = true,
                    example = "CANTOR",
                    schema = @Schema(implementation = TipoArtista.class)
            )
            @RequestParam("tipo") TipoArtista tipo) {

        return ResponseEntity.ok(artistaAlbumService.buscarPorTipoArtista(tipo));
    }

    @Operation(
            summary = "Consulta parametrizada de álbuns por artista",
            description = "Permite consultar álbuns filtrando opcionalmente pelo tipo do artista (CANTOR ou BANDA) e/ou pelo nome do artista."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaAlbumListDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado - Token JWT ausente, inválido ou expirado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @Tag(name = "Artista x Álbum", description = "Endpoints para gerenciamento do vínculo entre artistas e álbuns")
    @GetMapping("/consulta")
    public ResponseEntity<List<ArtistaAlbumListDTO>> consultar(
                                                                @Parameter(
                                                                        description = "Tipo do artista",
                                                                        example = "BANDA",
                                                                        schema = @Schema(implementation = TipoArtista.class)
                                                                )
                                                                @RequestParam(required = false) TipoArtista tipo,

                                                                @Parameter(
                                                                        description = "Nome do artista (ou parte do nome)"
                                                                )
                                                                @RequestParam(required = false) String nomeArtista
                                                                ) {
        return ResponseEntity.ok(
                artistaAlbumService.consultar(tipo, nomeArtista)
        );
    }


}

