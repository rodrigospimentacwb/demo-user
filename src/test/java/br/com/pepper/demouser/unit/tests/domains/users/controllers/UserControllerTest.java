package br.com.pepper.demouser.unit.tests.domains.users.controllers;

import br.com.pepper.demouser.commons.AbstractUnitTest;
import br.com.pepper.demouser.domains.users.controllers.UserController;
import br.com.pepper.demouser.domains.users.controllers.dtos.UserDto;
import br.com.pepper.demouser.domains.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest extends AbstractUnitTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

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
        UserDto userDto = createUserDto(1L);
        when(userService.findById(userId)).thenReturn(userDto);

        ResponseEntity<UserDto> responseEntity = userController.findById(userId);

        assertEquals(userId, Objects.requireNonNull(responseEntity.getBody()).id());
        verify(userService, times(1)).findById(userId);
    }
}