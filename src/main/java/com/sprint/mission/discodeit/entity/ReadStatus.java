package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID userId;
    private UUID channelId;
    private Instant lastRead;

    public ReadStatus(UUID userId, UUID channelId, Instant lastRead) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.userId = userId;
        this.channelId = channelId;
        this.lastRead = lastRead;
    }


    public void update(Instant newlastRead) {
        this.lastRead = newlastRead;
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "ReadStatus{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", userId=" + userId +
                ", channelId=" + channelId +
                ", lastRead=" + lastRead +
                '}';
    }
}
