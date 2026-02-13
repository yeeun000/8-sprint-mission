package com.sprint.mission.discodeit.repositoryTest;

import static com.sprint.mission.discodeit.entity.Channel.ChannelType.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
public class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @PersistenceContext
  EntityManager em;

  @Test
  @DisplayName("채널 메시지를 작성자 정보와 함께 조회할 수 있다.")
  void findAllByChannelIdWithAuthor_success() {
    User user = new User("username", "email", "password", null);
    em.persist(user);

    UserStatus status = new UserStatus(user, Instant.now());
    em.persist(status);

    Channel channel = new Channel(PUBLIC, "name", "desc");
    em.persist(channel);

    Message message = new Message("hi", channel, user, List.of());
    em.persist(message);

    em.flush();
    em.clear();

    Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(),
        Instant.now().plusSeconds(1),
        PageRequest.of(0, 10)
    );

    assertThat(result.getContent()).hasSize(1);
    Message found = result.getContent().get(0);
    assertThat(found.getAuthor()).isNotNull();
    assertThat(found.getAuthor().getStatus()).isNotNull();
  }


  @Test
  @DisplayName("메시지가 없으면 빈 결과를 반환한다.")
  void findAllByChannelIdWithAuthor_fail() {

    Channel channel = new Channel(Channel.ChannelType.PUBLIC, "name", "desc");
    em.persist(channel);

    em.flush();
    em.clear();

    Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(),
        Instant.now(),
        PageRequest.of(0, 10)
    );

    assertThat(result.getContent()).isEmpty();
  }

}
