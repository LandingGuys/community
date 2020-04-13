package life.lv.community.service;

import life.lv.community.model.User;

import java.util.List;

public interface UserService {
    void createOrUpdate(User user);
    void updateUser(User user);
    boolean findByName(String username);
    void register(User user);
    boolean login(User user);
    List<User> findNewUsers(Integer code);

    User findById(Long id);
}
