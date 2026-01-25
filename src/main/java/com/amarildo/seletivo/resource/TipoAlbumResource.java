package com.amarildo.seletivo.resource;


import com.amarildo.seletivo.model.TipoAlbum;
import com.amarildo.seletivo.service.TipoAlbumService;
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

    @GetMapping
    public ResponseEntity<List<TipoAlbum>> listarTodos() {
        List<TipoAlbum> tipos = tipoAlbumService.listarTodos();
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/{idenTipoAlbum}")
    public ResponseEntity<TipoAlbum> buscarPorId(@PathVariable Long idenTipoAlbum) {
        TipoAlbum tipoAlbum = tipoAlbumService.buscarPorId(idenTipoAlbum);
        return ResponseEntity.ok(tipoAlbum);
    }

    @PutMapping("/{idenTipoAlbum}")
    public ResponseEntity<TipoAlbum> atualizar(@PathVariable Long idenTipoAlbum, @RequestBody TipoAlbum tipoAlbum) {

        TipoAlbum atualizado = tipoAlbumService.atualizar(idenTipoAlbum, tipoAlbum);
        return ResponseEntity.ok(atualizado);
    }

    @PostMapping
    public ResponseEntity<TipoAlbum> salvar(@Valid @RequestBody TipoAlbum tipoAlbum) {

        TipoAlbum salvo = tipoAlbumService.salvar(tipoAlbum);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(salvo);
    }

}
