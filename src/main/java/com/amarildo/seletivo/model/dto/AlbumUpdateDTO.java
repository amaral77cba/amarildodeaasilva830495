package com.amarildo.seletivo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AlbumUpdateDTO {

    @NotBlank(message = "A descrição do álbum é obrigatória")
    private String descricaoAlbum;

    @NotNull(message = "O tipo de álbum é obrigatório")
    private Long idenTipoAlbum;

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
}
