package br.com.pepper.demouser.unit.tests.domains.users.services;

import br.com.pepper.demouser.commons.AbstractUnitTest;
import br.com.pepper.demouser.domains.UserService;
import br.com.pepper.demouser.domains.application.handlers.CustomException;
import br.com.pepper.demouser.domains.auth.controllers.dtos.SignUpDto;
import br.com.pepper.demouser.domains.notifications.services.EmailService;
import br.com.pepper.demouser.domains.users.controllers.dtos.UserDto;
import br.com.pepper.demouser.domains.users.models.User;
import br.com.pepper.demouser.domains.users.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(OutputCaptureExtension.class)
public class UserServiceTest extends AbstractUnitTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    public void testFindByIdUserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(createUser(1L)));

        UserDto result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    public void testFindByIdUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> userService.findById(1L));
    }

    @Test
    public void testAddUserAndNotify(CapturedOutput output) throws InterruptedException {
        SignUpDto signUpDto = createSignUpDto();
        when(passwordEncoder.encode(signUpDto.password())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(createUser(1L));
        doCallRealMethod().when(emailService).sendEmail(any(), any(), any());

        User user = userService.addUserAndNotify(signUpDto);

        assertNotNull(user);
        assertEquals(signUpDto.email(), user.getEmail());
        verify(userRepository).save(userCaptor.capture());
        assertEquals(USER_NAME, userCaptor.getValue().getName());

        // TODO PEPPER Leaving it just as a proof of concept, I will remove it later
        Thread.sleep(3000);
        Assertions.assertTrue(output.toString().contains("Email sent successfully."));
    }

    @Test
    public void testSaveUserFailure() {
        SignUpDto signUpDto = createSignUpDto();
        when(passwordEncoder.encode(signUpDto.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException());

        assertThrows(CustomException.class, () -> userService.saveUser(signUpDto));
    }

    @Test
    public void testFindUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(createUser(1L));
        userList.add(createUser(1L, "Alice", "alice.test@gmail.com", "woloolooloo"));
        Page<User> userPage = new PageImpl<>(userList);

        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        Pageable pageable = PageRequest.of(0, 10);
        Page<UserDto> resultPage = userService.findUsers(null, pageable, "id", "asc");

        assertEquals(userPage.getTotalElements(), resultPage.getTotalElements());
        assertEquals(userPage.getContent().size(), resultPage.getContent().size());
    }

    @Test
    public void testConvertToDto() {
        User user = createUser(1L);

        UserDto result = userService.convertToDto(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.id());
        assertEquals(user.getName(), result.name());
        assertEquals(user.getEmail(), result.email());
    }
}
