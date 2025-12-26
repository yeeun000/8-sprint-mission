package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.userDTO.CreateUserRequest;
import com.sprint.mission.discodeit.dto.userDTO.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDTO;
import com.sprint.mission.discodeit.dto.userDTO.UserStateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    public UserController(UserService userService,UserStatusService userStatusService) {
        this.userService = userService;
        this.userStatusService = userStatusService;
    }

    @PostMapping(value = "/user", consumes = "multipart/form-data")
    public User register(@RequestPart("createUserRequest") String createUserRequestJson,
                         @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        CreateUserRequest createUserRequest = objectMapper.readValue(createUserRequestJson, CreateUserRequest.class);

        BinaryContentDTO binaryContentDTO = null;
        if (file != null && !file.isEmpty()) {
            binaryContentDTO = new BinaryContentDTO(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
        }

        return userService.create(createUserRequest, binaryContentDTO);
    }


    @RequestMapping(value = "/user", method = RequestMethod.PUT, consumes = "multipart/form-data")
    public User update(@RequestPart("updateUserRequest") String updateUserRequestJson,
                       @RequestPart(value = "file", required = false) MultipartFile file) throws IOException{

        ObjectMapper objectMapper = new ObjectMapper();
        UpdateUserRequest updateUserRequest =
                objectMapper.readValue(updateUserRequestJson, UpdateUserRequest.class);


        BinaryContentDTO binaryContentDTO = null;
        if (file != null && !file.isEmpty()) {
            binaryContentDTO = new BinaryContentDTO(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
        }

        return userService.update(updateUserRequest, binaryContentDTO);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id){
        userService.delete(id);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<UserDTO> findAll(){
        return userService.findAll();
    }

    @RequestMapping(value = "/user/{id}/online", method = RequestMethod.PUT)
    public void updateOnline(@PathVariable UUID id) {
        userStatusService.updateByUserId( new UserStateDTO(id, Instant.now()));
    }

}
