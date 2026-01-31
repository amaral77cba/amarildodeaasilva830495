package com.amarildo.seletivo.service;

import com.amarildo.seletivo.model.TipoAlbum;
import com.amarildo.seletivo.model.dto.TipoAlbumCreateDTO;
import com.amarildo.seletivo.model.dto.TipoAlbumUpdateDTO;
import com.amarildo.seletivo.repository.TipoAlbumRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoAlbumService {

    @Autowired
    private TipoAlbumRepository tipoAlbumRepository;

   public List<TipoAlbum> listarTodos() {
        return tipoAlbumRepository.findAll();
    }

   public TipoAlbum buscarPorId(Long idenTipoAlbum) {
        return tipoAlbumRepository.findById(idenTipoAlbum)
                .orElseThrow(() -> new EntityNotFoundException(
                        "TipoAlbum não encontrado para o id: " + idenTipoAlbum));
   }

   public TipoAlbum salvar(TipoAlbumCreateDTO dto) {

       boolean existe = tipoAlbumRepository.existsByDescricaoTipoAlbumIgnoreCase(dto.getDescricaoTipoAlbum());

       if (existe) {
           throw new IllegalArgumentException(
                   "Já existe um tipo de álbum cadastrado com essa descrição");
       }

       TipoAlbum tipoAlbum = new TipoAlbum();
       tipoAlbum.setDescricaoTipoAlbum(dto.getDescricaoTipoAlbum());

       return tipoAlbumRepository.save(tipoAlbum);
    }

   public TipoAlbum atualizar(Long idenTipoAlbum, TipoAlbumUpdateDTO tipoAlbumAtualizado) {
        TipoAlbum tipoAlbumAux = buscarPorId(idenTipoAlbum);

       if (!tipoAlbumAux.getDescricaoTipoAlbum()
               .equalsIgnoreCase(tipoAlbumAtualizado.getDescricaoTipoAlbum())) {

           boolean descricaoJahExiste = tipoAlbumRepository.existsByDescricaoTipoAlbumIgnoreCaseAndIdenTipoAlbumNot(tipoAlbumAtualizado.getDescricaoTipoAlbum(), idenTipoAlbum);

           if (descricaoJahExiste) {
               throw new IllegalArgumentException(
                       "Já existe outro tipo de álbum com essa descrição");
           }
       }

        tipoAlbumAux.setDescricaoTipoAlbum(tipoAlbumAtualizado.getDescricaoTipoAlbum());

        return tipoAlbumRepository.save(tipoAlbumAux);
   }

}

