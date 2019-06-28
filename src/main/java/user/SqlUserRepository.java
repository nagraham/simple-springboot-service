package user;

import org.springframework.stereotype.Repository;

@Repository
public class SqlUserRepository implements UserRepository {
    @Override
    public User get(String id) {
        return new User(id, "Alex", 32);
    }
}
