package br.com.pepper.demouser.domains.users.controllers;

import br.com.pepper.demouser.domains.users.controllers.dtos.UserDto;
import br.com.pepper.demouser.domains.users.services.UserService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(@RequestParam(required = false) String name,
                                                  Pageable pageable,
                                                  @RequestParam(required = false, defaultValue = "id") String sortField,
                                                  @RequestParam(required = false, defaultValue = "asc") String sortDirection) {
        Page<UserDto> users = userService.findUsers(name, pageable, sortField, sortDirection);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
}
