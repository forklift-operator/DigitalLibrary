package bg.fmi.web.marketplace.service;

import bg.fmi.web.marketplace.dto.UserResponseDto;
import bg.fmi.web.marketplace.exception.ResourceNotFoundException;
import bg.fmi.web.marketplace.exception.UnauthorisedException;
import bg.fmi.web.marketplace.model.User;
import bg.fmi.web.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
    }


    public List<User> getAllUsers() {

        return userRepository.findAll();

    }

    public User getUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName(), userId));

    }

    public User updateUser(Long userId, UserResponseDto updatedUser) {
        if (!userId.equals(updatedUser.getId())) {
            throw new UnauthorisedException();
        }
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName(), userId));

        if (updatedUser.getFirstName() != null && !updatedUser.getFirstName().isBlank()) {
            existingUser.setFirstName(updatedUser.getFirstName());
        }

        if (updatedUser.getLastName() != null && !updatedUser.getLastName().isBlank()) {
            existingUser.setLastName(updatedUser.getLastName());
        }

        String updatedEmail = updatedUser.getEmail();
        userRepository.findByEmail(updatedEmail).ifPresent(user -> {
            if (!user.getId().equals(userId)) {
                throw new IllegalArgumentException("Email is already in use by another user.");
            }
        });

        existingUser.setEmail(updatedEmail);
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
