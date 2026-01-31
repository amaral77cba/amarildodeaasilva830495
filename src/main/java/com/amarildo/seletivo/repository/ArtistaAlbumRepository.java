package com.amarildo.seletivo.repository;

import com.amarildo.seletivo.model.enums.TipoArtista;
import com.amarildo.seletivo.model.Album;
import com.amarildo.seletivo.model.Artista;
import com.amarildo.seletivo.model.ArtistaAlbum;
import com.amarildo.seletivo.model.dto.ArtistaAlbumListDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArtistaAlbumRepository extends JpaRepository<ArtistaAlbum, Long> {
    boolean existsByArtistaAndAlbum(Artista artista, Album album);

    @Query("SELECT new com.amarildo.seletivo.model.dto.ArtistaAlbumListDTO(" +
            "aa.idenArtistaAlbum, a.idenArtista, a.nomeArtista, alb.idenAlbum, alb.descricaoAlbum) " +
            "FROM ArtistaAlbum aa " +
            "JOIN aa.artista a " +
            "JOIN aa.album alb " +
            "WHERE a.tipoArtista = :tipo")
    List<ArtistaAlbumListDTO> findByTipoArtista(@Param("tipo") TipoArtista tipo);

    @Query("SELECT new com.amarildo.seletivo.model.dto.ArtistaAlbumListDTO( " +
            "aa.idenArtistaAlbum, " +
            "a.idenArtista, " +
            "a.nomeArtista, " +
            "alb.idenAlbum, " +
            "alb.descricaoAlbum) " +
            "FROM ArtistaAlbum aa " +
            "JOIN aa.artista a " +
            "JOIN aa.album alb " +
            "WHERE (:tipo IS NULL OR a.tipoArtista = :tipo) " +
            "AND (:nomeArtista IS NULL OR LOWER(a.nomeArtista) LIKE LOWER(CAST(CONCAT('%', :nomeArtista, '%') AS string)))")
    List<ArtistaAlbumListDTO> consultar(
            @Param("tipo") TipoArtista tipo,
            @Param("nomeArtista") String nomeArtista);


}
