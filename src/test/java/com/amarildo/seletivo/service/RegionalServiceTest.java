package com.amarildo.seletivo.service;

import com.amarildo.seletivo.model.Regional;
import com.amarildo.seletivo.model.dto.RegionalDTO;
import com.amarildo.seletivo.repository.RegionalRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegionalServiceTest {

    @InjectMocks
    private RegionalService service;

    @Mock
    private RegionalRepository repository;

    @Mock
    private RestTemplate restTemplate;

    private static final String URL = "https://integrador-argus-api.geia.vip/v1/regionais";


    //Teste da Regra 1
    @Test
    void deveInserirNovaRegionalQuandoNaoExistirAtiva() {
        // ARRANGE (preparação)
        RegionalDTO dto = new RegionalDTO(10, "REGIONAL NOVA");

        when(restTemplate.getForObject(URL, RegionalDTO[].class))
                .thenReturn(new RegionalDTO[]{dto});

        when(repository.findByIdExternoAndAtivoTrue(10))
                .thenReturn(Optional.empty());

        when(repository.findByIdExternoNotInAndAtivoTrue(List.of(10)))
                .thenReturn(List.of());

        // ACT (execução)
        service.sincronizarRegionais();

        // ASSERT (verificação)
        verify(repository).save(argThat(regional ->
                regional.getIdExterno().equals(10) &&
                        regional.getNome().equals("REGIONAL NOVA") &&
                        regional.getAtivo()
        ));
    }

    //Teste da Regra 2
    @Test
    void deveInativarRegionaisQueNaoVieramNoEndpoint() {

        // ARRANGE (preparação)
        RegionalDTO dto = new RegionalDTO();
        dto.setId(1);
        dto.setNome("Regional A");

        Mockito.when(restTemplate.getForObject(
                Mockito.anyString(),
                Mockito.eq(RegionalDTO[].class)
        )).thenReturn(new RegionalDTO[]{dto});

        Regional regionalC = new Regional(
          10,
          "Regional C",
          true,
          3
        );

        Mockito.when(repository.findByIdExternoAndAtivoTrue(1))
                .thenReturn(Optional.empty());

        Mockito.when(repository.findByIdExternoNotInAndAtivoTrue(List.of(1)))
                .thenReturn(List.of(regionalC));


        // ACT (execução)
        service.sincronizarRegionais();

        // ASSERT (verificação)
        Assertions.assertFalse(regionalC.getAtivo());
        Mockito.verify(repository).save(regionalC);
    }

    //Teste Regra 3
    @Test
    void deveInativarRegistroAntigoECriarNovoQuandoNomeDaAPIForDiferente() {

        // ARRANGE (preparacao)
        RegionalDTO dto = new RegionalDTO();
        dto.setId(9);
        dto.setNome("REGIONAL DE CUIABÁ - NOVO");

        // informacao do banco
        Regional regionalAtual = new Regional(4, "REGIONAL DE CUIABÁ", true, 9);

        Mockito.when(repository.findByIdExternoAndAtivoTrue(9))
                .thenReturn(Optional.of(regionalAtual));

        Mockito.when(repository.findByIdExternoNotInAndAtivoTrue(Mockito.anyList()))
                .thenReturn(List.of());

        Mockito.when(restTemplate.getForObject(
                Mockito.anyString(),
                Mockito.eq(RegionalDTO[].class)
        )).thenReturn(new RegionalDTO[]{dto});

        // ACT (execucap)
        service.sincronizarRegionais();

        // ASSERT 1 (verificacao) → antigo inativado
        Assertions.assertFalse(regionalAtual.getAtivo());

        // ASSERT 2 (verificacao) → antigo foi salvo inativo
        Mockito.verify(repository).save(regionalAtual);

        // ASSERT 3 → novo registro criado corretamente
        Mockito.verify(repository).save(Mockito.argThat(r ->
                r.getId() == null &&
                        r.getIdExterno().equals(9) &&
                        r.getNome().equals("REGIONAL DE CUIABÁ - NOVO") &&
                        r.getAtivo().equals(true)
        ));
    }

}

