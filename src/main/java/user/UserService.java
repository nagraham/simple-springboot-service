package user;

public interface UserService {
    User createOrUpdate(User user);
    User get(String id);
}
