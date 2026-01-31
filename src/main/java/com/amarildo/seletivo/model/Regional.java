package com.amarildo.seletivo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbregional")
public class Regional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "id_externo")
    private Integer idExterno; // ID DA API EXTERNA

    public Regional() {
    }

    public Regional(Integer id, String nome, Boolean ativo, Integer idExterno) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
        this.idExterno = idExterno;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getIdExterno() {
        return idExterno;
    }

    public void setIdExterno(Integer idExterno) {
        this.idExterno = idExterno;
    }
}

