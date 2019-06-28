package user;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {
    private static final String TEST_ID = "test_id";
    private static final String TEST_NAME = "test_name";
    private static final int TEST_AGE = 42;
    private static final User TEST_USER = new User(TEST_ID, TEST_NAME, TEST_AGE);

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Test
    void getById_whenUserExists_callsUserRepository() {
        when(mockUserRepository.get(TEST_ID)).thenReturn(TEST_USER);

        User resultUser = userService.getById(TEST_ID);

        verify(mockUserRepository).get(TEST_ID);
    }
}