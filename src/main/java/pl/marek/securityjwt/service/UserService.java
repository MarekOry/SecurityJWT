package pl.marek.securityjwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.marek.securityjwt.exception.ResourceNotFoundException;
import pl.marek.securityjwt.model.Role;
import pl.marek.securityjwt.repository.RoleRepository;
import pl.marek.securityjwt.repository.UserRepository;
import pl.marek.securityjwt.model.User;
import pl.marek.securityjwt.dto.user.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserById(Long id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User editUser(Long id, UserEditDTO userEditDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        user.setFirstName(userEditDTO.getName());
        user.setLastName(userEditDTO.getLastName());
        user.setPhoneNumber(userEditDTO.getPhoneNumber());
        return userRepository.save(user);
    }

    public User editUserActivation(Long id, UserActivationDTO userActivationDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        user.setActive(userActivationDTO.isActive());
        return userRepository.save(user);
    }

    public User changePassword(UserChangePasswordRequestDTO request, Principal connectedUser) {

        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Passwords are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return userRepository.save(user);
    }

    public User changeRoles(Long id, UserRoleDTO userRoleDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        List<Role> newRoles = new ArrayList<>();
        userRoleDTO.getNewRolesIds().forEach(roleId ->
                roleRepository.findById(roleId).ifPresent(newRoles::add));

        user.setRoles(newRoles);
        return userRepository.save(user);
    }

}
