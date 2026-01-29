package com.amarildo.seletivo.model.dto;

public class ArquivoPresignedUrlDTO {

    private String url;
    private Integer expiresInMinutes;

    public ArquivoPresignedUrlDTO(String url, Integer expiresInMinutes) {
        this.url = url;
        this.expiresInMinutes = expiresInMinutes;
    }

    public String getUrl() {
        return url;
    }

    public Integer getExpiresInMinutes() {
        return expiresInMinutes;
    }

}
