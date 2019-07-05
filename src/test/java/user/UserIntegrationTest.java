package user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {
    private static final String TEST_NAME = "user_integration_test_name";
    private static final int TEST_AGE = 67;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private List<String> testUserIds = new LinkedList<>();

    @AfterEach
    void cleanup_users() {
        testUserIds.forEach(this::deleteUserById);
    }

    @Test
    void put_user_and_get_withValidParams_succeeds() {
        User expectedUser = putUserWithId("123abc");

        User result = this.testRestTemplate.getForObject("/user/123abc", User.class);

        assertThat(result.getId(), is(expectedUser.getId()));
        assertThat(result.getName(), is(expectedUser.getName()));
        assertThat(result.getAge(), is(expectedUser.getAge()));
    }

    @Test
    void get_user_whenDoesNotExist_returns404() {
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/user/idDoesNotExist", String.class);
        assertThat(response.getStatusCodeValue(), is(404));
    }

    @Test
    void get_all_users_whenNoneExist_returns200_andEmptyList() {
        ResponseEntity<List<User>> response = this.testRestTemplate.exchange(
                "/user",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {});

        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody().size(), is(0));
    }

    @Test
    void get_all_users_whenMultipleExist_returns200_andListOfUsers() {
        User user1 = putUserWithId("getUsersTestIdOne");
        User user2 = putUserWithId("getUsersTestIdTwo");
        User user3 = putUserWithId("getUsersTestIdThree");

        ResponseEntity<List<User>> response = this.testRestTemplate.exchange(
                "/user",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {});

        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody().size(), is(3));
        assertThat(response.getBody().get(0), is(user1));
        assertThat(response.getBody().get(1), is(user2));
        assertThat(response.getBody().get(2), is(user3));
    }

    @Test
    void delete_user_whenExists_isNoLongerAvailable() {
        putUserWithId("testUserToDeleteId");

        this.testRestTemplate.delete("/user/testUserToDeleteId");

        // user no longer exists
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/user/idDoesNotExist", String.class);
        assertThat(response.getStatusCodeValue(), is(404));
    }

    @Test
    void delete_user_whenDoesNotExist_returns404() {
        ResponseEntity<Void> response = this.testRestTemplate.exchange(
                "/user/idDoesNotExist",
                HttpMethod.DELETE,
                null,
                Void.class);

        assertThat(response.getStatusCodeValue(), is(404));
    }

    private User createTestUser(String id) {
        return new User(id, TEST_NAME, TEST_AGE);
    }

    private void deleteUserById(String id) {
        this.testRestTemplate.exchange("/user/" + id, HttpMethod.DELETE, null, Void.class);
    }

    private User putUserWithId(String id) {
        testUserIds.add(id);
        User user = createTestUser(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> putHttpEntity = new HttpEntity<User>(user, httpHeaders);
        this.testRestTemplate.exchange("/user/" + id, HttpMethod.PUT, putHttpEntity, Void.class);
        return user;
    }
}
