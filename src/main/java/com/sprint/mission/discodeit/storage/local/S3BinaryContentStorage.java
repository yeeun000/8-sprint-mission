package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final String bucket;
  private final long duration;
  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

@Autowired
  public S3BinaryContentStorage(
      @Value("${discodeit.storage.s3.access-key}") String accessKey,
      @Value("${discodeit.storage.s3.secret-key}") String secretKey,
      @Value("${discodeit.storage.s3.bucket}") String bucket,
      @Value("${discodeit.storage.s3.region}") String region,
      @Value("${discodeit.storage.s3.presigned-url-expiration:600}") long duration) {

    this(bucket, duration,
        S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .build(),
        S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .build());

  }

  //테스트용
  public S3BinaryContentStorage(String bucket, long duration, S3Client s3Client, S3Presigner s3Presigner) {
    this.bucket = bucket;
    this.duration = duration;
    this.s3Client = s3Client;
    this.s3Presigner = s3Presigner;
  }

  public UUID put(UUID id, byte[] bytes) {
    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucket)
        .key(id.toString())
        .build();
    RequestBody body = RequestBody.fromBytes(bytes);
    s3Client.putObject(request, body);
    return id;
  }

  public InputStream get(UUID uuid) {
    return s3Client.getObject(GetObjectRequest.builder()
        .bucket(bucket).
        key(uuid.toString())
        .build());
  }

  public ResponseEntity<Void> download(BinaryContentDto dto) {
    return ResponseEntity.status(HttpStatus.FOUND)
        .header("Location", generatePresignedUrl(dto.id().toString(), dto.contentType()))
        .build();
  }

  public S3Client getS3Client() {
    return s3Client;
  }

  public String generatePresignedUrl(String key, String contentType) {
    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .responseContentType(contentType)
        .build();

    GetObjectPresignRequest presign = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(duration))
        .getObjectRequest(request)
        .build();

    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presign);
    return presignedRequest.url().toString();
  }

}
