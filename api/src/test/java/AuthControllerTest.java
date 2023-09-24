import com.example.emarket.controllers.AuthController;
import com.example.emarket.exceptions.InvalidCredentialsException;
import com.example.emarket.exceptions.TokenExpiredException;
import com.example.emarket.exceptions.UserAlreadyExistsException;
import com.example.emarket.exceptions.UserDoesNotExistException;
import com.example.emarket.models.entities.User;
import com.example.emarket.responses.ResponseObject;
import com.example.emarket.services.JwtService;
import com.example.emarket.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRefreshTokenValidToken() throws TokenExpiredException {
        when(jwtService.generateToken(anyString())).thenReturn("newToken");

        ResponseEntity<ResponseObject> responseEntity = authController.refreshToken("validRefreshToken");

        assertEquals(200, responseEntity.getBody().getStatusCode());
        assertEquals("Token has been refreshed", responseEntity.getBody().getMessage());
        assertEquals("newToken", responseEntity.getBody().getData());
    }

    @Test
    public void testRefreshTokenEmptyToken() {
        ResponseEntity<ResponseObject> responseEntity = authController.refreshToken("");

        assertEquals(401, responseEntity.getBody().getStatusCode());
        assertEquals("Unauthorized", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());
    }

    @Test
    public void testRegisterUserSuccess() throws UserAlreadyExistsException {
        when(userService.registerUser(anyString(), anyString())).thenReturn(new User());
        when(jwtService.generateToken(any(User.class))).thenReturn("newToken");

        ResponseEntity<ResponseObject> responseEntity = authController.register("test@example.com", "password");

        assertEquals(200, responseEntity.getBody().getStatusCode());
        assertEquals("Registered successfully", responseEntity.getBody().getMessage());
        assertEquals("newToken", responseEntity.getBody().getData());
    }

    @Test
    public void testRegisterUserAlreadyExists() throws UserAlreadyExistsException {
        when(userService.registerUser(anyString(), anyString())).thenThrow(new UserAlreadyExistsException());

        ResponseEntity<ResponseObject> responseEntity = authController.register("test@example.com", "password");

        assertEquals(401, responseEntity.getBody().getStatusCode());
        assertEquals("User already exists", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());
    }

    @Test
    public void testRegisterUserInternalError() throws UserAlreadyExistsException {
        when(userService.registerUser(anyString(), anyString())).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<ResponseObject> responseEntity = authController.register("test@example.com", "password");

        assertEquals(500, responseEntity.getBody().getStatusCode());
        assertEquals("Internal Server Error", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());
    }

    @Test
    public void testSignInSuccess() throws InvalidCredentialsException, UserDoesNotExistException {
        User user = new User();
        when(userService.login(anyString(), anyString())).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("newToken");

        ResponseEntity<ResponseObject> responseEntity = authController.signIn("test@example.com", "password", response, request);

        assertEquals(200, responseEntity.getBody().getStatusCode());
        assertEquals("Log in successfully", responseEntity.getBody().getMessage());
        assertEquals("newToken", responseEntity.getBody().getData());

        // Verify that a Cookie is set
        verify(response).addCookie(any(Cookie.class));
    }

    @Test
    public void testSignInUserDoesNotExist() throws InvalidCredentialsException, UserDoesNotExistException {
        when(userService.login(anyString(), anyString())).thenThrow(new UserDoesNotExistException());

        ResponseEntity<ResponseObject> responseEntity = authController.signIn("test@example.com", "password", response, request);

        assertEquals(401, responseEntity.getBody().getStatusCode());
        assertEquals("User does not exist", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());
    }

    @Test
    public void testSignInInvalidCredentials() throws InvalidCredentialsException, UserDoesNotExistException {
        when(userService.login(anyString(), anyString())).thenThrow(new InvalidCredentialsException());

        ResponseEntity<ResponseObject> responseEntity = authController.signIn("test@gmail.com", "123", response, request);

        assertEquals(401, responseEntity.getBody().getStatusCode());
        assertEquals("Wrong username or password", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());
    }

    @Test
    public void testSignInInternalError() throws InvalidCredentialsException, UserDoesNotExistException {
        when(userService.login(anyString(), anyString())).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity<ResponseObject> responseEntity = authController.signIn("test@example.com", "password", response, request);

        assertEquals(500, responseEntity.getBody().getStatusCode());
        assertEquals("Internal Server Error", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());
    }

    @Test
    public void testSignOut() {
        ResponseEntity<ResponseObject> responseEntity = authController.signOut(response);

        assertEquals(200, responseEntity.getBody().getStatusCode());
        assertEquals("Log out successfully", responseEntity.getBody().getMessage());

        // Verify that a Cookie is set to clear the refreshToken
        verify(response).addCookie(any(Cookie.class));
    }
}
