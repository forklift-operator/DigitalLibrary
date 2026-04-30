package bg.fmi.web.marketplace.service;

import bg.fmi.web.marketplace.model.user.User;
import bg.fmi.web.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;

    }

    public User register(User user) {

        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    throw new RuntimeException("User with this email already exists");
                });

//        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);

    }

    public List<User> getAllUsers() {

        return userRepository.findAll();

    }

    public User login(User userRequest) {

        User foundUser = userRepository.findByEmail(userRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (foundUser.getPassword().equals(userRequest.getPassword())) {
            return foundUser;
        } else {
            throw new RuntimeException("Incorrect password");
        }

    }

    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with this id not found"));
    }
}
