package com.amarildo.seletivo.service;

import com.amarildo.seletivo.model.Arquivo;
import com.amarildo.seletivo.model.dto.ArquivoPresignedUrlDTO;
import com.amarildo.seletivo.repository.ArquivoRepository;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.http.Method;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@Transactional
public class ArquivoService {

    @Value("${minio.presigned.expiry-minutes}")
    private int expiracaoMinutos;

    @Autowired
    private ArquivoRepository arquivoRepository;

    @Autowired
    private MinioService minioService;

    @Autowired
    private MinioClient minioClient;

    public Arquivo salvar(Arquivo arquivo) {
        if (arquivo.getUuidArquivo() == null) {
            arquivo.setUuidArquivo(UUID.randomUUID());
        }
        return arquivoRepository.save(arquivo);
    }


    @Transactional(readOnly = true)
    public Arquivo buscarPorId(Long idenArquivo) {
        return arquivoRepository.findById(idenArquivo)
                .orElseThrow(() -> new EntityNotFoundException("Arquivo não encontrado"));
    }

    @Transactional(readOnly = true)
    public Arquivo buscarPorUuid(UUID uuidArquivo) {
        return arquivoRepository.findByUuidArquivo(uuidArquivo)
                .orElseThrow(() -> new EntityNotFoundException("Arquivo não encontrado"));
    }

    public void excluir(Long idenArquivo) {
        Arquivo arquivo = buscarPorId(idenArquivo);
        arquivoRepository.delete(arquivo);
    }

    public Resource download(Arquivo arquivo) {
        return minioService.download(
                arquivo.getBucket(),
                arquivo.getObjectName()
        );
    }


    public Arquivo salvarArquivo(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo inválido");
        }

        try {
            // 1. Gerar UUID
            UUID uuid = UUID.randomUUID();

            // 2. Extrair dados do arquivo
            String nomeOriginal = file.getOriginalFilename();
            String extensao = "";

            if (nomeOriginal != null && nomeOriginal.contains(".")) {
                extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            }

            // 3. Definir bucket e objectName
            String bucket = "bancoseletivo-arquivos"; //"album-imagens"; //
            String objectName = uuid + extensao;

            // 4. Upload no MinIO
            minioService.upload(
                    //bucket,
                    objectName,
                    file.getInputStream(),
                    file.getContentType(),
                    file.getSize()
            );

            // 5. Persistir metadados
            Arquivo arquivo = new Arquivo();
            arquivo.setUuidArquivo(uuid);
            arquivo.setNomeArquivo(nomeOriginal);
            arquivo.setExtensaoArquivo(extensao);
            arquivo.setContentType(file.getContentType());
            arquivo.setBucket(bucket);
            arquivo.setObjectName(objectName);
            arquivo.setTamanhoBytes(file.getSize());

            return arquivoRepository.save(arquivo);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar arquivo", e);
        }
    }


    public ArquivoPresignedUrlDTO gerarLinkDownload(UUID uuid) {

        Arquivo arquivo = arquivoRepository.findByUuidArquivo(uuid)
                .orElseThrow(() ->
                        new EntityNotFoundException("Arquivo não encontrado"));

        try {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(arquivo.getBucket())
                            .object(arquivo.getObjectName())
                            .expiry(expiracaoMinutos * 60)
                            .build()
            );

            return new ArquivoPresignedUrlDTO(url, expiracaoMinutos);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar link pré-assinado", e);
        }
    }


}
