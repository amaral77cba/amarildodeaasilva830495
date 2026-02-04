package com.amarildo.seletivo.service;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public MinioService(
            @Value("${minio.url}") String url,
            @Value("${minio.access-key}") String accessKey,
            @Value("${minio.secret-key}") String secretKey
    ) {
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    //Upload do arquivo para o MinIO
    public String uploadArquivo(MultipartFile file) {
        try {
            String objectName = gerarObjectName(file.getOriginalFilename());

            InputStream inputStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return objectName;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload do arquivo no MinIO", e);
        }
    }

    //Remove o arquivo do MinIO
    public void removerArquivo(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover arquivo do MinIO", e);
        }
    }

    //Gera URL temporaria para download
    public String gerarUrlDownload(String objectName, int minutosExpiracao) {
        try {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(minutosExpiracao * 60)
                            .build()
            );
            // troca apenas o host
            url = url.replace("http://minio:9000/", "http://localhost/minio/");
            System.out.println("###TrocadeURL_PontoDois");

            return url;

//            return minioClient.getPresignedObjectUrl(
//                    GetPresignedObjectUrlArgs.builder()
//                            .bucket(bucket)
//                            .object(objectName)
//                            .method(Method.GET)
//                            .expiry(minutosExpiracao * 60)
//                            .build()
//            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar URL de download", e);
        }
    }

    private String gerarObjectName(String nomeOriginal) {
        return UUID.randomUUID() + "_" + nomeOriginal;
    }

    public Resource download(String bucket, String objectName) {
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );

            return new InputStreamResource(inputStream);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao baixar arquivo do MinIO", e);
        }
    }

//    public void upload(String objectName,
//                       InputStream inputStream,
//                       String contentType,
//                       long size) {
//
//        try {
//            minioClient.putObject(
//                    PutObjectArgs.builder()
//                            .bucket(bucket)
//                            .object(objectName)
//                            .stream(inputStream, size, -1)
//                            .contentType(contentType)
//                            .build()
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("Erro ao enviar arquivo para o MinIO", e);
//        }
//    }

    public void upload(String bucket,
                       String objectName,
                       InputStream inputStream,
                       String contentType,
                       long size) {

        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucket)
                            .build()
            );

            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucket)
                                .build()
                );
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar arquivo ao MinIO", e);
        }
    }



}
