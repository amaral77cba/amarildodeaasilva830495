
--dados do Tipo Album
INSERT INTO tipo_album (descricao_tipo_album) VALUES ('Álbum de Estúdio');
INSERT INTO tipo_album (descricao_tipo_album) VALUES ('EP');
INSERT INTO tipo_album (descricao_tipo_album) VALUES ('Ao Vivo');
INSERT INTO tipo_album (descricao_tipo_album) VALUES ('Greatest Hits');

--insercao de alguns Artistas
INSERT INTO artista (nome_artista, tipo_artista) VALUES ('Serj Tankian', 'CANTOR');
INSERT INTO artista (nome_artista, tipo_artista) VALUES ('Mike Shinoda', 'CANTOR');
INSERT INTO artista (nome_artista, tipo_artista) VALUES ('Michel Teló', 'CANTOR');
INSERT INTO artista (nome_artista, tipo_artista) VALUES ('Guns N'' Roses', 'BANDA');

--insercao de alguns albuns
-- Serj Tankian
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Harakiri', 1);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Black Blooms', 1);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('The Rough Dog', 1);

-- Mike Shinoda
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('The Rising Tied', 1);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Post Traumatic', 1);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Post Traumatic EP', 2);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Where''d You Go', 1);

-- Michel Teló
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Bem Sertanejo', 1);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Bem Sertanejo - O Show (Ao Vivo)', 3);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Bem Sertanejo - (1ª Temporada) - EP', 2);

-- Guns N' Roses
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Use Your Illusion I', 1);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Use Your Illusion II', 1);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Greatest Hits', 4);
 


--Outros inserts
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Xou da Xuxa 3', 1);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Músicas para Louvar ao Senhor', 1);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Xegundo Xou da Xuxa', 1);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Xou da Xuxa', 1);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('4º Xou da Xuxa', 1);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Quatro Estações: O Show', 3);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('As Quatro Estações', 1);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Era Uma Vez... Ao Vivo', 3);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Djavan ao Vivo', 1);
INSERT INTO album (descricao_album, iden_tipo_album) VALUES ('Esse Cara Sou Eu', 1);


INSERT INTO artista (nome_artista, tipo_artista) VALUES ('Xuxa', 'CANTOR');
INSERT INTO artista (nome_artista, tipo_artista) VALUES ('Padre Marcelo Rossi', 'CANTOR');
INSERT INTO artista (nome_artista, tipo_artista) VALUES ('Sandy e Junior', 'BANDA');
INSERT INTO artista (nome_artista, tipo_artista) VALUES ('Djavan', 'BANDA');
INSERT INTO artista (nome_artista, tipo_artista) VALUES ('Roberto Carlos', 'CANTOR');