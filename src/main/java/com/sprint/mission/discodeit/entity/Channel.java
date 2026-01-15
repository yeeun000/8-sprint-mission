package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Channel implements Serializable {

  private static final long serialVersionUID = 1L;

  public enum ChannelType {
    PUBLIC,
    PRIVATE
  }

  private UUID id;
  private String channelName;
  private String description;
  private ChannelType type;
  private Instant createdAt;
  private Instant updatedAt;


  public Channel(ChannelType type, String name, String description) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.updatedAt = this.createdAt;
    this.type = type;
    this.channelName = name;
    this.description = description;
  }

  public static Channel createPrivateChannel() {
    return new Channel(ChannelType.PRIVATE, null, null);
  }

  public static Channel createPublicChannel(String name, String description) {
    return new Channel(ChannelType.PUBLIC, name, description);
  }

  public void update(String newchannelName, String newdescription) {
    this.channelName = newchannelName;
    this.description = newdescription;
    this.updatedAt = Instant.now();
  }


  @Override
  public String toString() {
    return "Channel{" +
        "id=" + id +
        ", channelName='" + channelName + '\'' +
        ", description='" + description + '\'' +
        ", type=" + type +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        '}';
  }
}
