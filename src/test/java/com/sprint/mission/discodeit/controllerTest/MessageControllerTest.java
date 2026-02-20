package com.sprint.mission.discodeit.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.dto.messageDTO.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BasicMessageService messageService;

  @BeforeEach
  @DisplayName("테스트 환경 설정 확인")
  void setUp() {
    assert mockMvc != null;
    assert messageService != null;
  }

  @Test
  @DisplayName("메시지 조회 성공")
  void find_success() throws Exception {
    UserDto userDto = new UserDto(
        UUID.randomUUID(),
        "username",
        "test",
        null,
        true
    );
    UUID channelId = UUID.randomUUID();
    MessageDto messageDto = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        Instant.now(),
        "content",
        channelId,
        userDto,
        List.of()
    );
    Instant cursor = Instant.now();
    PageResponse<MessageDto> pageResponse = new PageResponse<>(List.of(messageDto), null, 1, false,
        1L);
    
    given(messageService.findAllByChannelId(eq(channelId), any(Instant.class),
        any(Pageable.class))).willReturn(
        pageResponse);

    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString())
            .param("cursor", cursor.toString()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content[0].content").value("content"));
  }

  @Test
  @DisplayName("메시지 삭제 실패")
  void delete_fail() throws Exception {
    UUID messageId = UUID.randomUUID();

    doThrow(new MessageNotFoundException(messageId))
        .when(messageService)
        .delete(messageId);

    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNotFound());
  }
}
