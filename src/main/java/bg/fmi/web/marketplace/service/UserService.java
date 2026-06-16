package bg.fmi.web.marketplace.service;

import bg.fmi.web.marketplace.exception.EmailAlreadyExistsException;
import bg.fmi.web.marketplace.exception.InvalidCredentialsException;
import bg.fmi.web.marketplace.exception.ResourceNotFoundException;
import bg.fmi.web.marketplace.model.user.User;
import bg.fmi.web.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.encoder = passwordEncoder;
    }

    public User register(User user) {

        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    throw new EmailAlreadyExistsException(user.getEmail());
                });

        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);

    }

    public List<User> getAllUsers() {

        return userRepository.findAll();

    }

    public User login(User userRequest) {

        User foundUser = userRepository.findByEmail(userRequest.getEmail())
                .orElseThrow(InvalidCredentialsException::new);


        if (encoder.matches(userRequest.getPassword(), foundUser.getPassword())) {
            return foundUser;
        } else {
            throw new InvalidCredentialsException();
        }

    }

    public User getUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName(), userId));

    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
