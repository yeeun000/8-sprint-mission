package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class BinaryContent {

    private UUID id;
    private Instant createdAt;
    private UUID userId;
    private UUID messageId;
    private String fileName;
    private String filePath;

    public BinaryContent(UUID userId, String fileName, String filePath){
        this.id= UUID.randomUUID();
        this.createdAt=Instant.now();
        this.userId=userId;
        this.fileName=fileName;
        this.filePath=filePath;
    }

    public BinaryContent( UUID messageId, String fileName, String filePath, boolean t){
        this.id= UUID.randomUUID();
        this.createdAt=Instant.now();
        this.messageId=messageId;
        this.fileName=fileName;
        this.filePath=filePath;
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String toString() {
        return "BinaryContent{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", userId=" + userId +
                ", messageId=" + messageId +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
