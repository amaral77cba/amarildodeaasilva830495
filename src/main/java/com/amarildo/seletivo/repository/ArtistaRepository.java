package com.amarildo.seletivo.repository;

import com.amarildo.seletivo.model.enums.TipoArtista;
import com.amarildo.seletivo.model.Artista;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    List<Artista> findByTipoArtista(TipoArtista tipoArtista, Sort sort);

    boolean existsByNomeArtistaIgnoreCase(String nomeArtista);

    boolean existsByNomeArtistaIgnoreCaseAndIdenArtistaNot(String nomeArtista, Long idenArtista);
}
