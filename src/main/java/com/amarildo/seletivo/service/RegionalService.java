package com.amarildo.seletivo.service;

import com.amarildo.seletivo.model.Regional;
import com.amarildo.seletivo.model.dto.RegionalDTO;
import com.amarildo.seletivo.repository.RegionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegionalService {

    @Autowired
    private RegionalRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
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

            Optional<Regional> atualOpt = repository.findByIdExternoAndAtivoTrue(dto.getId());

            if (atualOpt.isEmpty()) {

                // Regra 1 - novo
                repository.save(new Regional(null, dto.getNome(), true, dto.getId()));
                continue;
            }

            Regional atual = atualOpt.get();

            if (!atual.getNome().equals(dto.getNome())) {
                // Regra 3 - atributo alterado
                atual.setAtivo(false);
                repository.save(atual);

                repository.save(new Regional(null, dto.getNome(), true, dto.getId())
                );
            }
        }

        // Regra 2 - inativar
        List<Regional> paraDesativar = repository.findByIdExternoNotInAndAtivoTrue(idsExternos);
        for (Regional r : paraDesativar) {
            r.setAtivo(false);
            repository.save(r);
        }

    }

    public List<Regional> listarAtivas() {
        return repository.findByAtivoTrueOrderByNomeAsc();
    }

}

