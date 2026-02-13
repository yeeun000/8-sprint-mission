package com.sprint.mission.discodeit.controllerTest;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BasicUserService userService;

  @MockitoBean
  private BasicUserStatusService userStatusService;

  @BeforeEach
  @DisplayName("테스트 환경 설정 확인")
  void setUp() {
    assert mockMvc != null;
    assert userService != null;
    assert userStatusService != null;
  }

  @Test
  @DisplayName("유저 전체 조회 성공")
  void findAll_success() throws Exception {

    UserDto dto = new UserDto(
        UUID.randomUUID(),
        "username",
        "test",
        null,
        true
    );

    given(userService.findAll()).willReturn(List.of(dto));

    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].username").value("username"));
  }

  @Test
  @DisplayName("유저 삭제 실패")
  void delete_fail() throws Exception {
    UUID userId = UUID.randomUUID();

    doThrow(new UserNotFoundException(userId))
        .when(userService)
        .delete(userId);

    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNotFound());
  }

}
