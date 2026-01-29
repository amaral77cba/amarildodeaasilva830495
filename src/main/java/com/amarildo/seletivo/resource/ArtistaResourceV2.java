package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.model.Artista;
import com.amarildo.seletivo.service.ArtistaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/artistas")
public class ArtistaResourceV2 {

    @Autowired
    private ArtistaService artistaService;

    @PutMapping("/{idenArtista}")
    public ResponseEntity<Artista> atualizarV2(@PathVariable Long idenArtista, @Valid @RequestBody Artista artista) {

        return ResponseEntity.ok(artistaService.atualizarv2(idenArtista, artista));
    }
}

