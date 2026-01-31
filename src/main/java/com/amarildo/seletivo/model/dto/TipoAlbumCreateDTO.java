package com.amarildo.seletivo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TipoAlbumCreateDTO {

    @NotBlank(message = "A descrição do tipo de álbum é obrigatória")
    @Size(max = 200, message = "A descrição pode ter no máximo 200 caracteres")
    private String descricaoTipoAlbum;

    public String getDescricaoTipoAlbum() {
        return descricaoTipoAlbum;
    }

    public void setDescricaoTipoAlbum(String descricaoTipoAlbum) {
        this.descricaoTipoAlbum = descricaoTipoAlbum;
    }
}
