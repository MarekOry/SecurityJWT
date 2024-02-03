package pl.marek.securityjwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marek.securityjwt.dto.UserDTO;
import pl.marek.securityjwt.service.UserService;
import pl.marek.securityjwt.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> get(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new UserDTO(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> usersDTOS = new ArrayList<>();
        users.forEach(user -> usersDTOS.add(new UserDTO(user)));
        return ResponseEntity.ok(usersDTOS);
    }
}