package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends BaseUpdatableEntity {

  @Column(name = "username", nullable = false, length = 50, unique = true)
  private String username;

  @Column(name = "password", nullable = false, length = 60)
  private String password;

  @Column(name = "email", nullable = false, length = 100, unique = true)
  private String email;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "profile_id", unique = true)
  private BinaryContent profile;

  @OneToOne(mappedBy = "user", orphanRemoval = true)
  private UserStatus status;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReadStatus> readStatuses = new ArrayList<>();

  public void update(String username, String email, String password, BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  public User(String username, String email, String password, BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }


  public static User create(String username, String email, String password) {
    return new User(username, email, password, null);
  }


  public static User createProfile(String username, String email, String password,
      BinaryContent profile) {
    return new User(username, email, password, profile);
  }

}
