package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.model.enums.TipoArtista;
import com.amarildo.seletivo.model.dto.ArtistaAlbumCreateDTO;
import com.amarildo.seletivo.model.dto.ArtistaAlbumListDTO;
import com.amarildo.seletivo.model.dto.ArtistaAlbumResponseDTO;
import com.amarildo.seletivo.service.ArtistaAlbumService;
import io.swagger.v3.oas.annotations.Operation;
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

    @GetMapping
    public ResponseEntity<List<ArtistaAlbumListDTO>> listarTodos() {

        return ResponseEntity.ok(artistaAlbumService.listarTodos());

    }

    @GetMapping("/{idenArtistaAlbum}")
    public ResponseEntity<ArtistaAlbumListDTO> buscarPorId(@PathVariable Long idenArtistaAlbum) {

        return ResponseEntity.ok(artistaAlbumService.buscarPorId(idenArtistaAlbum));
    }

    @PostMapping
    public ResponseEntity<ArtistaAlbumResponseDTO> salvar(@Valid @RequestBody ArtistaAlbumCreateDTO artistaAlbumCreateDTO) {

        ArtistaAlbumResponseDTO response = artistaAlbumService.salvar(artistaAlbumCreateDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/por-tipo")
    @Operation(summary = "Filtra álbuns por tipo de artista",
            description = "Retorna uma lista simplificada de vínculos baseada no tipo (CANTOR ou BANDA)")
    public ResponseEntity<List<ArtistaAlbumListDTO>> listarPorTipo(
            @RequestParam("tipo") TipoArtista tipo) {

        return ResponseEntity.ok(artistaAlbumService.buscarPorTipoArtista(tipo));
    }

    @GetMapping("/consulta")
    @Operation(
            summary = "Consulta parametrizada de álbuns por artista",
            description = "Permite filtrar álbuns por nome do artista e/ou tipo (CANTOR ou BANDA)"
    )
    public ResponseEntity<List<ArtistaAlbumListDTO>> consultar(
                                                                @RequestParam(required = false) TipoArtista tipo,
                                                                @RequestParam(required = false) String nomeArtista
                                                                ) {
        return ResponseEntity.ok(
                artistaAlbumService.consultar(tipo, nomeArtista)
        );
    }


}

