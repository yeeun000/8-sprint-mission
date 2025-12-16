//package com.sprint.mission.discodeit.repository.file;
//
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.entity.UserStatus;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.UserStatusRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.UUID;
//
//@Repository
//public class FileUserStatusRepository extends FileRepository<UserStatus> implements UserStatusRepository {
//
//    private UserRepository userRepository;
//
//    public FileUserStatusRepository(UserRepository userRepository) {
//        super("src/main/java/com/sprint/mission/discodeit/service/data/UserStatus.ser");
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public boolean onlineStatus(UUID id) {
//        User findId = userRepository.findId(id);
//        UserStatus status = new UserStatus(findId.getId());
//        return status.accessTime();
//    }
//
//    @Override
//    public void add(UserStatus status) {
//        getFile().put(status.getId(), status);
//    }
//
//    @Override
//    public UserStatus find(UUID id) {
//        return getFile().get(id);
//    }
//
//    @Override
//    public List<UserStatus> findAll() {
//        return getFile().values().stream().toList();
//    }
//
//    @Override
//    public void remove(UUID userId) {
//        getFile().remove(userId);
//    }
//}
