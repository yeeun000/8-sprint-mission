package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class ReadStatus {

    private UUID id;
    private Instant createAt;
    private Instant updateAt;
    private UUID userId;
    private UUID channelId;
    private Instant lastRead;

    public ReadStatus(UUID userId, UUID channelId){
        this.id=UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
        this.userId=userId;
        this.channelId= channelId;
        this.lastRead= Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public Instant getLastRead() {
        return lastRead;
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
