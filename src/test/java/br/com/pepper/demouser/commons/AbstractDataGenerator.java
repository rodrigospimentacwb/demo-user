package br.com.pepper.demouser.commons;

import br.com.pepper.demouser.domains.auth.controllers.dtos.SignInDto;
import br.com.pepper.demouser.domains.auth.controllers.dtos.SignUpDto;
import br.com.pepper.demouser.domains.users.controllers.dtos.UserDto;
import br.com.pepper.demouser.domains.users.models.User;
import org.springframework.test.util.ReflectionTestUtils;

public abstract class AbstractDataGenerator {

    public static final String USER_NAME = "Johnny Silverhand";
    public static final String USER_EMAIL = "jonny.silver@gmail.com";
    public static final String USER_PASSWORD = "Aa123456@";
    public static final String MOCKED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBleGFtcGxlLmNvbSIsImlhdCI6MTcxNzA3NzgxNywiZXhwIjoxNzE3MDgxNDE3fQ.yAkw495GS3vSNWhX8K1xN6c56CPEWDDmrfncGdSFGU0";
    public static final String JWT_TEST_SECRET = "5814616bfbdc942c74f1577984f0c45af4cc9e58f429fdba2b6b4b80a441eddc";
    public static final int JWT_TEST_EXPIRATION_MILIS = 3600000;

    public static void forceValue(Object object, String fieldName, Object value) {
        ReflectionTestUtils.setField(object, fieldName, value);
    }

    public User createUser() {
        return createUser(null);
    }

    public User createUser(Long id) {
        return createUser(id, USER_NAME, USER_EMAIL, USER_PASSWORD);
    }

    public User createUser(Long id, String name, String email, String password) {
        User user = new User(name, email, password);
        forceValue(user, "id", id);
        return user;
    }

    public SignInDto createSignInDto() {
        return new SignInDto(USER_EMAIL, USER_PASSWORD);
    }

    public SignUpDto createSignUpDto() {
        return new SignUpDto(USER_NAME, USER_EMAIL, USER_PASSWORD);
    }

    public UserDto createUserDto() {
        return createUserDto(null);
    }

    public UserDto createUserDto(Long id) {
        return new UserDto(id, USER_NAME, USER_EMAIL);
    }

    public static String createSignInDtoJson() {
        return createSignInDtoJson(USER_EMAIL, USER_PASSWORD);
    }

    public static String createSignInDtoJson(String email, String password) {
        return String.format("""
            {
                "email": "%s",
                "password": "%s"
            }
            """, email, password);
    }

    public static String createSignUpDtoJson() {
        return createSignUpDtoJson(USER_NAME, USER_EMAIL, USER_PASSWORD);
    }

    public static String createSignUpDtoJson(String name, String email, String password) {
        return String.format("""
            {
                "name": "%s",
                "email": "%s",
                "password": "%s"
            }
            """, name, email, password);
    }
}
