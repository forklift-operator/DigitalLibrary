package bg.fmi.web.marketplace.service;

import bg.fmi.web.marketplace.dto.UserLoginDto;
import bg.fmi.web.marketplace.exception.EmailAlreadyExistsException;
import bg.fmi.web.marketplace.exception.InvalidCredentialsException;
import bg.fmi.web.marketplace.exception.ResourceNotFoundException;
import bg.fmi.web.marketplace.model.user.Role;
import bg.fmi.web.marketplace.model.user.User;
import bg.fmi.web.marketplace.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private final ModelMapper mapper = new ModelMapper();

    @Test
    public void whenRegisterThenReturnRegisterDTO() {
        User user = new User(
                1L,
                "test",
                "test",
                "test@mail.com",
                "test",
                Role.USER,
                List.of(),
                List.of(),
                List.of()
        );

        when(userRepository.save(any(User.class))).thenReturn(user);

        User registered = userService.register(user);

        assertEquals(registered, user);
    }

    @Test
    public void whenRegisteringSameEmailThenThrow() {
        User user = new User(
                1L,
                "test",
                "test",
                "test@mail.com",
                "test",
                Role.USER,
                List.of(),
                List.of(),
                List.of()
        );

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        EmailAlreadyExistsException ex = assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.register(user)
        );

        assertEquals(ex.getMessage(), "User with this email already exists: " + user.getEmail());
    }

    @Test
    public void whenGetAllUsersIsCalledThenReturnList() {
        assertInstanceOf(List.class, userService.getAllUsers());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void loginWithValidCredentials() {

        User user = new User(
                1L,
                "test",
                "test",
                "test@mail.com",
                "test",
                Role.USER,
                List.of(),
                List.of(),
                List.of()
        );


        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertEquals(userService.login(user), user);
    }

    @Test
    public void loginWithInvalidEmailThenThrow() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        UserLoginDto invalidEmailDto = new UserLoginDto("INVALID@test.com", "test");
        User invalidEmailUser = mapper.map(invalidEmailDto, User.class);

        assertThrows(InvalidCredentialsException.class, () -> userService.login(invalidEmailUser));

    }

    @Test
    public void loginWithInvalidPasswordThenThrow() {

        User user = new User(
                1L,
                "test",
                "test",
                "test@mail.com",
                "test",
                Role.USER,
                List.of(),
                List.of(),
                List.of()
        );

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserLoginDto invalidPasswordDto = new UserLoginDto("test@mail.com", "INVALID");
        User invalidPasswordUser = mapper.map(invalidPasswordDto, User.class);

        assertThrows(InvalidCredentialsException.class, () -> userService.login(invalidPasswordUser));

    }

    @Test
    public void getUserByIdReturnsUser() {

        User user = new User(
                1L,
                "test",
                "test",
                "test@mail.com",
                "test",
                Role.USER,
                List.of(),
                List.of(),
                List.of()
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertEquals(userService.getUserById(1L), user);

    }

    @Test
    public void getUserByInvalidIdThrows() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }
}
