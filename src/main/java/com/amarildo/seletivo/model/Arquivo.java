package com.amarildo.seletivo.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "arquivo")
public class Arquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iden_arquivo")
    private Long idenArquivo;

    @Column(name = "uuid_arquivo")
    private UUID uuidArquivo;

    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    @Column(name = "descricao_arquivo")
    private String descricaoArquivo;

    @Column(name = "extensao_arquivo")
    private String extensaoArquivo;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "bucket")
    private String bucket;

    @Column(name = "object_name")
    private String objectName;

    @Column(name = "tamanho_bytes")
    private Long tamanhoBytes;


    public Arquivo() {
    }

    public Arquivo(UUID uuidArquivo) {
        this.uuidArquivo = uuidArquivo;
    }

    public Long getIdenArquivo() {
        return idenArquivo;
    }

    public void setIdenArquivo(Long idenArquivo) {
        this.idenArquivo = idenArquivo;
    }

    public UUID getUuidArquivo() {
        return uuidArquivo;
    }

    public void setUuidArquivo(UUID uuidArquivo) {
        this.uuidArquivo = uuidArquivo;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getDescricaoArquivo() {
        return descricaoArquivo;
    }

    public void setDescricaoArquivo(String descricaoArquivo) {
        this.descricaoArquivo = descricaoArquivo;
    }

    public String getExtensaoArquivo() {
        return extensaoArquivo;
    }

    public void setExtensaoArquivo(String extensaoArquivo) {
        this.extensaoArquivo = extensaoArquivo;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Long getTamanhoBytes() {
        return tamanhoBytes;
    }

    public void setTamanhoBytes(Long tamanhoBytes) {
        this.tamanhoBytes = tamanhoBytes;
    }
}
