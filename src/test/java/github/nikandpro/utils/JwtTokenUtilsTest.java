package github.nikandpro.utils;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenUtilsTest {

    private JwtTokenUtils jwtTokenUtils;

    @BeforeEach
    void setUp() {
        jwtTokenUtils = new JwtTokenUtils("testSecretKeyWithAtLeast32Characters123", 3600000);
    }

    @Test
    void generateToken_Success() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        // Act
        String token = jwtTokenUtils.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void getEmailFromToken_Success() {
        // Arrange
        String testEmail = "test@example.com";
        String token = jwtTokenUtils.generateToken(createUserDetails(testEmail));

        // Act
        String extractedEmail = jwtTokenUtils.getEmailFromToken(token);

        // Assert
        assertEquals(testEmail, extractedEmail);
    }
@Test
void validateToken_ValidToken_ReturnsTrue() {
    // Arrange
    String testEmail = "test@example.com";
    UserDetails userDetails = createUserDetails(testEmail);
    String token = jwtTokenUtils.generateToken(userDetails);

    // Act
    boolean isValid = jwtTokenUtils.validateToken(token, userDetails);

    // Assert
    assertTrue(isValid);
}

    @Test
    void validateToken_InvalidToken_ReturnsFalse() {
        // Arrange
        UserDetails userDetails = createUserDetails("test@example.com");
        String invalidToken = "invalid.token.here";

        // Act
        boolean isValid = jwtTokenUtils.validateToken(invalidToken, userDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void getAllClaimsFromToken_InvalidToken_ThrowsException() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act & Assert
        assertThrows(JwtException.class, () ->
                jwtTokenUtils.getAllClaimsFromToken(invalidToken)
        );
    }

    @Test
    void validateToken_DifferentUsers_ReturnsFalse() {
        // Arrange
        String token = jwtTokenUtils.generateToken(createUserDetails("user1@example.com"));
        UserDetails differentUser = createUserDetails("user2@example.com");

        // Act
        boolean isValid = jwtTokenUtils.validateToken(token, differentUser);

        // Assert
        assertFalse(isValid);
    }

    private UserDetails createUserDetails(String username) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.emptyList();
            }

            @Override
            public String getPassword() {
                return "testPassword";
            }

            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }
}
