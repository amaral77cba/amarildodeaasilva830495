package com.amarildo.seletivo.resource;


import com.amarildo.seletivo.model.TipoAlbum;
import com.amarildo.seletivo.model.dto.TipoAlbumCreateDTO;
import com.amarildo.seletivo.model.dto.TipoAlbumUpdateDTO;
import com.amarildo.seletivo.service.TipoAlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-album")
public class TipoAlbumResource {

    @Autowired
    private TipoAlbumService tipoAlbumService;

    @Operation(
            summary = "Listar tipos de álbum",
            description = "Retorna a lista de todos os tipos de álbum cadastrados no sistema",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de tipos de álbum retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TipoAlbum.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno no servidor"
                    )
            }
    )
    @Tag(name = "Tipo de Álbum", description = "Endpoints para gerenciamento de tipos de álbum")
    @GetMapping
    public ResponseEntity<List<TipoAlbum>> listarTodos() {
        List<TipoAlbum> tipos = tipoAlbumService.listarTodos();
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/{idenTipoAlbum}")
    @Operation(
            summary = "Buscar tipo de álbum por ID",
            description = "Retorna os dados de um tipo de álbum a partir do seu identificador",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tipo de álbum encontrado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TipoAlbum.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Tipo de álbum não encontrado"
                    )
            }
    )
    @Tag(name = "Tipo de Álbum", description = "Endpoints para gerenciamento de tipos de álbum")
    public ResponseEntity<TipoAlbum> buscarPorId(@PathVariable Long idenTipoAlbum) {
        TipoAlbum tipoAlbum = tipoAlbumService.buscarPorId(idenTipoAlbum);
        return ResponseEntity.ok(tipoAlbum);
    }

    @Operation(
            summary = "Atualizar tipo de álbum",
            description = "Atualiza os dados de um tipo de álbum existente a partir do seu identificador",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tipo de álbum atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TipoAlbum.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Tipo de álbum não encontrado"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos na requisição"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno no servidor"
                    )
            }
    )
    @Tag(name = "Tipo de Álbum", description = "Endpoints para gerenciamento de tipos de álbum")
    @PutMapping("/{idenTipoAlbum}")
    public ResponseEntity<TipoAlbum> atualizar(
            @Parameter(
                    description = "Identificador do tipo de álbum",
                    example = "1",
                    required = true
            )
            @PathVariable Long idenTipoAlbum,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do tipo de álbum a serem atualizados",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoAlbumUpdateDTO.class)
                    )
            )
            @Valid @RequestBody TipoAlbumUpdateDTO dto
    ) {
        TipoAlbum atualizado = tipoAlbumService.atualizar(idenTipoAlbum, dto);
        return ResponseEntity.ok(atualizado);
    }


    @Operation(
            summary = "Cadastrar tipo de álbum",
            description = "Cria um novo tipo de álbum no sistema",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Tipo de álbum criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TipoAlbum.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos ou mal formatados"
                    )
            }
    )
    @Tag(name = "Tipo de Álbum", description = "Endpoints para gerenciamento de tipos de álbum")
    @PostMapping
    public ResponseEntity<TipoAlbum> salvar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do tipo de álbum a ser cadastrado",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoAlbumCreateDTO.class),
                            examples = @ExampleObject(
                                    value = "{ \"descricaoTipoAlbum\": \"Álbum de Estúdio\" }"
                            )
                    )
            )
            @Valid @RequestBody TipoAlbumCreateDTO dto
    ) {

        TipoAlbum salvo = tipoAlbumService.salvar(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(salvo);
    }

}
