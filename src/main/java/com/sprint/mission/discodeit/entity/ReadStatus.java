package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createAt;
    private Instant updateAt;
    private UUID userId;
    private UUID channelId;
    private Instant lastRead;

    public ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
        this.userId = userId;
        this.channelId = channelId;
        this.lastRead = Instant.now();
    }


    public void setLastRead(Instant lastRead) {
        this.lastRead = lastRead;
    }

    @Override
    public String toString() {
        return "ReadStatus{" +
                "id=" + id +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", userId=" + userId +
                ", channelId=" + channelId +
                ", lastRead=" + lastRead +
                '}';
    }
}
