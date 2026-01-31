package com.amarildo.seletivo.service;

import com.amarildo.seletivo.model.Regional;
import com.amarildo.seletivo.model.dto.RegionalDTO;
import com.amarildo.seletivo.repository.RegionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegionalService {

    @Autowired
    private RegionalRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    public RegionalService(RegionalRepository repository) {
        this.repository = repository;
        this.restTemplate = new RestTemplate();
    }

    public void sincronizarRegionais() {
        // Buscar regionais na API externas
        String url = "https://integrador-argus-api.geia.vip/v1/regionais";

        RegionalDTO[] externas;

        try {
            externas = restTemplate.getForObject(url, RegionalDTO[].class);
        } catch (Exception e) {
            // aqui você pode logar
            return;
        }

        if (externas == null) return;

        List<Integer> idsExternos = Arrays.stream(externas)
                .map(RegionalDTO::getId)
                .collect(Collectors.toList());

        // Inserir ou atualizar regionais
        for (RegionalDTO dto : externas) {
            Regional regional = repository.findById(dto.getId())
                    .orElse(new Regional(dto.getId(), dto.getNome(), true));

            regional.setNome(dto.getNome());
            regional.setAtivo(true);

            repository.save(regional);
        }

        // Desativar regionais que não existem mais
        List<Regional> paraDesativar = repository.findByIdNotIn(idsExternos);
        for (Regional r : paraDesativar) {
            r.setAtivo(false);
            repository.save(r);
        }

    }

    public List<Regional> listarAtivas() {
        return repository.findByAtivoTrueOrderByNomeAsc();
    }

}

