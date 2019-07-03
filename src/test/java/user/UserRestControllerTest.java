package user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserRestController.class)
class UserRestControllerTest {
    private static final String TEST_ID = "user_controller_test_id";
    private static final String TEST_NAME = "user_controller_test_name";
    private static final int TEST_AGE = 77;
    private static final User TEST_USER = new User(TEST_ID, TEST_NAME, TEST_AGE);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;

    @Test
    void get_user_onSuccess_returns200_and_UserJson() throws Exception {
        when(mockUserService.get(TEST_ID)).thenReturn(TEST_USER);

        mockMvc.perform(get("/user/" + TEST_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(TEST_USER)));

        verify(mockUserService).get(TEST_ID);
    }

    // TODO: Need to implement an exception handler for my controllers
    // https://www.baeldung.com/exception-handling-for-rest-with-spring
    @Disabled
    @Test
    void get_user_whenIllegalArgumentException_returns4xx() throws Exception {
        when(mockUserService.get(TEST_ID)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get("/user/" + TEST_ID))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void put_user_whenBodyIsValidUser_callsCreateOrUpdate_andReturns200() throws Exception {
        mockMvc.perform(put("/user/" + TEST_ID)
                .content(OBJECT_MAPPER.writeValueAsBytes(TEST_USER))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockUserService).createOrUpdate(TEST_USER);
    }

    @Test
    void put_user_whenIdInBodyIsDifferent_usesIdProvidedAsPathVariable() throws Exception {
        String pathId = "put_user_path_id";
        mockMvc.perform(put("/user/" + pathId)
                .content(OBJECT_MAPPER.writeValueAsBytes(TEST_USER))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockUserService).createOrUpdate(new User(pathId, TEST_NAME, TEST_AGE));
    }

    @Test
    void put_user_whenBodyIsInvalid_returns4xx() throws Exception {
        mockMvc.perform(put("/user/" + TEST_ID)
                .content(OBJECT_MAPPER.writeValueAsBytes("foo bar"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}