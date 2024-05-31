package br.com.pepper.demouser.domains.users.controllers;

import br.com.pepper.demouser.domains.users.controllers.dtos.UserDto;
import br.com.pepper.demouser.domains.users.services.UserService;
import br.com.pepper.demouser.test.commons.AbstractTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest extends AbstractTestUtils {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetUsers() {
        Pageable pageable = Pageable.unpaged();
        when(userService.findUsers(null, pageable, "id", "asc")).thenReturn(Page.empty());

        ResponseEntity<Page<UserDto>> responseEntity = userController.getUsers(null, pageable, "id", "asc");

        assertEquals(Collections.emptyList(), Objects.requireNonNull(responseEntity.getBody()).getContent());
        verify(userService, times(1)).findUsers(null, pageable, "id", "asc");
    }

    @Test
    void testFindById() {
        Long userId = 1L;
        UserDto userDto = new UserDto(userId, "Johnny Silverhand", "jonny.silver@gmail.com");
        when(userService.findById(userId)).thenReturn(userDto);

        ResponseEntity<UserDto> responseEntity = userController.findById(userId);

        assertEquals(userId, Objects.requireNonNull(responseEntity.getBody()).id());
        verify(userService, times(1)).findById(userId);
    }
}