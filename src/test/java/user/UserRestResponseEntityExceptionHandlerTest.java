package user;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class UserRestResponseEntityExceptionHandlerTest {

    UserRestResponseEntityExceptionHandler handler = new UserRestResponseEntityExceptionHandler();

    @Test
    void handleResourceNotFound_anyException_returnsResponseEntityWith404AndStatusMessage() {
        ResponseEntity<Object> result = handler.handleResourceNotFound(
                new RuntimeException("Nothing found!"),
                null);

        assertThat(result.getStatusCodeValue(), is(404));
        assertThat(result.getBody().toString(), is("Resource not found"));
    }
}