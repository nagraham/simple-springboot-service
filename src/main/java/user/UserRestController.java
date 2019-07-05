package user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/user/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id") String id) {
        userService.delete(id);
    }

    @RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable("id") String id) {
        return userService.get(id);
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public List<User> getUsers() {
        return userService.all();
    }

    @RequestMapping(path = "/user/{id}", method = RequestMethod.PUT)
    public void putUser(@PathVariable("id") String id, @RequestBody User user) {
        user.setId(id);
        userService.createOrUpdate(user);
    }
}
