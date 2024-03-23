package pl.marek.securityjwt.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.marek.securityjwt.dto.user.UserDTO;
import pl.marek.securityjwt.service.UserService;
import pl.marek.securityjwt.model.User;
import pl.marek.securityjwt.dto.user.*;

import java.security.Principal;
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

    @PutMapping("{id}")
    public ResponseEntity<UserDTO> editUser(@Valid @RequestBody UserEditDTO userEditDTO,
                                            @PathVariable Long id) {

        User editedUser = userService.editUser(id, userEditDTO);

        return ResponseEntity.ok(new UserDTO(editedUser));
    }

    @PutMapping("{id}/activation")
    public ResponseEntity<UserDTO> editUserActivation(@Valid @RequestBody UserActivationDTO userActivationDTO,
                                                      @PathVariable Long id) {
        User editedUser = userService.editUserActivation(id, userActivationDTO);
        return ResponseEntity.ok(new UserDTO(editedUser));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<UserDTO> changePassword(@RequestBody UserChangePasswordRequestDTO request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/roles")
    public ResponseEntity<UserDTO> editUserRoles(@Valid @RequestBody UserRoleDTO userRoleDTO,
                                                 @PathVariable Long id) {

        User editedUser = userService.changeRoles(id, userRoleDTO);

        return ResponseEntity.ok(new UserDTO(editedUser));
    }
}