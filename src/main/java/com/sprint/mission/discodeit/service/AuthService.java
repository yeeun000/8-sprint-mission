package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.dto.LoginResponse;
import com.sprint.mission.discodeit.entity.User;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}
