package com.miranda.opencord.storage.infrastructure.service;

import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioStorageService {

    private final MinioClient minioClient;

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @PostConstruct
    public void init() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket MinIO '{}' criado com sucesso.", bucketName);
            }

            // Define política de leitura pública para o bucket
            String policy = """
                {
                    "Version": "2012-10-17",
                    "Statement": [
                        {
                            "Effect": "Allow",
                            "Principal": {"AWS": ["*"]},
                            "Action": ["s3:GetObject"],
                            "Resource": ["arn:aws:s3:::%s/*"]
                        }
                    ]
                }
                """.formatted(bucketName);

            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                            .bucket(bucketName)
                            .config(policy)
                            .build()
            );
            log.info("Política de leitura pública configurada para o bucket MinIO '{}'.", bucketName);
        } catch (Exception e) {
            log.error("Erro ao inicializar bucket no MinIO: {}", e.getMessage(), e);
        }
    }

    public String uploadAvatar(UUID userId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo de imagem não pode estar vazio.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("O arquivo deve ser uma imagem válida (PNG, JPG, WEBP, GIF).");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "png";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }

        String objectName = "avatars/" + userId + "-" + System.currentTimeMillis() + "." + extension;

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(contentType)
                            .build()
            );

            return minioUrl + "/" + bucketName + "/" + objectName;
        } catch (Exception e) {
            log.error("Erro ao fazer upload da imagem para o MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao salvar a imagem no servidor de armazenamento: " + e.getMessage());
        }
    }

    public com.miranda.opencord.message.application.dto.MessageAttachmentDto uploadAttachment(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo enviado está vazio.");
        }

        long maxBytes = 100 * 1024 * 1024L; // 100MB
        if (file.getSize() > maxBytes) {
            throw new IllegalArgumentException("O arquivo ultrapassa o limite máximo de 100MB.");
        }

        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "arquivo";
        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";
        String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");

        String objectName = "attachments/" + UUID.randomUUID() + "/" + sanitizedFilename;

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(contentType)
                            .build()
            );

            String fileUrl = minioUrl + "/" + bucketName + "/" + objectName;

            return new com.miranda.opencord.message.application.dto.MessageAttachmentDto(
                    fileUrl,
                    originalFilename,
                    file.getSize(),
                    contentType
            );
        } catch (Exception e) {
            log.error("Erro ao fazer upload de anexo para o MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao salvar o anexo: " + e.getMessage());
        }
    }
}
