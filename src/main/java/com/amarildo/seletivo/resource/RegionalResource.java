package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.model.Regional;
import com.amarildo.seletivo.service.RegionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/regionais")
public class RegionalResource {

    private final RegionalService regionalService;

    public RegionalResource(RegionalService regionalService) {
        this.regionalService = regionalService;
    }

    @PostMapping("/sincronizar")
    @Operation(
            summary = "Sincronizar regionais com API externa",
            description = "Realiza a sincronização das regionais com o sistema externo. " +
                    "Novas regionais são criadas, regionais ausentes são inativadas " +
                    "e alterações de nome geram novo registro ativo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sincronização realizada com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado ou token inválido"),
                    @ApiResponse(responseCode = "500", description = "Erro interno durante o processo de sincronização")
            }
    )
    @Tag(name = "Regionais", description = "Endpoint de sincronizacao e consulta de regionais")
    public ResponseEntity<Void> sincronizar() {
        regionalService.sincronizarRegionais();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(
            summary = "Listar regionais ativas",
            description = "Retorna a lista de regionais ativas cadastradas no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de regionais retornada com sucesso",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = Regional.class)
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado ou token inválido"),
                    @ApiResponse(responseCode = "500", description = "Erro interno ao buscar regionais")
            }
    )
    @Tag(name = "Regionais", description = "Endpoint de sincronizacao e consulta de regionais")
    public ResponseEntity<List<Regional>> listar() {
        return ResponseEntity.ok(regionalService.listarAtivas());
    }
}
