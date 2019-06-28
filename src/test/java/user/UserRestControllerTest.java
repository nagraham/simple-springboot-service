package user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserRestControllerTest {
    private static final User TEST_USER = new User("123", "Alex", 32);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void get_user_onSuccess_returns200() throws Exception {
        mockMvc.perform(get("/user/123"))
                .andExpect(status().isOk());
    }

    @Test
    void get_user_onSuccess_returnsUserJson() throws Exception {
        mockMvc.perform(get("/user/123"))
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(TEST_USER)));
    }
}