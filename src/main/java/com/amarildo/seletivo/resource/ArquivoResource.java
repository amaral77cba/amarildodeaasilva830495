package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.model.Arquivo;
import com.amarildo.seletivo.model.TipoAlbum;
import com.amarildo.seletivo.model.dto.ArquivoPresignedUrlDTO;
import com.amarildo.seletivo.model.dto.ArquivoResponseDTO;
import com.amarildo.seletivo.service.ArquivoService;
import com.amarildo.seletivo.service.ArquivoStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Arquivos", description = "Endpoints para gerenciamento de arquivos")
@RestController
@RequestMapping("/api/v1/arquivos")
public class ArquivoResource {

    @Autowired
    private ArquivoStorageService arquivoStorageService;

    @Autowired
    private ArquivoService arquivoService;

    //Upload do arquivo
    @PostMapping(consumes = "multipart/form-data")
    @Operation(
            summary = "Upload de arquivo",
            description = "Realiza o upload de um arquivo para o storage (MinIO) e " +
                            "persiste seus metadados no banco de dados. " +
                            "Permite informar uma descrição opcional.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Arquivo enviado com sucesso",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Arquivo.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Arquivo inválido ou requisição mal formada"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado ou token inválido"),
                    @ApiResponse(responseCode = "500", description = "Erro interno ao processar o upload")
            }
    )
    public ResponseEntity<Arquivo> upload(
            @Parameter(
                    description = "Arquivo a ser enviado",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart("file") MultipartFile file,

            @Parameter(
                    description = "Descrição opcional do arquivo"
            )
            @RequestPart(value = "descricao", required = false) String descricao
    ) {

        Arquivo arquivo = arquivoStorageService.upload(file, descricao);
        return ResponseEntity.status(HttpStatus.CREATED).body(arquivo);
    }

    //Gera URL temporaria para download
    @GetMapping("/download/{uuid}")
    @Operation(
            summary = "Gerar URL temporária para download de arquivo",
            description = "Gera uma URL temporária para download de um arquivo armazenado, válida por um período em minutos",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "URL de download gerada com sucesso",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            example = "https://minio.exemplo.com/bucket/arquivo.pdf?X-Amz-Expires=600"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Arquivo não encontrado"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "500", description = "Erro interno ao gerar URL de download")
            }
    )
    public ResponseEntity<String> gerarUrlDownload(
            @Parameter(
                    description = "UUID do arquivo",
                    example = "575786b0-4f9e-4d2b-9f2c-123456789abc",
                    required = true
            )
            @PathVariable UUID uuid,
            @Parameter(
                    description = "Tempo de validade da URL em minutos",
                    example = "30"
            )
            @RequestParam(defaultValue = "30") int minutos
    ) {
        //System.out.println("###_Aqui0302");
        String url = arquivoStorageService.gerarUrlDownload(uuid, minutos);
        return ResponseEntity.ok(url);

    }

    @GetMapping("/{uuid}/download")
    @Operation(
            summary = "Realiza o download do arquivo",
            description = "Efetua o download direto do arquivo identificado pelo UUID, retornando o conteúdo como stream."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Arquivo retornado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Arquivo não encontrado")
    })
    public ResponseEntity<Resource> download(@PathVariable UUID uuid) {

        Arquivo arquivo = arquivoService.buscarPorUuid(uuid);

        //System.out.println("### uuid informado: " + uuid);
        Resource resource = arquivoService.download(arquivo);
        //System.out.println("### linkgerado para download: " + resource);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + arquivo.getNomeArquivo() + "\"")
                .body(resource);
    }

    @GetMapping("/download/id/{idenArquivo}")
    @Operation(
            summary = "Realiza o download do arquivo pelo ID",
            description = "Efetua o download direto do arquivo identificado pelo ID interno do sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Arquivo retornado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Arquivo não encontrado")
    })
    public ResponseEntity<Resource> downloadPorId(
            @Parameter(
                    description = "Identificador interno do arquivo",
                    example = "1",
                    required = true
            )
            @PathVariable Long idenArquivo) {

        Arquivo arquivo = arquivoService.buscarPorId(idenArquivo);

        Resource resource = arquivoService.download(arquivo);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + arquivo.getNomeArquivo() + "\"")
                .body(resource);
    }


    @GetMapping("/{uuid}/link")
    @Operation(
            summary = "Gerar link pré-assinado para download de arquivo",
            description = "Gera e retorna uma URL temporária para download de um arquivo armazenado, com tempo de expiração definido pela aplicação"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Link de download gerado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArquivoPresignedUrlDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Arquivo não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao gerar o link de download")
    })
    public ResponseEntity<ArquivoPresignedUrlDTO> gerarLinkDownload(
            @Parameter(
                    description = "UUID do arquivo",
                    example = "575786b0-4f9e-4d2b-9f2c-123456789abc",
                    required = true
            )
            @PathVariable UUID uuid) {

        return ResponseEntity.ok(
                arquivoService.gerarLinkDownload(uuid)
        );
    }


    @GetMapping
    @Operation(
            summary = "Listar todos os arquivos",
            description = "Retorna uma lista com todos os arquivos cadastrados, contendo apenas os dados básicos do arquivo"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de arquivos retornada com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ArquivoResponseDTO.class))
            )
    )
    @ApiResponse(responseCode = "204", description = "Nenhum arquivo encontrado"
    )
    public ResponseEntity<List<ArquivoResponseDTO>> listarTodos() {
        List<ArquivoResponseDTO> arquivos = arquivoService.listarTodos();
        return ResponseEntity.ok(arquivos);
    }
}
