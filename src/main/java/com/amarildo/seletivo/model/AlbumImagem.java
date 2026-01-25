package com.amarildo.seletivo.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "album_imagem")
public class AlbumImagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idenAlbumImagem;

    @ManyToOne
    @JoinColumn(name = "iden_album")
    private Album album;

    @ManyToOne
    @JoinColumn(name = "iden_arquivo")
    private Arquivo arquivo;

    @Column(name = "data_album_imagem", insertable = false, updatable = false)
    private OffsetDateTime dataAlbumImagem = OffsetDateTime.now();

    public Long getIdenAlbumImagem() {
        return idenAlbumImagem;
    }

    public void setIdenAlbumImagem(Long idenAlbumImagem) {
        this.idenAlbumImagem = idenAlbumImagem;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Arquivo getArquivo() {
        return arquivo;
    }

    public void setArquivo(Arquivo arquivo) {
        this.arquivo = arquivo;
    }

    public OffsetDateTime getDataAlbumImagem() {
        return dataAlbumImagem;
    }

    public void setDataAlbumImagem(OffsetDateTime dataAlbumImagem) {
        this.dataAlbumImagem = dataAlbumImagem;
    }
}
