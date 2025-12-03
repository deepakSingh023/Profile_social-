package com.example.Social.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import jakarta.annotation.PostConstruct;

import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class R2ImageService {

    @Value("${cloudflare.r2.bucket-name}")
    private String bucketName;

    @Value("${cloudflare.r2.access-key}")
    private String accessKey;

    @Value("${cloudflare.r2.secret-key}")
    private String secretKey;

    @Value("${cloudflare.r2.public-base-url}")
    private String publicUrl;

    @Value("${cloudflare.r2.endpoint}")
    private String endpoint;

    private S3Client r2Client;

    @PostConstruct
    public void init() {
        r2Client = S3Client.builder()
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .region(Region.US_EAST_1) // required by AWS SDK
                .endpointOverride(URI.create(endpoint))
                .build();
    }

    public String uploadProfilePic(MultipartFile file) {
        try {
            String key = "profile_pics/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

            r2Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .contentType(file.getContentType())
                            .key(key)
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );

            return publicUrl + "/" + key;

        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }

    public void deleteImage(String url) {
        if (url == null || url.isEmpty()) return;

        try {
            String key = url.replace(publicUrl + "/", "");

            r2Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build()
            );

        } catch (Exception e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }
}
