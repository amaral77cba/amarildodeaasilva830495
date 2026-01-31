package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.model.Regional;
import com.amarildo.seletivo.service.RegionalService;
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
    public ResponseEntity<Void> sincronizar() {
        regionalService.sincronizarRegionais();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Regional>> listar() {
        return ResponseEntity.ok(regionalService.listarAtivas());
    }
}
