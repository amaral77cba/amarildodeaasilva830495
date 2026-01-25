package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.model.Album;
import com.amarildo.seletivo.model.TipoAlbum;
import com.amarildo.seletivo.model.dto.AlbumCreateDTO;
import com.amarildo.seletivo.model.dto.AlbumResponseDTO;
import com.amarildo.seletivo.repository.AlbumRepository;
import com.amarildo.seletivo.service.AlbumService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/albuns")
public class AlbumResource {

    @Autowired
    private AlbumService albumService;

    @GetMapping
    public ResponseEntity<List<AlbumResponseDTO>> listarTodos() {
        return ResponseEntity.ok(albumService.listarTodos());
    }

    @GetMapping("/{idenAlbum}")
    public ResponseEntity<AlbumResponseDTO> buscarPorId(@PathVariable Long idenAlbum) {
        return ResponseEntity.ok(albumService.buscarPorId(idenAlbum));
    }

    @GetMapping("/tipo/{idenTipoAlbum}")
    public ResponseEntity<List<AlbumResponseDTO>> listarPorTipo(@PathVariable Long idenTipoAlbum) {

        return ResponseEntity.ok(albumService.listarPorTipo(idenTipoAlbum));
    }

    @PostMapping
    public ResponseEntity<AlbumResponseDTO> criar(@Valid @RequestBody AlbumCreateDTO albumCriado) {

        AlbumResponseDTO response = albumService.salvar(albumCriado);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{idenAlbum}")
    public ResponseEntity<AlbumResponseDTO> atualizar(@PathVariable Long idenAlbum, @Valid @RequestBody AlbumCreateDTO albumAtualizado) {

        return ResponseEntity.ok(albumService.atualizar(idenAlbum, albumAtualizado));
    }
}

