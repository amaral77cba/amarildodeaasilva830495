package com.amarildo.seletivo.repository;

import com.amarildo.seletivo.model.AlbumImagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumImagemRepository extends JpaRepository<AlbumImagem, Long> {

    List<AlbumImagem> findByAlbum_IdenAlbum(Long idenAlbum);

    List<AlbumImagem> findByAlbum_IdenAlbumOrderByDataAlbumImagemAsc(Long idenAlbum);

}
