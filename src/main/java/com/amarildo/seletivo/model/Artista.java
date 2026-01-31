package com.amarildo.seletivo.model;

import com.amarildo.seletivo.model.enums.TipoArtista;
import jakarta.persistence.*;

@Entity
@Table(name = "artista",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "nome_artista")
        })
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iden_artista")
    private Long idenArtista;

    @Column(name = "nome_artista", nullable = false, length = 200)
    private String nomeArtista;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_artista", nullable = false, length = 10)
    private TipoArtista tipoArtista;

    public Artista() {
    }

    public Artista(String nomeArtista, TipoArtista tipoArtista) {
        this.nomeArtista = nomeArtista;
        this.tipoArtista = tipoArtista;
    }

    public Long getIdenArtista() {
        return idenArtista;
    }

    public void setIdenArtista(Long idenArtista) {
        this.idenArtista = idenArtista;
    }

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
