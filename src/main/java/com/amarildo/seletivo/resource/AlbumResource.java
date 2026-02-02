package com.amarildo.seletivo.resource;


import com.amarildo.seletivo.model.dto.AlbumCreateDTO;
import com.amarildo.seletivo.model.dto.AlbumResponseDTO;
import com.amarildo.seletivo.model.dto.AlbumUpdateDTO;
import com.amarildo.seletivo.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Tag(name = "Álbuns", description = "Endpoints para gerenciamento de álbuns")
@RestController
@RequestMapping("/api/v1/albuns")
public class AlbumResource {

    @Autowired
    private AlbumService albumService;

    @GetMapping
    @Operation(
            summary = "Listar todos os álbuns",
            description = "Retorna a lista de todos os álbuns cadastrados no sistema",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de álbuns retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = AlbumResponseDTO.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "204", description = "Nenhum álbum encontrado")
            }
    )
    public ResponseEntity<List<AlbumResponseDTO>> listarTodos() {
        return ResponseEntity.ok(albumService.listarTodos());
    }

    @GetMapping("/{idenAlbum}")
    @Operation(
            summary = "Buscar álbum por identificador",
            description = "Retorna os dados de um álbum a partir do seu identificador",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Álbum encontrado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AlbumResponseDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Álbum não encontrado")
            }
    )
    public ResponseEntity<AlbumResponseDTO> buscarPorId(
            @Parameter(
                    description = "Identificador do álbum",
                    example = "10",
                    required = true
            )
            @PathVariable Long idenAlbum) {
        return ResponseEntity.ok(albumService.buscarPorId(idenAlbum));
    }

    @GetMapping("/tipo/{idenTipoAlbum}")
    @Operation(
            summary = "Listar álbuns por tipo",
            description = "Retorna a lista de álbuns associados a um tipo de álbum específico",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de álbuns retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AlbumResponseDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Tipo de álbum não encontrado")
            }
    )
    public ResponseEntity<List<AlbumResponseDTO>> listarPorTipo(
            @Parameter(
                    description = "Identificador do tipo de álbum",
                    example = "1",
                    required = true
            )
            @PathVariable Long idenTipoAlbum) {

        return ResponseEntity.ok(albumService.listarPorTipo(idenTipoAlbum));
    }

    @PostMapping
    @Operation(
            summary = "Criar álbum",
            description = "Cria um novo álbum informando a descrição e o tipo de álbum",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Álbum criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AlbumResponseDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
            }
    )
    public ResponseEntity<AlbumResponseDTO> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para criação do álbum",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumCreateDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "descricaoAlbum": "Greatest Hits",
                                  "idenTipoAlbum": 1
                                }
                                """
                            )
                    )
            )
            @Valid @RequestBody AlbumCreateDTO albumCriado) {

        AlbumResponseDTO response = albumService.salvar(albumCriado);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{idenAlbum}")
    @Operation(
            summary = "Atualizar álbum existente",
            description = "Atualiza os dados de um álbum existente, alterando descrição e tipo do álbum",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Álbum atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AlbumResponseDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida ou dados incompletos"),
                    @ApiResponse(responseCode = "404", description = "Álbum não encontrado")
            }
    )
    public ResponseEntity<AlbumResponseDTO> atualizar(
            @Parameter(
                    description = "Identificador do álbum a ser atualizado",
                    example = "1",
                    required = true
            )
            @PathVariable Long idenAlbum,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do álbum a serem atualizados",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumUpdateDTO.class)
                    )
            )
            @Valid @RequestBody AlbumUpdateDTO dto) {

        return ResponseEntity.ok(albumService.atualizar(idenAlbum, dto));

    }

    @GetMapping("/pagina")
    @Operation(
            summary = "Listar álbuns com paginação",
            description = "Retorna uma lista paginada de álbuns",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Página de álbuns retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AlbumResponseDTO.class)
                            )
                    )
            }
    )
    public ResponseEntity<Page<AlbumResponseDTO>> listarPaginado(
            @ParameterObject
            @PageableDefault(size = 10, page = 0)
            Pageable pageable) {
        Page<AlbumResponseDTO> pagina = albumService.listarTodosPaginado(pageable);
        return ResponseEntity.ok(pagina);
    }

}

