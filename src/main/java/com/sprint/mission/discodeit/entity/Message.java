package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String content;
    private UUID channelId;
    private UUID userId;
    private Instant createdAt;
    private Instant updatedAt;
    private List<UUID> attachmentIds;

    public Message(String content, UUID channelId, UUID userId, List<UUID> attachmentIds) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.content = content;
        this.userId = userId;
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
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
