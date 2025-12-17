package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String contents;
    private UUID channelId;
    private UUID userId;
    private Instant createAt;
    private Instant updateAt;
    private List<UUID> attachmentlds;

    public Message(String contents, UUID userId, UUID channelId, List<BinaryContentDTO> files) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
        this.contents = contents;
        this.userId = userId;
        this.channelId = channelId;
        if (files != null && !files.isEmpty())
            this.attachmentlds = files.stream().map(BinaryContentDTO::id).toList();
    }




    public void update(String contents, List<UUID> attachmentlds) {
        this.contents = contents;
        if (attachmentlds != null) {
            this.attachmentlds = attachmentlds;
        }
        this.updateAt = Instant.now();
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", channelId=" + channelId +
                ", userId=" + userId +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
