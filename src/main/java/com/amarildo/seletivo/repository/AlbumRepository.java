package com.amarildo.seletivo.repository;

import com.amarildo.seletivo.model.Album;
import com.amarildo.seletivo.model.TipoAlbum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    boolean existsByDescricaoAlbumIgnoreCase(String descricaoAlbum);

    boolean existsByDescricaoAlbumIgnoreCaseAndIdenAlbumNot(String descricaoAlbum, Long idenAlbum);

    List<Album> findByTipoAlbum(TipoAlbum tipoAlbum);

    List<Album> findByTipoAlbum_IdenTipoAlbum(Long idenTipoAlbum);

}
