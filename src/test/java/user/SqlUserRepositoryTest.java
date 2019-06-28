package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class SqlUserRepositoryTest {
    private static final String TEST_ID = "test_id";

    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository = new SqlUserRepository();
    }

    @Test
    void get_alwaysReturnsSameUser() {
        User result = userRepository.get(TEST_ID);

        assertThat(result.getId(), is(TEST_ID));
        assertThat(result.getName(), is("Alex"));
        assertThat(result.getAge(), is(32));
    }
}