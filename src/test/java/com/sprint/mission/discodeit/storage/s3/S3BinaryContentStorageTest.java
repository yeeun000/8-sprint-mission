package com.sprint.mission.discodeit.storage.s3;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import com.sprint.mission.discodeit.storage.local.S3BinaryContentStorage;
import java.net.URL;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@ExtendWith(MockitoExtension.class)
public class S3BinaryContentStorageTest {

  @Mock
  private S3Client s3Client;

  @Mock
  private S3Presigner s3Presigner;

  private S3BinaryContentStorage storage;

  @BeforeEach
  void setup() {
    storage = new S3BinaryContentStorage(
        "testBucket",
        600,
        s3Client,
        s3Presigner
    );
  }

  @Test
  void putTest() {
    UUID uuid = UUID.randomUUID();
    byte[] bytes = "test".getBytes();
    storage.put(uuid, bytes);
    verify(s3Client, times(1))
        .putObject(any(PutObjectRequest.class), any(RequestBody.class));
  }

  @Test
  void getTest() {
    UUID uuid = UUID.randomUUID();
    storage.get(uuid);
    verify(s3Client, times(1))
        .getObject(any(GetObjectRequest.class));
  }

  @Test
  void downloadTest() throws Exception {
    UUID uuid = UUID.randomUUID();
    BinaryContentDto dto = new BinaryContentDto(uuid, "test.txt", 100L, "text/plain");

    PresignedGetObjectRequest mockRequest = mock(PresignedGetObjectRequest.class);

    when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class)))
        .thenReturn(mockRequest);

    when(mockRequest.url())
        .thenReturn(new URL("https://test.com"));

    ResponseEntity<Void> response = storage.download(dto);

    assertEquals(HttpStatus.FOUND, response.getStatusCode());
    assertEquals("https://test.com", response.getHeaders().getLocation().toString());

    verify(s3Presigner, times(1)).presignGetObject(any(GetObjectPresignRequest.class));
  }
}
