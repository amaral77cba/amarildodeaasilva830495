package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.model.ArtistaAlbum;
import com.amarildo.seletivo.model.dto.ArtistaAlbumCreateDTO;
import com.amarildo.seletivo.model.dto.ArtistaAlbumListDTO;
import com.amarildo.seletivo.model.dto.ArtistaAlbumResponseDTO;
import com.amarildo.seletivo.service.ArtistaAlbumService;
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

}

