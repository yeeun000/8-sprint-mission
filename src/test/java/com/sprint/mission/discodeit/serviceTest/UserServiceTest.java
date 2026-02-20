package com.sprint.mission.discodeit.serviceTest;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.dto.userDTO.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 테스트")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BasicUserService userService;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @BeforeEach
    @DisplayName("테스트 환경 설정 확인")
    void setUp() {
        assertNotNull(userRepository);
        assertNotNull(userMapper);
        assertNotNull(userService);
        assertNotNull(binaryContentRepository);
        assertNotNull(binaryContentStorage);
    }

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("아이디, 이메일, 비밀번호 작성되면 유저가 생성되어야 한다.")
        void create_whenAllWrite_UserSuccess() {
            UserCreateRequest request = new UserCreateRequest(
                    "username",
                    "test@test",
                    "password"
            );

            given(userRepository.existsByUsername("username")).willReturn(false);
            given(userRepository.existsByEmail("test@test")).willReturn(false);
            given(userRepository.save(any(User.class)))
                    .willAnswer(invocation -> invocation.getArgument(0));

            UserDto fakeDto = new UserDto(
                    UUID.randomUUID(),
                    "test",
                    "test",
                    null,
                    true
            );

            given(userMapper.toDto(any(User.class))).willReturn(fakeDto);
            UserDto result = userService.create(request, null);

            assertNotNull(result);

            then(userRepository).should().save(userCaptor.capture());
            User savedUser = userCaptor.getValue();

            assertEquals("username", savedUser.getUsername());
            assertEquals("test@test", savedUser.getEmail());

        }

        @Test
        @DisplayName("이메일이 같으면 생성 실패해야 한다.")
        void create_whenEmailDuplicate_UserFailure() {
            UserCreateRequest request = new UserCreateRequest(
                    "username",
                    "test@test",
                    "password"
            );

            given(userRepository.existsByUsername("username")).willReturn(false);
            given(userRepository.existsByEmail("test@test")).willReturn(true);

            DuplicateEmailException exception = assertThrows(
                    DuplicateEmailException.class,
                    () -> userService.create(request, null),
                    "이미 존재하는 이메일입니다."
            );

            assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("수정 테스트")
    class updateTest {

        @Test
        @DisplayName("하나 이상 입력하면 변경할 수 있다.")
        void update_whenWrite_UserSuccess() {
            UserUpdateRequest request = new UserUpdateRequest(
                    null,
                    "test@test",
                    null
            );
            BinaryContentCreateRequest newProfile = new BinaryContentCreateRequest(
                    "profile",
                    "png",
                    "abc".getBytes()
            );
            BinaryContent oldProfile = new BinaryContent("old", 1L, "png");
            User user = new User("username", "basic@test", "password", oldProfile);
            UUID userId = UUID.randomUUID();
            ReflectionTestUtils.setField(user, "id", userId);

            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(userRepository.existsByEmail("test@test")).willReturn(false);
            given(binaryContentRepository.save(any(BinaryContent.class)))
                    .willAnswer(invocation -> invocation.getArgument(0));

            userService.update(userId, request, newProfile);

            then(binaryContentRepository).should().save(any(BinaryContent.class));
            then(binaryContentRepository).should().deleteById(oldProfile.getId());

            assertEquals("username", user.getUsername());
            assertEquals("test@test", user.getEmail());

        }

        @Test
        @DisplayName("이메일만 변경하는 경우 성공해야 한다.")
        void update_WriteEmail_Success() {
            UUID userId = UUID.randomUUID();
            User user = new User("user", "old@test", "pw", null);
            UserUpdateRequest request = new UserUpdateRequest(null, "new@test", null);

            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(userRepository.existsByEmail("new@test")).willReturn(false);

            userService.update(userId, request, null);

            assertEquals("new@test", user.getEmail());

            then(binaryContentRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("새 프로필을 추가할 수 있다.")
        void update_addNewProfile_Success() {
            UUID userId = UUID.randomUUID();
            User user = new User("user", "test@test", "pw", null);
            UserUpdateRequest request = new UserUpdateRequest(null, null, null);
            BinaryContentCreateRequest profileRequest = new BinaryContentCreateRequest("new", "png", "new".getBytes());

            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(binaryContentRepository.save(any(BinaryContent.class)))
                    .willAnswer(inv -> inv.getArgument(0));

            userService.update(userId, request, profileRequest);

            then(binaryContentRepository).should().save(any(BinaryContent.class));
            then(binaryContentStorage).should().put(any(), any());

            then(binaryContentRepository).should(never()).deleteById(any());
        }

        @Test
        @DisplayName("이미 존재하는 이메일이면 실패해야한다.")
        void update_whenEmailDuplicate_UserFailure() {
            UserUpdateRequest request = new UserUpdateRequest(
                    null,
                    "test@test",
                    null
            );

            User user = new User("username", "basic@test", "password", null);

            given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
            given(userRepository.existsByEmail("test@test")).willReturn(true);

            DuplicateEmailException exception = assertThrows(
                    DuplicateEmailException.class,
                    () -> userService.update(user.getId(), request, null),
                    "이미 존재하는 이메일입니다."
            );

            assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("삭제 테스트")
    class DeleteTest {

        @Test
        @DisplayName("유저 정보가 있으면 삭제할 수 있다.")
        void delete_whenUserIdExist_UserSuccess() {
            UUID userId = UUID.randomUUID();

            given(userRepository.existsById(userId)).willReturn(true);

            userService.delete(userId);

            then(userRepository).should().deleteById(userId);
        }

        @Test
        @DisplayName("유저 정보가 없으면 실패해야 한다.")
        void delete_whenUserIdNotExist_UserFailure() {
            UUID userId = UUID.randomUUID();

            given(userRepository.existsById(userId)).willReturn(false);

            UserNotFoundException exception = assertThrows(
                    UserNotFoundException.class,
                    () -> userService.delete(userId),
                    "유저를 찾을 수 없습니다."
            );

            assertEquals("유저를 찾을 수 없습니다.", exception.getMessage());
        }
    }

    @Test
    @DisplayName("유저 전체 조회를 할 수 있어야 한다.")
    void findAll_UserSuccess() {
        given(userRepository.findAllWithProfileAndStatus()).willReturn(List.of(new User("username", "test", "pw", null)));
        userService.findAll();

        then(userRepository).should().findAllWithProfileAndStatus();
    }

    @Test
    @DisplayName("유저 조회할 때 유저가 없으면 실패해야 한다.")
    void find_UserFail_NotFound() {
        UUID id = UUID.randomUUID();
        given(userRepository.findById(id)).willReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.find(id),
                "유저를 찾을 수 없습니다."
        );

        assertEquals("유저를 찾을 수 없습니다.", exception.getMessage());
    }

}
