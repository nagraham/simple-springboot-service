package user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
}
