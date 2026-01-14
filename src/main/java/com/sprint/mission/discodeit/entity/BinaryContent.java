package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID id;
  private Instant createdAt;
  private String fileName;
  private String Type;
  private byte[] bytes;
  private Long size;

  public BinaryContent(String fileName, Long size, String type, byte[] bytes) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.fileName = fileName;
    this.size = size;
    this.Type = type;
    this.bytes = bytes;
  }
}
