package user;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void get_whenUserExists_callsUserRepository() {
        when(mockUserRepository.findById(TEST_ID)).thenReturn(Optional.of(TEST_USER));

        User resultUser = userService.get(TEST_ID);

        verify(mockUserRepository).findById(TEST_ID);
    }

    @Test
    void get_whenUserDoesNotExist_throwsIllegalArgumentException() {
        when(mockUserRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.get(TEST_ID));
    }

    @Test
    void createOrUpdate_callsSaveOnRepo_andReturnsUser() {
        when(mockUserRepository.save(TEST_USER)).thenReturn(TEST_USER);

        User resultUser = userService.createOrUpdate(TEST_USER);

        verify(mockUserRepository).save(TEST_USER);
    }
}