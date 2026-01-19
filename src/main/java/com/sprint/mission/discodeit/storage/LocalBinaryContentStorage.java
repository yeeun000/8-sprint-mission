package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Paths.get(rootPath);
  }

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(root);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    try {
      Files.write(resolvePath(id), bytes);
      return id;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public InputStream get(UUID id) {
    try {
      return Files.newInputStream(resolvePath(id));
    } catch (IOException e) {
      return null;
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto dto) {
    InputStream inputStream = get(dto.id());
    if (inputStream == null) {
      return ResponseEntity.notFound().build();
    }

    Resource resource = new InputStreamResource(inputStream);

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(dto.contentType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline() // attachment 아님!
            .filename(dto.fileName())
            .build().toString())
        .body(resource);
  }
}
