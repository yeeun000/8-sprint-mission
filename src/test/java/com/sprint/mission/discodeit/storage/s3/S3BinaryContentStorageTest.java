package com.sprint.mission.discodeit.storage.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@SpringBootTest
@EnabledIf(expression = "#{environment['discodeit.storage.type'] == 's3'}", loadContext = true)
@ActiveProfiles("test")
public class S3BinaryContentStorageTest {

  @Autowired
  private S3BinaryContentStorage s3BinaryContentStorage;

  @Value("${discodeit.storage.s3.bucket}")
  private String bucket;

  private UUID testId;
  private byte[] testData;

  @BeforeEach
  void setUp() throws Exception {
    testId = UUID.randomUUID();
    testData = "test".getBytes();
  }

  @AfterEach
  void tearDown() throws Exception {
    try {
      s3BinaryContentStorage.getS3Client().deleteObject(DeleteObjectRequest.builder()
          .bucket(bucket)
          .key(testId.toString())
          .build());
    } catch (Exception e) {
    }
  }

  @Test
  void put_success() throws Exception {
    UUID id = s3BinaryContentStorage.put(testId, testData);
    assertThat(id).isEqualTo(testId);

    try (InputStream is = s3BinaryContentStorage.get(testId)) {
      byte[] downloadedData = is.readAllBytes();
      assertThat(downloadedData).isEqualTo(testData);
    }
  }

  @Test
  void get_not_found() throws Exception {
    assertThatThrownBy(() -> s3BinaryContentStorage.get(UUID.randomUUID()))
        .isInstanceOf(NoSuchKeyException.class);
  }

  @Test
  void download_success() throws Exception {
    s3BinaryContentStorage.put(testId, testData);
    BinaryContentDto dto = new BinaryContentDto(
        testId, "test", (long) testData.length, "test/plain"
    );

    ResponseEntity<Void> response = s3BinaryContentStorage.download(dto);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    assertThat(response.getHeaders().get(HttpHeaders.LOCATION)).isNotNull();

    String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);
    assertThat(location).contains(bucket);
    assertThat(location).contains(testId.toString());
  }

}
