package com.amarildo.seletivo.service;

import com.amarildo.seletivo.model.TipoAlbum;
import com.amarildo.seletivo.model.dto.ArtistaCreateDTO;
import com.amarildo.seletivo.model.dto.ArtistaUpdateDTO;
import com.amarildo.seletivo.model.enums.TipoArtista;
import com.amarildo.seletivo.model.Artista;
import com.amarildo.seletivo.repository.ArtistaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ArtistaService {

    @Autowired
    private ArtistaRepository artistaRepository;

    public Artista salvar(ArtistaCreateDTO dto){

        boolean existeArtista = artistaRepository.existsByNomeArtistaIgnoreCase(dto.getNomeArtista());

        if (existeArtista) {
            throw new IllegalArgumentException("Já existe um artista cadastrado com esse nome");
        }

        Artista artista = new Artista();
        artista.setNomeArtista(dto.getNomeArtista());
        artista.setTipoArtista(dto.getTipoArtista());

        return artistaRepository.save(artista);
    }

    public Artista buscarPorId(Long idenArtista) {
        return artistaRepository.findById(idenArtista)
                .orElseThrow(() ->
                        new EntityNotFoundException("Artista não encontrado para o id: " + idenArtista));
    }

    public List<Artista> listarTodos() {
        return artistaRepository.findAll();
    }

    public List<Artista> listarPorTipo(TipoArtista tipoArtista, Sort.Direction direction) {
        Sort sort = Sort.by(direction, "nomeArtista");
        return artistaRepository.findByTipoArtista(tipoArtista, sort);
    }

    public Artista atualizar(Long idenArtista, ArtistaUpdateDTO artistaAtualizado) {
        Artista artistaAux = buscarPorId(idenArtista);

        //verifica se o nome foi alterado
        if (!artistaAux.getNomeArtista().equalsIgnoreCase(artistaAtualizado.getNomeArtista())){
            boolean nomeJahExistente = artistaRepository.existsByNomeArtistaIgnoreCaseAndIdenArtistaNot(artistaAtualizado.getNomeArtista(), idenArtista);

            if (nomeJahExistente){
                throw new IllegalArgumentException("Já existe outro artista cadastrado com esse nome");
            }
        }

        // atualiza todos os campos exceto a PK
        artistaAux.setNomeArtista(artistaAtualizado.getNomeArtista());
        artistaAux.setTipoArtista(artistaAtualizado.getTipoArtista());

        return artistaRepository.save(artistaAux);
    }

    public Artista atualizarv2(Long idenArtista, ArtistaUpdateDTO dto) {
        Artista artistaAux = buscarPorId(idenArtista);

        // verifica se o nome foi alterado
        if (dto.getNomeArtista() != null &&
                !artistaAux.getNomeArtista().equalsIgnoreCase(dto.getNomeArtista())) {

            boolean nomeJahExiste = artistaRepository.existsByNomeArtistaIgnoreCaseAndIdenArtistaNot(dto.getNomeArtista(), idenArtista);

            if (nomeJahExiste) {
                throw new IllegalArgumentException(
                        "Já existe outro artista cadastrado com esse nome");
            }
        }

        BeanUtils.copyProperties(dto, artistaAux, "idenArtista");

        return artistaRepository.save(artistaAux);
    }
}
