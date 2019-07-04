package user;

public interface UserService {
    User createOrUpdate(User user);
    void delete(String id);
    User get(String id);
}
