package com.amarildo.seletivo.service;

import com.amarildo.seletivo.model.Album;
import com.amarildo.seletivo.model.AlbumImagem;
import com.amarildo.seletivo.model.Arquivo;
import com.amarildo.seletivo.model.dto.AlbumImagemResponseDTO;
import com.amarildo.seletivo.repository.AlbumImagemRepository;
import com.amarildo.seletivo.repository.AlbumRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AlbumImagemService {

    @Autowired
    private AlbumImagemRepository albumImagemRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArquivoService arquivoService;


    @Transactional
    public AlbumImagemResponseDTO adicionarImagem(Long idenAlbum, MultipartFile file, String descricao) {

        // 1. Buscar o álbum
        Album album = albumRepository.findById(idenAlbum)
                .orElseThrow(() -> new EntityNotFoundException("Álbum não encontrado"));

        // 2. Salvar o arquivo (MinIO + tabela ARQUIVO)
        Arquivo arquivo = arquivoService.salvarArquivo(file);

        // 3. Criar vínculo Album_Imagem
        AlbumImagem albumImagem = new AlbumImagem();
        albumImagem.setAlbum(album);
        albumImagem.setArquivo(arquivo);
        albumImagem.setDataAlbumImagem(OffsetDateTime.now());

        // 4. Persistir
        albumImagem = albumImagemRepository.save(albumImagem);

        return new AlbumImagemResponseDTO(
                albumImagem.getIdenAlbumImagem(),
                album.getIdenAlbum(),
                arquivo.getUuidArquivo(),
                arquivo.getIdenArquivo(),
                albumImagem.getDataAlbumImagem()
        );
    }


    public List<AlbumImagem> adicionarImagens(Long idenAlbum, List<MultipartFile> files) {

        Album album = albumRepository.findById(idenAlbum)
                .orElseThrow(() -> new EntityNotFoundException("Álbum não encontrado"));

        List<AlbumImagem> imagens = new ArrayList<>();

        for (MultipartFile file : files) {
            Arquivo arquivo = arquivoService.salvarArquivo(file);

            AlbumImagem albumImagem = new AlbumImagem();
            albumImagem.setAlbum(album);
            albumImagem.setArquivo(arquivo);
            //albumImagem.setDataAlbumImagem(OffsetDateTime.now()); //nao precisa setar, pois jah tem o DEFAULT no banco

            imagens.add(albumImagemRepository.save(albumImagem));
        }

        return imagens;
    }


    public List<AlbumImagem> listarPorAlbum(Long idenAlbum) {
        return albumImagemRepository
                .findByAlbum_IdenAlbumOrderByDataAlbumImagemAsc(idenAlbum);
    }


//    public void removerImagem(Long idenAlbumImagem) {
//
//        AlbumImagem albumImagem = albumImagemRepository.findById(idenAlbumImagem)
//                .orElseThrow(() -> new EntityNotFoundException("Imagem não encontrada"));
//
//        // Remove vínculo
//        albumImagemRepository.delete(albumImagem);
//
//        // Remove arquivo (MinIO + tabela ARQUIVO)
//        arquivoService.removerArquivo(albumImagem.getArquivo());
//    }

}
