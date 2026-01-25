package com.amarildo.seletivo.resource;

import com.amarildo.seletivo.model.AlbumImagem;
import com.amarildo.seletivo.model.dto.AlbumImagemResponseDTO;
import com.amarildo.seletivo.service.AlbumImagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/albuns")
public class AlbumImagemResource {

    @Autowired
    private AlbumImagemService albumImagemService;


    @PostMapping(
            value = "/{idenAlbum}/imagens",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AlbumImagemResponseDTO> adicionarImagem(
            @PathVariable Long idenAlbum,
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "descricao", required = false) String descricao
    ) {

        AlbumImagemResponseDTO albumImagemDTO = albumImagemService.adicionarImagem(idenAlbum, file, descricao);

        return ResponseEntity.status(HttpStatus.CREATED).body(albumImagemDTO);
    }
}
