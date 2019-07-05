package user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> all() {
        Iterable<User> userIterable = userRepository.findAll();
        return iterableToList(userIterable);
    }

    public User createOrUpdate(User user) {
        return userRepository.save(user);
    }

    public void delete(String id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("User with id=" + id + " does not exist");
        }
    }

    public User get(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User with id=" + id + " does not exist");
        }
        return userOptional.get();
    }

    private List<User> iterableToList(Iterable<User> userIterable) {
        List<User> userList = new ArrayList<>();
        userIterable.forEach(userList::add);
        return userList;
    }
}
