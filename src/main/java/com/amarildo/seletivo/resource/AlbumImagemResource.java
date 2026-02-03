package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.model.AlbumImagem;
import com.amarildo.seletivo.model.dto.AlbumImagemResponseDTO;
import com.amarildo.seletivo.service.AlbumImagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Imagens dos Álbuns", description = "Endpoints para gerenciamento de álbuns e suas imagens")
@RestController
@RequestMapping("/api/v1/albuns")
public class AlbumImagemResource {

    @Autowired
    private AlbumImagemService albumImagemService;

    @PostMapping(value = "/{idenAlbum}/imagens", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Adicionar imagem a um álbum",
            description = "Realiza o upload de uma imagem vinculada a um álbum específico. " +
                    "Permite informar uma descrição opcional para a imagem.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Imagem adicionada ao álbum com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AlbumImagemResponseDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Arquivo inválido ou requisição mal formada"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado ou token inválido"),
                    @ApiResponse(responseCode = "404", description = "Álbum não encontrado")
            }
    )
    public ResponseEntity<AlbumImagemResponseDTO> adicionarImagem(

            @Parameter(
                    description = "Identificador do álbum",
                    example = "10",
                    required = true
            )

            @PathVariable Long idenAlbum,

            @Parameter(
                    description = "Arquivo de imagem a ser enviado",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )

            @RequestPart("file") MultipartFile file,

            @Parameter(
                    description = "Descrição opcional da imagem"
            )

            @RequestPart(value = "descricao", required = false) String descricao
    ) {

        AlbumImagemResponseDTO albumImagemDTO = albumImagemService.adicionarImagem(idenAlbum, file, descricao);

        return ResponseEntity.status(HttpStatus.CREATED).body(albumImagemDTO);
    }
}
