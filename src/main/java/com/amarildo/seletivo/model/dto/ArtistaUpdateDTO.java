package com.amarildo.seletivo.model.dto;


import com.amarildo.seletivo.model.enums.TipoArtista;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ArtistaUpdateDTO {

    @NotBlank(message = "O nome do artista é obrigatório")
    @Size(max = 200, message = "O nome do artista deve ter no máximo 200 caracteres")
    private String nomeArtista;

    @NotNull(message = "O tipo do artista é obrigatório")
    private TipoArtista tipoArtista;

    public String getNomeArtista() {
        return nomeArtista;
    }

    public void setNomeArtista(String nomeArtista) {
        this.nomeArtista = nomeArtista;
    }

    public TipoArtista getTipoArtista() {
        return tipoArtista;
    }

    public void setTipoArtista(TipoArtista tipoArtista) {
        this.tipoArtista = tipoArtista;
    }
}