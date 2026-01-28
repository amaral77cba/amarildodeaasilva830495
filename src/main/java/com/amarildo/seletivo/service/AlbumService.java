package com.amarildo.seletivo.service;

import com.amarildo.seletivo.model.Album;
import com.amarildo.seletivo.model.TipoAlbum;
import com.amarildo.seletivo.model.dto.AlbumCreateDTO;
import com.amarildo.seletivo.model.dto.AlbumResponseDTO;
import com.amarildo.seletivo.repository.AlbumRepository;
import com.amarildo.seletivo.repository.TipoAlbumRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TipoAlbumRepository tipoAlbumRepository;

    public List<AlbumResponseDTO> listarTodos() {
        return albumRepository.findAll()
                .stream()
                .map(album -> new AlbumResponseDTO(
                        album.getIdenAlbum(),
                        album.getDescricaoAlbum(),
                        album.getTipoAlbum().getIdenTipoAlbum(),
                        album.getTipoAlbum().getDescricaoTipoAlbum()
                ))
                .toList();
    }

    public AlbumResponseDTO buscarPorId(Long idenAlbum) {
        Album album = buscarEntidadePorId(idenAlbum);

        return new AlbumResponseDTO(
                album.getIdenAlbum(),
                album.getDescricaoAlbum(),
                album.getTipoAlbum().getIdenTipoAlbum(),
                album.getTipoAlbum().getDescricaoTipoAlbum()
        );
    }

    public List<AlbumResponseDTO> listarPorTipo(Long idenTipoAlbum) {

        return albumRepository.findByTipoAlbum_IdenTipoAlbum(idenTipoAlbum)
                .stream()
                .map(album -> new AlbumResponseDTO(
                        album.getIdenAlbum(),
                        album.getDescricaoAlbum(),
                        album.getTipoAlbum().getIdenTipoAlbum(),
                        album.getTipoAlbum().getDescricaoTipoAlbum()
                ))
                .toList();
    }


    public AlbumResponseDTO salvar(AlbumCreateDTO albumCriado) {

        boolean existe = albumRepository.existsByDescricaoAlbumIgnoreCase(albumCriado.getDescricaoAlbum());

        if (existe) {
            throw new IllegalArgumentException("Já existe um álbum cadastrado com essa descrição");
        }

        TipoAlbum tipoAlbum = tipoAlbumRepository.findById(albumCriado.getIdenTipoAlbum())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de álbum não encontrado"));

        Album album = new Album();
        album.setDescricaoAlbum(albumCriado.getDescricaoAlbum());
        album.setTipoAlbum(tipoAlbum);

        Album salvo = albumRepository.save(album);

        return new AlbumResponseDTO(
                salvo.getIdenAlbum(),
                salvo.getDescricaoAlbum(),
                tipoAlbum.getIdenTipoAlbum(),
                tipoAlbum.getDescricaoTipoAlbum()
        );
    }

    public Album buscarEntidadePorId(Long idenAlbum) {
        return albumRepository.findById(idenAlbum)
                .orElseThrow(() ->
                        new EntityNotFoundException("Álbum não encontrado para o id: " + idenAlbum));
    }
    public AlbumResponseDTO atualizar(Long idenAlbum, AlbumCreateDTO albumAtualizado) {

        Album albumAux = buscarEntidadePorId(idenAlbum);

        // verifica se a descrição foi alterada
        if (!albumAux.getDescricaoAlbum()
                .equalsIgnoreCase(albumAtualizado.getDescricaoAlbum())) {

            boolean descricaoJahExiste = albumRepository.existsByDescricaoAlbumIgnoreCaseAndIdenAlbumNot(albumAtualizado.getDescricaoAlbum(), idenAlbum);

            if (descricaoJahExiste) {
                throw new IllegalArgumentException("Já existe outro álbum cadastrado com essa descrição");
            }
        }

        TipoAlbum tipoAlbum = tipoAlbumRepository
                .findById(albumAtualizado.getIdenTipoAlbum())
                .orElseThrow(() ->
                        new EntityNotFoundException("Tipo de álbum não encontrado"));


        // atualiza campos permitidos
        albumAux.setDescricaoAlbum(albumAtualizado.getDescricaoAlbum());
        albumAux.setTipoAlbum(tipoAlbum);

        Album salvo = albumRepository.save(albumAux);

        return new AlbumResponseDTO(
                salvo.getIdenAlbum(),
                salvo.getDescricaoAlbum(),
                salvo.getTipoAlbum().getIdenTipoAlbum(),
                salvo.getTipoAlbum().getDescricaoTipoAlbum()
        );
    }

    public Page<AlbumResponseDTO> listarTodosPaginado(Pageable pageable) {

        return albumRepository.findAll(pageable)
                .map(album -> new AlbumResponseDTO(
                        album.getIdenAlbum(),
                        album.getDescricaoAlbum(),
                        album.getTipoAlbum().getIdenTipoAlbum(),
                        album.getTipoAlbum().getDescricaoTipoAlbum()
                ));
    }
}

