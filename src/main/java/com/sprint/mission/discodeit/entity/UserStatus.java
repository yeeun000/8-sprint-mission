package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class UserStatus {

    private UUID id;
    private Instant createAt;
    private Instant updateAt;
    private UUID userId;
    private Instant lastCome;
    private boolean online;

    public UserStatus(UUID userId){
        this.id=UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
        this.userId=userId;
        this.lastCome= Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getLastCome() {
        return lastCome;
    }

    public boolean isOnline() {
        return online;
    }

    public boolean accessTime(){
        Instant after=lastCome.plusSeconds(300);
        online=Instant.now().isBefore(after);
        return online;
    }

    public void setLastCome(Instant lastCome) {
        this.lastCome = lastCome;
    }

    @Override
    public String toString() {
        return "UserStatus{" +
                "id=" + id +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", userId=" + userId +
                ", lastCome=" + lastCome +
                ", online=" + online +
                '}';
    }
}
