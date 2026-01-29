package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.model.Arquivo;
import com.amarildo.seletivo.model.dto.ArquivoPresignedUrlDTO;
import com.amarildo.seletivo.service.ArquivoService;
import com.amarildo.seletivo.service.ArquivoStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/arquivos")
public class ArquivoResource {

    @Autowired
    private ArquivoStorageService arquivoStorageService;

    @Autowired
    private ArquivoService arquivoService;

    //Upload do arquivo
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Arquivo> upload(
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "descricao", required = false) String descricao
    ) {

        Arquivo arquivo = arquivoStorageService.upload(file, descricao);
        return ResponseEntity.status(HttpStatus.CREATED).body(arquivo);
    }

    //Gera URL temporaria para download
    @GetMapping("/download/{uuid}")
    public ResponseEntity<String> gerarUrlDownload(
            @PathVariable UUID uuid,
            @RequestParam(defaultValue = "10") int minutos
    ) {

        String url = arquivoStorageService.gerarUrlDownload(uuid, minutos);
        return ResponseEntity.ok(url);
    }

    //Remove arquivo(MinIO e Banco)
//    @DeleteMapping("/{idenArquivo}")
//    public ResponseEntity<Void> remover(@PathVariable Long idenArquivo) {
//        arquivoStorageService.remover(idenArquivo);
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping("/{uuid}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID uuid) {

        Arquivo arquivo = arquivoService.buscarPorUuid(uuid);

        Resource resource = arquivoService.download(arquivo);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + arquivo.getNomeArquivo() + "\"")
                .body(resource);
    }

    @GetMapping("/download/id/{idenArquivo}")
    public ResponseEntity<Resource> downloadPorId(@PathVariable Long idenArquivo) {

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
            summary = "Gera link pré-assinado para download",
            description = "Retorna uma URL temporária para download do arquivo com expiração de 30 minutos"
    )
    @ApiResponse(responseCode = "200", description = "Link gerado com sucesso")
    @ApiResponse(responseCode = "404", description = "Arquivo não encontrado")
    public ResponseEntity<ArquivoPresignedUrlDTO> gerarLinkDownload(@PathVariable UUID uuid) {

        return ResponseEntity.ok(
                arquivoService.gerarLinkDownload(uuid)
        );
    }


}
