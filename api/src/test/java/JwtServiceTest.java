import com.example.emarket.exceptions.TokenExpiredException;
import com.example.emarket.models.entities.User;
import com.example.emarket.services.JwtService;
import com.example.emarket.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserService userService;

    private static final String SECRET_KEY = "ownrJE4LNVXTBOUdVZ2xmJ7VSDNhKTRJsagLsdS3jLfsOY91basfKf";

    @BeforeEach
    public void setup() {
        userService = mock(UserService.class);
        jwtService = new JwtService(userService);
    }

    @Test
    public void testGenerateToken() {
        // Mock user details
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        // Generate token
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    public void testGenerateRefreshToken() {
        // Mock user details
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        // Generate refresh token
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        assertNotNull(refreshToken);
        assertTrue(refreshToken.length() > 0);
    }

    @Test
    public void testIsValidToken() {
        // Mock user details
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("testuser");

        // Generate token
        String token = jwtService.generateToken(user);

        // Validate token
        boolean isValid = jwtService.isValidToken(token, user);

        assertTrue(isValid);
    }

    @Test
    public void testGenerateTokenFromRefreshToken() throws TokenExpiredException {
        // Mock UserService to return user
        User user = new User();
        user.setEmail("testuser@example.com");
        when(userService.getUserByEmail("testuser@example.com")).thenReturn(user);

        // Generate refresh token
        String refreshToken = jwtService.generateRefreshToken(user);

        // Generate token from refresh token
        String newToken = jwtService.generateToken(refreshToken);

        assertNotNull(newToken);
        assertTrue(newToken.length() > 0);
    }
}
