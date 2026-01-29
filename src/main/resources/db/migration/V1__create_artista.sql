CREATE TABLE artista (
    iden_artista BIGSERIAL PRIMARY KEY,
    nome_artista VARCHAR(200) NOT NULL,
    tipo_artista VARCHAR(10) NOT NULL,
    CONSTRAINT chk_tipo_artista
        CHECK (tipo_artista IN ('BANDA', 'CANTOR'))
);