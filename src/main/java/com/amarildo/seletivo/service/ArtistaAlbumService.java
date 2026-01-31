package com.amarildo.seletivo.service;

import com.amarildo.seletivo.model.enums.TipoArtista;
import com.amarildo.seletivo.model.Album;
import com.amarildo.seletivo.model.Artista;
import com.amarildo.seletivo.model.ArtistaAlbum;
import com.amarildo.seletivo.model.dto.ArtistaAlbumCreateDTO;
import com.amarildo.seletivo.model.dto.ArtistaAlbumListDTO;
import com.amarildo.seletivo.model.dto.ArtistaAlbumResponseDTO;
import com.amarildo.seletivo.repository.AlbumRepository;
import com.amarildo.seletivo.repository.ArtistaAlbumRepository;
import com.amarildo.seletivo.repository.ArtistaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
@Transactional
public class ArtistaAlbumService {

    @Autowired
    private ArtistaAlbumRepository artistaAlbumRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private AlbumRepository albumRepository;

    public List<ArtistaAlbumListDTO> listarTodos() {
        return artistaAlbumRepository.findAll()
                .stream()
                .map(aa -> new ArtistaAlbumListDTO(
                        aa.getIdenArtistaAlbum(),
                        aa.getArtista().getIdenArtista(),
                        aa.getArtista().getNomeArtista(),
                        aa.getAlbum().getIdenAlbum(),
                        aa.getAlbum().getDescricaoAlbum()
                ))
                .toList();
    }

    public ArtistaAlbumListDTO  buscarPorId(Long idenArtistaAlbum) {


        ArtistaAlbum artistaAlbum = artistaAlbumRepository.findById(idenArtistaAlbum)
                .orElseThrow(() ->
                        new EntityNotFoundException("Vínculo artista/álbum não encontrado"));

        return new ArtistaAlbumListDTO(
                artistaAlbum.getIdenArtistaAlbum(),
                artistaAlbum.getArtista().getIdenArtista(),
                artistaAlbum.getArtista().getNomeArtista(),
                artistaAlbum.getAlbum().getIdenAlbum(),
                artistaAlbum.getAlbum().getDescricaoAlbum()
        );
    }

    public ArtistaAlbumResponseDTO salvar(ArtistaAlbumCreateDTO artistaAlbumCreateDTO) {

        Artista artista = artistaRepository.findById(artistaAlbumCreateDTO.getIdenArtista())
                .orElseThrow(() ->
                        new EntityNotFoundException("Artista não encontrado"));

        Album album = albumRepository.findById(artistaAlbumCreateDTO.getIdenAlbum())
                .orElseThrow(() ->
                        new EntityNotFoundException("Álbum não encontrado"));

        boolean existe = artistaAlbumRepository.existsByArtistaAndAlbum(artista, album);

        if (existe) {
            throw new IllegalArgumentException("Já existe o vínculo artista / álbum");
        }

        ArtistaAlbum artistaAlbum = new ArtistaAlbum();
        artistaAlbum.setArtista(artista);
        artistaAlbum.setAlbum(album);

        ArtistaAlbum salvo = artistaAlbumRepository.save(artistaAlbum);

        return new ArtistaAlbumResponseDTO(
                salvo.getIdenArtistaAlbum(),
                artista.getIdenArtista(),
                album.getIdenAlbum()
        );
    }

    public List<ArtistaAlbumListDTO> buscarPorTipoArtista(TipoArtista tipo) {
        return artistaAlbumRepository.findByTipoArtista(tipo);
    }

    public List<ArtistaAlbumListDTO> consultar(TipoArtista tipo, String nomeArtista) {
        return artistaAlbumRepository.consultar(tipo, nomeArtista);
    }


}

