package user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserRestController.class)
class UserRestControllerTest {
    private static final User TEST_USER = new User("123", "Alex", 32);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;

    @Test
    void get_user_onSuccess_returns200() throws Exception {
        when(mockUserService.getById("123")).thenReturn(TEST_USER);
        mockMvc.perform(get("/user/123"))
                .andExpect(status().isOk());
    }

    @Test
    void get_user_onSuccess_returnsUserJson() throws Exception {
        when(mockUserService.getById("123")).thenReturn(TEST_USER);
        mockMvc.perform(get("/user/123"))
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(TEST_USER)));
    }
}