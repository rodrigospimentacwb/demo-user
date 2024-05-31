package br.com.pepper.demouser.domains.users.services;

import br.com.pepper.demouser.domains.application.handlers.CustomException;
import br.com.pepper.demouser.domains.auth.controllers.dtos.SignUpDto;
import br.com.pepper.demouser.domains.notifications.services.EmailService;
import br.com.pepper.demouser.domains.users.controllers.dtos.UserDto;
import br.com.pepper.demouser.domains.users.models.User;
import br.com.pepper.demouser.domains.users.repositories.UserRepository;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRepository userRepository;

    @Resource
    private EmailService emailService;

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
    }

    public User addUserAndNotify(SignUpDto newUserData) {
        User user = saveUser(newUserData);
        CompletableFuture.runAsync(() -> sendWelcomeEmail(user.getEmail()));
        return user;
    }

    @Transactional
    public User saveUser(SignUpDto newUserData) {
        try {
            User user = new User(newUserData.name(),newUserData.email(),passwordEncoder.encode(newUserData.password()));
            return userRepository.save(user);
        } catch (Exception ex) {
            throw new CustomException("Failed to save new user", HttpStatus.CONFLICT, ex);
        }
    }

    @Transactional(readOnly = true)
    public Page<UserDto> findUsers(String name, Pageable pageable, String sortField, String sortDirection) {
        Sort sort = Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<User> usersPage = name != null && !name.isEmpty() ?
                userRepository.findByNameContainingIgnoreCase(name, pageable) :
                userRepository.findAll(pageable);

        return usersPage.map(this::convertToDto);
    }

    @Async
    private CompletableFuture<Void> sendWelcomeEmail(String email) {
        try {
            String subject = "Welcome Aboard: Your New Account Information";
            String body = "Body Body";
            emailService.sendEmail(email, subject, body);
            return CompletableFuture.completedFuture(null);
        } catch (Exception ex) {
            logger.warn("Fail, welcome email not send", ex);
            return CompletableFuture.failedFuture(ex);
        }
    }

    public UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
