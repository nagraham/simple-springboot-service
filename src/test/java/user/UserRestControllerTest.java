package user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    void all_whenUsersExist_returns200_and_listOfUsers() throws Exception {
        List<User> expectedUserList = Arrays.asList(
                new User(TEST_ID + "_one", TEST_NAME, TEST_AGE),
                new User(TEST_ID + "_two", TEST_NAME, TEST_AGE),
                new User(TEST_ID + "_three", TEST_NAME, TEST_AGE)
        );

        when(mockUserService.all()).thenReturn(expectedUserList);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(expectedUserList)));

        verify(mockUserService).all();
    }

    @Test
    void all_whenNoUsersAvailable_returns200_and_emptyList() throws Exception {
        when(mockUserService.all()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void delete_user_onSuccess_callsMockUserServiceAndReturns200() throws Exception {
        mockMvc.perform(delete("/user/" + TEST_ID))
                .andExpect(status().isOk());

        verify(mockUserService).delete(TEST_ID);
    }

    @Test
    void get_user_onSuccess_returns200_and_UserJson() throws Exception {
        when(mockUserService.get(TEST_ID)).thenReturn(TEST_USER);

        mockMvc.perform(get("/user/" + TEST_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(TEST_USER)));

        verify(mockUserService).get(TEST_ID);
    }

    @Test
    void get_user_whenResourceNotFoundException_returns4xx() throws Exception {
        when(mockUserService.get(TEST_ID)).thenThrow(ResourceNotFoundException.class);

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