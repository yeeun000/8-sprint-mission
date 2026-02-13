package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") Path root) {
    this.root = root;
  }

  @PostConstruct
  public void init() {
    if (!Files.exists(root)) {
      try {
        Files.createDirectories(root);
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    Path filePath = resolvePath(id);
    if (Files.exists(filePath)) {
      throw new IllegalArgumentException("파일이 존재하지 않습니다.");
    }
    try (OutputStream outputStream = Files.newOutputStream(filePath)) {
      outputStream.write(bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    Path filePath = resolvePath(id);
    if (Files.notExists(filePath)) {
      throw new NoSuchElementException("파일이 존재하지 않습니다.");
    }
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto dto) {
    InputStream inputStream = get(dto.id());
    Resource resource = new InputStreamResource(inputStream);

    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + dto.fileName() + "\"")
        .header(HttpHeaders.CONTENT_TYPE, dto.contentType())
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(dto.size()))
        .body(resource);
  }
}
