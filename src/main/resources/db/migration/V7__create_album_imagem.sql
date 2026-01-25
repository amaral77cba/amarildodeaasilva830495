CREATE TABLE album_imagem (
    iden_album_imagem   BIGSERIAL PRIMARY KEY,
    iden_album          BIGINT NOT NULL,
    iden_arquivo        BIGINT NOT NULL,
    data_album_imagem   TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_album_imagem_album
        FOREIGN KEY (iden_album)
        REFERENCES album (iden_album)
        ON DELETE CASCADE,

    CONSTRAINT fk_album_imagem_arquivo
        FOREIGN KEY (iden_arquivo)
        REFERENCES arquivo (iden_arquivo)
        ON DELETE CASCADE
);