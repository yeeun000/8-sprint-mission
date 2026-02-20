package com.sprint.mission.discodeit.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.userDTO.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BasicUserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("유저 생성")
    void userCreate_success() throws Exception {
        UserCreateRequest request = new UserCreateRequest(
                "username",
                "email",
                "password"
        );

        MockMultipartFile partFile = new MockMultipartFile(
                "userCreateRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(request)
        );

        MockMultipartFile profile = new MockMultipartFile(
                "profile",
                "name",
                "png",
                "profile".getBytes()
        );


        mockMvc.perform(multipart("/api/users")
                        .file(partFile)
                        .file(profile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated());

        User savedUser = userRepository.findByUsername("username").orElseThrow();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("email");

    }

    @Test
    @DisplayName("유저 삭제")
    void userDelete_success() throws Exception {
        UserCreateRequest userRequest = new UserCreateRequest(
                "username",
                "test",
                "password"
        );

        UserDto userDto = userService.create(userRequest, null);
        UUID id = userDto.id();

        mockMvc.perform(delete("/api/users/{userId}", id))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(id)).isEmpty();
    }

}
