CREATE TABLE arquivo (
   iden_arquivo       BIGSERIAL PRIMARY KEY,
   uuid_arquivo       UUID NOT NULL,
   nome_arquivo       VARCHAR(255),
   descricao_arquivo  VARCHAR(200),
   extensao_arquivo   VARCHAR(20),
   content_type       VARCHAR(200),
   bucket 			  VARCHAR(100),
   object_name        VARCHAR(255),
   tamanho_bytes      BIGINT
);
