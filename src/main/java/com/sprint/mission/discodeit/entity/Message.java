package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.FileDTO;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String contents;
    private UUID channeld;
    private UUID userId;
    private Instant createAt;
    private Instant updateAt;
    private List<UUID> attachmentlds;

    public Message(String contents, UUID userId, UUID channeld, List<FileDTO> files) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
        this.contents = contents;
        this.userId = userId;
        this.channeld = channeld;
        if(files != null && !files.isEmpty())
            this.attachmentlds=files.stream().map(FileDTO::id).toList();
    }

    public UUID getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChanneld() {
        return channeld;
    }

    public List<UUID> getAttachmentlds() {
        return attachmentlds;
    }

    public void update(String contents, List<UUID> attachmentlds){
        this.contents=contents;
        if(attachmentlds != null){
           this.attachmentlds=attachmentlds;
        }
        this.updateAt = Instant.now();
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", channeld=" + channeld +
                ", userId=" + userId +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
