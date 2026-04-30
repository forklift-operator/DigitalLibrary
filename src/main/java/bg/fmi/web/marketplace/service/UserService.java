package bg.fmi.web.marketplace.service;

import bg.fmi.web.marketplace.model.user.User;
import bg.fmi.web.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        List<User> foundUser = userRepository.findByEmail(user.getEmail());
        if (!foundUser.isEmpty()) throw new RuntimeException("User with this email already exists");
//        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
