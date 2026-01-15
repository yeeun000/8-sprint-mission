package com.sprint.mission.discodeit.entity;


import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;

  private UUID id;
  private String content;
  private UUID channelId;
  private UUID authorId;
  private Instant createdAt;
  private Instant updatedAt;
  private List<UUID> attachmentIds;

  public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.updatedAt = this.createdAt;
    this.content = content;
    this.authorId = authorId;
    this.channelId = channelId;
    this.attachmentIds = attachmentIds;
  }

  public void update(String content) {
    this.content = content;
    this.updatedAt = Instant.now();
  }

  @Override
  public String toString() {
    return "Message{" +
        "id=" + id +
        ", content='" + content + '\'' +
        ", channelId=" + channelId +
        ", authorId=" + authorId +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        '}';
  }
}
