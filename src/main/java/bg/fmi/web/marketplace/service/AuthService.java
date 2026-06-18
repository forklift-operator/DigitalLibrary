package bg.fmi.web.marketplace.service;

import bg.fmi.web.marketplace.exception.EmailAlreadyExistsException;
import bg.fmi.web.marketplace.exception.InvalidCredentialsException;
import bg.fmi.web.marketplace.model.user.User;
import bg.fmi.web.marketplace.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User register(User user) {

        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    throw new EmailAlreadyExistsException(user.getEmail());
                });

        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);

    }

    public User login(User user) {

        User foundUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(InvalidCredentialsException::new);


        if (encoder.matches(user.getPassword(), foundUser.getPassword())) {
            return foundUser;
        } else {
            throw new InvalidCredentialsException();
        }

    }

}
