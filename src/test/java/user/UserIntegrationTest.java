package user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {
    private static final String TEST_NAME = "user_integration_test_name";
    private static final int TEST_AGE = 67;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void put_user_and_get_withValidParams_succeeds() {
        User expectedUser = createTestUser("123abc");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> putHttpEntity = new HttpEntity<User>(expectedUser, httpHeaders);
        this.testRestTemplate.exchange("/user/123abc", HttpMethod.PUT, putHttpEntity, Void.class);

        User result = this.testRestTemplate.getForObject("/user/123abc", User.class);

        assertThat(result.getId(), is(expectedUser.getId()));
        assertThat(result.getName(), is(expectedUser.getName()));
        assertThat(result.getAge(), is(expectedUser.getAge()));
    }

    // TODO: Enable after exception mapping is set up
    @Disabled
    @Test
    void get_user_whenDoesNotExist_returns4xx() {
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/user/idDoesNotExist", String.class);
        assertThat(response.getStatusCodeValue(), is(404));
    }

    private User createTestUser(String id) {
        return new User(id, TEST_NAME, TEST_AGE);
    }
}
