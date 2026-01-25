package com.amarildo.seletivo.service;

import com.amarildo.seletivo.model.Arquivo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class ArquivoStorageService {

    @Autowired
    private MinioService minioService;

    @Autowired
    private ArquivoService arquivoService;

    @Transactional
    public Arquivo upload(MultipartFile file, String descricao) {

        String objectName = null;

        try {
            // 1. Upload no MinIO
            objectName = minioService.uploadArquivo(file);

            // 2. Monta entidade
            Arquivo arquivo = new Arquivo();
            arquivo.setUuidArquivo(UUID.randomUUID());
            arquivo.setNomeArquivo(file.getOriginalFilename());
            arquivo.setDescricaoArquivo(descricao);
            arquivo.setExtensaoArquivo(extrairExtensao(file.getOriginalFilename()));
            arquivo.setContentType(file.getContentType());
            //arquivo.setBucket("arquivos");
            arquivo.setBucket("bancoseletivo-arquivos");
            arquivo.setObjectName(objectName);
            arquivo.setTamanhoBytes(file.getSize());

            // 3. Salva no banco usando seu service atual
            return arquivoService.salvar(arquivo);

        } catch (Exception e) {
            // rollback manual do MinIO
            if (objectName != null) {
                minioService.removerArquivo(objectName);
            }
            throw new RuntimeException("Erro ao salvar arquivo", e);
        }
    }

    public void remover(Long idenArquivo) {
        Arquivo arquivo = arquivoService.buscarPorId(idenArquivo);

        minioService.removerArquivo(arquivo.getObjectName());
        arquivoService.excluir(idenArquivo);
    }

    public String gerarUrlDownload(UUID uuidArquivo, int minutos) {
        Arquivo arquivo = arquivoService.buscarPorUuid(uuidArquivo);
        return minioService.gerarUrlDownload(arquivo.getObjectName(), minutos);
    }

    private String extrairExtensao(String nome) {
        if (nome == null || !nome.contains(".")) {
            return null;
        }
        return nome.substring(nome.lastIndexOf('.') + 1);
    }

}
