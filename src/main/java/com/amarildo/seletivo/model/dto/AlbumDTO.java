package com.amarildo.seletivo.model.dto;

import com.amarildo.seletivo.model.Album;

public class AlbumDTO {

    private Long idenAlbum;
    private String descricaoAlbum;

    private Long idenTipoAlbum;
    private String descricaoTipoAlbum;

    public AlbumDTO() {}

    public AlbumDTO(Album album) {
        this.idenAlbum = album.getIdenAlbum();
        this.descricaoAlbum = album.getDescricaoAlbum();
        this.idenTipoAlbum = album.getTipoAlbum().getIdenTipoAlbum();
        this.descricaoTipoAlbum = album.getTipoAlbum().getDescricaoTipoAlbum();
    }

    public Long getIdenAlbum() {
        return idenAlbum;
    }

    public void setIdenAlbum(Long idenAlbum) {
        this.idenAlbum = idenAlbum;
    }

    public String getDescricaoAlbum() {
        return descricaoAlbum;
    }

    public void setDescricaoAlbum(String descricaoAlbum) {
        this.descricaoAlbum = descricaoAlbum;
    }

    public Long getIdenTipoAlbum() {
        return idenTipoAlbum;
    }

    public void setIdenTipoAlbum(Long idenTipoAlbum) {
        this.idenTipoAlbum = idenTipoAlbum;
    }

    public String getDescricaoTipoAlbum() {
        return descricaoTipoAlbum;
    }

    public void setDescricaoTipoAlbum(String descricaoTipoAlbum) {
        this.descricaoTipoAlbum = descricaoTipoAlbum;
    }
}
