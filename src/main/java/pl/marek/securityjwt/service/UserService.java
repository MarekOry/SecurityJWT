package pl.marek.securityjwt.service;

import org.springframework.stereotype.Service;
import pl.marek.securityjwt.Exception.ResourceNotFoundException;
import pl.marek.securityjwt.repository.UserRepository;
import pl.marek.securityjwt.model.User;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
