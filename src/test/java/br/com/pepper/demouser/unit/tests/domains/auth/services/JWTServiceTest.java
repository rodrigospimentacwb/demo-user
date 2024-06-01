package br.com.pepper.demouser.unit.tests.domains.auth.services;

import br.com.pepper.demouser.commons.AbstractUnitTest;
import br.com.pepper.demouser.domains.auth.services.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

public class JWTServiceTest extends AbstractUnitTest {
    @InjectMocks
    private JWTService jwtService;

    @BeforeEach
    public void setUp() {
        forceValue(jwtService, "jwtSecret", JWT_TEST_SECRET);
        forceValue(jwtService, "jwtExpirationInMs", JWT_TEST_EXPIRATION_MILIS);
    }

    @Test
    public void testGenerateToken() {
        UserDetails userDetails = User.withUsername(USER_EMAIL).password(USER_PASSWORD).roles("USER").build();
        String token = jwtService.generateToken(userDetails);
        String extractedUsername = jwtService.extractUsername(token);

        assertEquals(USER_EMAIL, extractedUsername);
    }

    @Test
    public void testIsTokenValid() {
        UserDetails userDetails = User.withUsername(USER_EMAIL).password(USER_PASSWORD).roles("USER").build();
        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    public void testIsTokenValidWithExpiredToken() throws InterruptedException {
        forceValue(jwtService, "jwtExpirationInMs", 1);

        UserDetails userDetails = User.withUsername(USER_EMAIL).password(USER_PASSWORD).roles("USER").build();
        String token = jwtService.generateToken(userDetails);

        Thread.sleep(3); // TODO PEPPER It's ugly but I haven't found a solution for it yet

        assertThrows(ExpiredJwtException.class, () ->jwtService.isTokenValid(token, userDetails));
    }
}