package user;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
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
    void all_whenUsersExistInRepo_callsUserRepo_and_returnsList() {
        Iterable<User> expectedUserList = Arrays.asList(
                new User(TEST_ID + "_one", TEST_NAME, TEST_AGE),
                new User(TEST_ID + "_two", TEST_NAME, TEST_AGE),
                new User(TEST_ID + "_three", TEST_NAME, TEST_AGE)
        );

        when(mockUserRepository.findAll()).thenReturn(expectedUserList);

        List<User> results = userService.all();

        assertThat(results, containsInAnyOrder(
                new User(TEST_ID + "_one", TEST_NAME, TEST_AGE),
                new User(TEST_ID + "_two", TEST_NAME, TEST_AGE),
                new User(TEST_ID + "_three", TEST_NAME, TEST_AGE)));
        verify(mockUserRepository).findAll();
    }

    @Test
    void all_whenNoUsers_callsUserRepo_and_returnsEmptyList() {
        when(mockUserRepository.findAll()).thenReturn(new ArrayList<>());

        List<User> results = userService.all();

        assertThat(results, is(empty()));
        verify(mockUserRepository).findAll();
    }

    @Test
    void get_whenUserExists_callsUserRepository() {
        when(mockUserRepository.findById(TEST_ID)).thenReturn(Optional.of(TEST_USER));

        User resultUser = userService.get(TEST_ID);

        verify(mockUserRepository).findById(TEST_ID);
    }

    @Test
    void get_whenUserDoesNotExist_throwsResourceNotFoundException() {
        when(mockUserRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.get(TEST_ID));
    }

    @Test
    void createOrUpdate_callsSaveOnRepo_andReturnsUser() {
        when(mockUserRepository.save(TEST_USER)).thenReturn(TEST_USER);

        User resultUser = userService.createOrUpdate(TEST_USER);

        verify(mockUserRepository).save(TEST_USER);
    }

    @Test
    void delete_callsDeleteOnUserRepository() {
        userService.delete(TEST_ID);

        verify(mockUserRepository).deleteById(TEST_ID);
    }

    @Test
    void delete_repoThrowsEmptyResultDataAccessException_throwResourceNotFoundException() {
        doThrow(EmptyResultDataAccessException.class)
                .when(mockUserRepository)
                .deleteById(TEST_ID);

        assertThrows(ResourceNotFoundException.class, () -> userService.delete(TEST_ID));
    }
}