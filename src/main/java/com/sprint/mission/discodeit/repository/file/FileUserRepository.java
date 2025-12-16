//package com.sprint.mission.discodeit.repository.file;
//
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.UUID;
//
//@Repository
//public class FileUserRepository extends FileRepository<User> implements UserRepository {
//
//    public FileUserRepository() {
//        super("src/main/java/com/sprint/mission/discodeit/service/data/user.ser");
//    }
//
//    @Override
//    public void add(User user) {
//        getFile().put(user.getId(), user);
//        saveFile();
//    }
//
//    @Override
//    public List<User> findAll() {
//        return getFile().values().stream().toList();
//    }
//
//    @Override
//    public User findId(UUID userid) {
//        boolean find = getFile().containsKey(userid);
//        if (find) {
//            saveFile();
//            return getFile().get(userid);
//        } else return null;
//    }
//
//    @Override
//    public void remove(UUID userId) {
//        getFile().remove(userId);
//        saveFile();
//    }
//
//    @Override
//    public boolean existsName(String name) {
//        for (User user : getFile().values()) {
//            if (name.equals(user.getName()))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean existsEmail(String email) {
//        for (User user : getFile().values()) {
//            if (email.equals(user.getEmail()))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public User findName(String name) {
//        for (User user : getFile().values()) {
//            if (name.equals(user.getName()))
//                return user;
//        }
//        return null;
//    }
//
//}
