package user;

import java.util.List;

public interface UserService {
    List<User> all();
    User createOrUpdate(User user);
    void delete(String id);
    User get(String id);
}
