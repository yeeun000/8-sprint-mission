package com.sprint.mission.discodeit.storage.s3;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class AWSS3TEST {

  private S3Client s3Client;
  private String bucketName;
  private S3Presigner s3Presigner;

  @BeforeEach
  void setUp() throws Exception {
    Properties prop = new Properties();
    prop.load(new FileInputStream(".env"));

    String accessKey = prop.getProperty("AWS_ACCESS_KEY_ID");
    String secretKey = prop.getProperty("AWS_SECRET_ACCESS_KEY");
    this.bucketName = prop.getProperty("AWS_S3_BUCKET");
    String region = prop.getProperty("AWS_S3_REGION");

    s3Client = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)
        ))
        .build();

    s3Presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)
        ))
        .build();

  }

  @Test
  void upload() {
    Path path = Paths.get(".discodeit/storage/test.txt");
    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucketName)
        .key("test")
        .contentType("text/plain")
        .build();
    RequestBody body = RequestBody.fromFile(path);
    s3Client.putObject(request, body);
  }

  @Test
  void download() {
    Path path = Paths.get(".discodeit/storage/test2.txt");
    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(bucketName)
        .key("test")
        .build();
    s3Client.getObject(request, path);
  }

  @Test
  void PresignedUrl() {
    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(bucketName)
        .key("test")
        .build();

    GetObjectPresignRequest presign = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))
        .getObjectRequest(request)
        .build();

    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presign);
    String url = presignedRequest.url().toString();
    System.out.println(url);
  }
}
