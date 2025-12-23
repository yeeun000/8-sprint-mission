package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private String password;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID profileId;

    public User(String name, String email, String password, UUID profileId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.name = name;
        this.password = password;
        this.email = email;
        this.profileId = profileId;
    }


    public void update(String name, String password, String email, UUID profileId) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.profileId = profileId;
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
