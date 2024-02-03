package pl.marek.securityjwt.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.marek.securityjwt.dto.UserRegisterDTO;
import pl.marek.securityjwt.exception.ResourceNotFoundException;
import pl.marek.securityjwt.exception.RestException;
import pl.marek.securityjwt.model.RefreshToken;
import pl.marek.securityjwt.model.User;
import pl.marek.securityjwt.repository.RefreshTokenRepository;
import pl.marek.securityjwt.repository.RoleRepository;
import pl.marek.securityjwt.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository,
                                 PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void register(UserRegisterDTO userRegisterDTO) {
        if (emailTaken(userRegisterDTO.getEmail())) {
            throw new RestException("Exception.user.with.this.email.already.exists");
        }
        final User user = new User();
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setFirstName(userRegisterDTO.getFirstName());
        user.setLastName(userRegisterDTO.getLastName());
        user.setPhoneNumber(userRegisterDTO.getPhoneNumber());
        user.setActive(false);
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpirationDate(null);

        user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_EMPLOYEE")));
        userRepository.save(user);

    }

    public void registerRefreshToken(String refreshToken, String email, LocalDateTime expirationTime){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User.not.found"));
        RefreshToken token = new RefreshToken(passwordEncoder.encode(refreshToken), user, expirationTime);
        refreshTokenRepository.save(token);
    }

    private boolean emailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}