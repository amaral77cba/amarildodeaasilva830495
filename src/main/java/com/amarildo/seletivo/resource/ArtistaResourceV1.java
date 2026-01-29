package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.enumerador.TipoArtista;
import com.amarildo.seletivo.model.Artista;
import com.amarildo.seletivo.service.ArtistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


import java.util.List;

@RestController
@RequestMapping("/api/v1/artistas")
public class ArtistaResourceV1 {

    @Autowired
    private ArtistaService artistaService;

    @GetMapping
    public ResponseEntity<List<Artista>> listarTodos() {
        return ResponseEntity.ok(artistaService.listarTodos());
    }

    @GetMapping("/{idenArtista}")
    public ResponseEntity<Artista> buscarPorId(@PathVariable Long idenArtista) {
        Artista artista = artistaService.buscarPorId(idenArtista);
        return ResponseEntity.ok(artista);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Artista>> listarPorTipo(@PathVariable TipoArtista tipo) {
        return ResponseEntity.ok(artistaService.listarPorTipo(tipo));
    }

    @PostMapping
    public ResponseEntity<Artista> criar(@Valid @RequestBody Artista artista) {
        Artista salvo = artistaService.salvar(artista);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{idenArtista}")
    public ResponseEntity<Artista> atualizar(@PathVariable Long idenArtista, @Valid @RequestBody Artista artista){
        Artista artistaSalva = artistaService.atualizar(idenArtista, artista);
        return ResponseEntity.ok(artistaSalva);
    }

}
