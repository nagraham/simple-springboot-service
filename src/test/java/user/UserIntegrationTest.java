package user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void get_user_whenUserExists_returnsUser() {
        User result = this.testRestTemplate.getForObject("/user/123", User.class);
        assertThat(result.getId(), is("123"));
        assertThat(result.getName(), is("Alex"));
        assertThat(result.getAge(), is(32));
    }
}
