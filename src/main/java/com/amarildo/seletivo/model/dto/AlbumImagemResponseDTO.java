package com.amarildo.seletivo.model.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public class AlbumImagemResponseDTO {

    private Long idenAlbumImagem;
    private Long idenAlbum;
    private UUID uuidArquivo;
    private Long idenArquivo;
    private OffsetDateTime dataAlbumImagem;




    public AlbumImagemResponseDTO(Long idenAlbumImagem, Long idenAlbum, UUID uuidArquivo, Long idenArquivo, OffsetDateTime dataAlbumImagem) {
        this.idenAlbumImagem = idenAlbumImagem;
        this.idenAlbum = idenAlbum;
        this.uuidArquivo = uuidArquivo;
        this.idenArquivo = idenArquivo;
        this.dataAlbumImagem = dataAlbumImagem;
    }

    public Long getIdenAlbumImagem() {
        return idenAlbumImagem;
    }

    public Long getIdenAlbum() {
        return idenAlbum;
    }

    public UUID getUuidArquivo() {
        return uuidArquivo;
    }

    public Long getIdenArquivo(){
        return idenArquivo;
    }

    public OffsetDateTime getDataAlbumImagem() {
        return dataAlbumImagem;
    }
}
