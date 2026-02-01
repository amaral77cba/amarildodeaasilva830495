package com.amarildo.seletivo.model.dto;

import java.util.UUID;

public class ArquivoResponseDTO {

    private Long idenArquivo;

    private UUID uuidArquivo;

    private String nomeArquivo;

    public ArquivoResponseDTO(Long idenArquivo, UUID uuidArquivo, String nomeArquivo) {
        this.idenArquivo = idenArquivo;
        this.uuidArquivo = uuidArquivo;
        this.nomeArquivo = nomeArquivo;
    }

    public Long getIdenArquivo() {
        return idenArquivo;
    }

    public UUID getUuidArquivo() {
        return uuidArquivo;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }
}
