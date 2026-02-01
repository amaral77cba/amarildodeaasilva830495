package com.amarildo.seletivo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "album",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "descricao_album")
        })
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iden_album")
    private Long idenAlbum;

    @Column(name = "descricao_album")
    private String descricaoAlbum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "iden_tipo_album",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_album_tipo_album")
    )
    private TipoAlbum tipoAlbum;

    public Album() {
    }

    public Album(String descricaoAlbum, TipoAlbum tipoAlbum) {
        this.descricaoAlbum = descricaoAlbum;
        this.tipoAlbum = tipoAlbum;
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

    public TipoAlbum getTipoAlbum() {
        return tipoAlbum;
    }

    public void setTipoAlbum(TipoAlbum tipoAlbum) {
        this.tipoAlbum = tipoAlbum;
    }
}
