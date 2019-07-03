package user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@DataJpaTest
class UserRepositoryTest {
    private static final String TEST_ID = "user_repo_test_id";
    private static final String TEST_NAME = "user_repo_test_name";
    private static final int TEST_AGE = 55;
    private static final User TEST_USER = new User(TEST_ID, TEST_NAME, TEST_AGE);

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    /*
     * I don't need tests for save(), findById() since the CrudRepo generates them
     * But since this is my first time using this library, I like having these tests using an in-memory
     * DB to validate it works as I expect it to
     */

    @Test
    void save_newObject_persistsTheObject() {
        userRepository.save(TEST_USER);

        User savedUser = testEntityManager.find(User.class, TEST_ID);
        assertThat(savedUser.getId(), is(TEST_ID));
    }

    @Test
    void save_userAlreadyExists_persistsTheObject() {
        userRepository.save(TEST_USER);
        userRepository.save(TEST_USER);

        User savedUser = testEntityManager.find(User.class, TEST_ID);
        assertThat(savedUser.getId(), is(TEST_ID));
    }

    @Test
    void findById_whenUserExists_returnsUser() {
        testEntityManager.persist(TEST_USER);

        Optional<User> userOpt = userRepository.findById(TEST_ID);

        if (!userOpt.isPresent()) { fail("Expected to find user " + TEST_USER + "; but not found"); }
        User result = userOpt.get();

        assertThat(result.getId(), is(TEST_ID));
        assertThat(result.getName(), is(TEST_NAME));
        assertThat(result.getAge(), is(TEST_AGE));
    }
}