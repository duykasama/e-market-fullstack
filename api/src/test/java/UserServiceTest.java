import com.example.emarket.enums.Role;
import com.example.emarket.exceptions.InvalidCredentialsException;
import com.example.emarket.exceptions.UserAlreadyExistsException;
import com.example.emarket.exceptions.UserDoesNotExistException;
import com.example.emarket.models.entities.User;
import com.example.emarket.repositories.UserRepository;
import com.example.emarket.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUserByEmail() {
        User mockUser = new User();
        mockUser.setEmail("testuser@example.com");
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(mockUser);

        User user = userService.getUserByEmail("testuser@example.com");

        assertNotNull(user);
        assertEquals("testuser@example.com", user.getEmail());
    }

    @Test
    public void testRegisterUser() throws UserAlreadyExistsException {
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(null);

        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");

        // Mock userRepository to return the saved user
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId("26"); // Simulate the user being saved with an ID
            return user;
        });

        User user = userService.registerUser("newuser@example.com", "password123");

        assertNotNull(user);
        assertEquals("newuser@example.com", user.getEmail());
        assertEquals("hashedPassword", user.getPassword());
        assertEquals(Role.USER, user.getRole());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUserUserAlreadyExistsException() {
        User existingUser = new User();
        when(userRepository.findByEmail("existing@example.com")).thenReturn(existingUser);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser("existing@example.com", "password123");
        });

        verify(userRepository, never()).save(any());
    }

    @Test
    public void testLogin() throws UserDoesNotExistException, InvalidCredentialsException {
        User mockUser = new User();
        mockUser.setEmail("testuser@example.com");
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(mockUser);
        when(passwordEncoder.matches("password123", mockUser.getPassword())).thenReturn(true);

        User user = userService.login("testuser@example.com", "password123");

        assertNotNull(user);
        assertEquals("testuser@example.com", user.getEmail());
    }

    @Test
    public void testLoginUserDoesNotExistException() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        assertThrows(UserDoesNotExistException.class, () -> {
            userService.login("nonexistent@example.com", "password123");
        });
    }

    @Test
    public void testLoginInvalidCredentialsException() {
        User mockUser = new User();
        mockUser.setEmail("testuser@example.com");
        mockUser.setPassword("differentPassword"); // Not matching with the provided password
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(mockUser);
        when(passwordEncoder.matches("password123", mockUser.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> {
            userService.login("testuser@example.com", "password123");
        });
    }
}
